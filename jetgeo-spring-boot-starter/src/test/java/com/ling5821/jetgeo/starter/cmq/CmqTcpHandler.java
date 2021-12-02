package com.ling5821.jetgeo.starter.cmq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ling5821.jetgeo.JetGeo;
import com.ling5821.jetgeo.model.GeoInfo;
import com.ling5821.jetgeo.starter.model.GpsInfo;
import com.qcloud.cmq.client.consumer.BatchDeleteCallback;
import com.qcloud.cmq.client.consumer.BatchDeleteResult;
import com.qcloud.cmq.client.consumer.Consumer;
import com.qcloud.cmq.client.consumer.Message;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.exception.MQServerException;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author lsj
 * @date 2020/5/12 9:36
 */
@Component
@Slf4j
public class CmqTcpHandler {

    private static final int DEFAULT_MSG_COUNT = 16;
    private static final int DEFAULT_POLLING_WAIT_SECONDS = 5;
    private static final int DEFAULT_REQUEST_TIMEOUT = 5000;
    public static final String SECRET_ID = System.getProperty("secretId");
    public static final String SECRET_KEY = System.getProperty("secretKey");
    public static final String TCP_SERVER = "http://cmq-nameserver-sh.tencentcloudapi.com";

    private final Consumer consumer;

    @Autowired
    private JetGeo jetGeo;

    public CmqTcpHandler() {
        this.consumer = initConsumer();
    }

    public Consumer initConsumer() {
        Consumer consumer = new Consumer();
        consumer.setNameServerAddress(TCP_SERVER);
        consumer.setSecretId(SECRET_ID);
        consumer.setSecretKey(SECRET_KEY);
        consumer.setBatchPullNumber(DEFAULT_MSG_COUNT);
        consumer.setPollingWaitSeconds(DEFAULT_POLLING_WAIT_SECONDS);
        consumer.setRequestTimeoutMS(DEFAULT_REQUEST_TIMEOUT);
        consumer.start();
        return consumer;
    }

    private void batchDeleteQueueMsg(String queueName, List<Long> receiptHandlerList) {
        try {
            consumer.batchDeleteMsg(queueName, receiptHandlerList, new BatchDeleteCallback() {
                @Override
                public void onSuccess(BatchDeleteResult batchDeleteResult) {
                    batchDeleteResult.getErrorList().forEach(item -> {
                        consumer.deleteMsg(queueName, item.getReceiptHandle());
                    });
                }

                @Override
                public void onException(Throwable throwable) {
                    receiptHandlerList.forEach(item -> {
                        consumer.deleteMsg(queueName, item);
                    });
                }
            });
        } catch (MQClientException | MQServerException e) {
            log.error("batchDeleteQueueMsg", e);
        }

    }

    public List<Long> handleGatewayMessage(List<Message> messageList) {
        List<Long> receiptHandlerList = new LinkedList<>();
        messageList.forEach(this::handleMessage);
        return receiptHandlerList;
    }

    public void handleMessage(Message message) {
        JSONObject jsonObject = JSONUtil.parseObj(message.getData());
        JSONObject data = (JSONObject) jsonObject.get("data");
        GpsInfo gpsInfo = JSONUtil.toBean(data, GpsInfo.class);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        GeoInfo geoInfo = jetGeo.getGeoInfo(gpsInfo.getLatitude(), gpsInfo.getLongitude());
        stopWatch.stop();
        System.out.printf("%s seconds %dms %s\n", gpsInfo.getDeviceId(),
            stopWatch.getTotalTimeMillis(), geoInfo);
    }

    public void start() {
        String queueName = "gps-test";
        this.consumer.subscribe(queueName, (queue, list) -> handleGatewayMessage(list));
    }
}
