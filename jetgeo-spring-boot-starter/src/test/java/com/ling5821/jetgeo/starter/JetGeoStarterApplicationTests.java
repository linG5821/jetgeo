package com.ling5821.jetgeo.starter;

import com.ling5821.jetgeo.JetGeo;
import com.ling5821.jetgeo.model.GeoInfo;
import com.ling5821.jetgeo.starter.cmq.CmqTcpHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * @author lsj
 * @date 2021/11/24 15:19
 */
@SpringBootTest(classes = {JetGeoAutoConfiguration.class, CmqTcpHandler.class})
@Slf4j
public class JetGeoStarterApplicationTests {

    @Autowired
    private JetGeo jetGeo;

    @Autowired
    private CmqTcpHandler cmqTcpHandler;

    @Test
    void testJetGeo() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100000; i++) {
            GeoInfo geoInfo1 = jetGeo.getGeoInfo(35.338650457294094, 112.25346613445444);
            System.out.println(geoInfo1);
        }
        stopWatch.stop();
        log.info("time seconds {}s", stopWatch.getTotalTimeMillis() / 1000);

    }

    @Test
    void testGpsPressure() throws InterruptedException {
        cmqTcpHandler.start();
        Thread.currentThread().join();
    }
}
