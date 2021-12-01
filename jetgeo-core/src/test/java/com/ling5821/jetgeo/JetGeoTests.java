package com.ling5821.jetgeo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ling5821.jetgeo.config.JetGeoProperties;
import com.ling5821.jetgeo.enums.LevelEnum;
import com.ling5821.jetgeo.model.GeoInfo;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.jupiter.api.Test;

/**
 * @author lsj
 * @date 2021/11/20 14:34
 */
public class JetGeoTests {
    public static final JetGeo jetGeo;
    static {
        JetGeoProperties properties = new JetGeoProperties();
        properties.setGeoDataParentPath("D:\\Projects\\idea\\jetgeo\\jetgeo-core\\src\\main\\resources");
        properties.setLevel(LevelEnum.province);
        properties.setLevel(LevelEnum.city);
        properties.setLevel(LevelEnum.district);
        jetGeo = new JetGeo(properties);
    }

    @Test
    void initTest() {
        GeoInfo geoInfo1 = jetGeo.getGeoInfo(35.338650457294094, 112.25346613445444);
        System.out.println(geoInfo1);

        GeoInfo geoInfo2 = jetGeo.getGeoInfo(39.916755616254555,93.42899448106573);
        System.out.println(geoInfo2);

        GeoInfo geoInfo3 = jetGeo.getGeoInfo(41.03984141034477,93.39220344090528);
        System.out.println(geoInfo3);

        GeoInfo geoInfo4 = jetGeo.getGeoInfo(30.424732936810585,99.13300704545584);
        System.out.println(geoInfo4);

        GeoInfo geoInfo5 = jetGeo.getGeoInfo(30.281764911231406,98.80224205919843);
        System.out.println(geoInfo5);

        GeoInfo geoInfo6 = jetGeo.getGeoInfo(50.49211333201635,124.39579025135696);
        System.out.println(geoInfo6);

        GeoInfo geoInfo7 = jetGeo.getGeoInfo(50.47740931720987,124.46268489454489);
        System.out.println(geoInfo7);

        GeoInfo geoInfo8 = jetGeo.getGeoInfo(50.203970460629016,124.24193207907093);
        System.out.println(geoInfo8);

        GeoInfo geoInfo9 = jetGeo.getGeoInfo(50.17430508526461,124.37044861056783);
        System.out.println(geoInfo9);

        GeoInfo geoInfo10 = jetGeo.getGeoInfo(50.58880035292801,124.10597230982526);
        System.out.println(geoInfo10);

        GeoInfo geoInfo11 = jetGeo.getGeoInfo(50.54790703930835,124.11281195442807);
        System.out.println(geoInfo11);

        GeoInfo geoInfo12 = jetGeo.getGeoInfo(50.200244507206136,124.31338798795879);
        System.out.println(geoInfo12);

        GeoInfo geoInfo13 = jetGeo.getGeoInfo(22.133244733471454,113.56844351864902);
        System.out.println(geoInfo13);

        GeoInfo geoInfo14 = jetGeo.getGeoInfo(22.40322011200182,114.11298895339678);
        System.out.println(geoInfo14);
    }

    @Test
    void getCellIdTest() {
        /*for (RegionCache regionCache : JetGeo.PROVINCE_REGION_CACHE.values()) {
            List<S2Region> s2RegionList = regionCache.getS2RegionList();
            for (S2Region s2Region : s2RegionList) {
                StringBuilder sb = new StringBuilder();
                List<S2CellId> cellIdList = S2Util.getCellIdList(s2Region);
                Map<Integer,Integer> sizeCountMap = new HashMap<>();
                cellIdList.forEach(s2CellId -> {
                    System.out.println("Level:" + s2CellId.level() + ", ID:" + s2CellId.toToken() + ",Min:"
                        + s2CellId.rangeMin().toToken() + ",Max:" + s2CellId.rangeMax().toToken());
                    sb.append(",").append(s2CellId.toToken());
                    sizeCountMap.put(s2CellId.level(), sizeCountMap.getOrDefault(s2CellId.level(), 0) + 1);
                });
                System.out.println(sb.substring(1));
                System.out.println("totalSize:" + cellIdList.size());
                sizeCountMap.forEach((key, value) -> System.out.printf("level:%d,size:%d\n", key, value));
                System.out.println(regionCache.getRegionName() + "-----------------------------");
                cellIdList = null;
            }
        }*/
        System.out.println(RamUsageEstimator.humanSizeOf(JetGeo.PROVINCE_REGION_CACHE));
        System.out.println(RamUsageEstimator.humanSizeOf(jetGeo.CITY_REGION_CACHE));

    }

    void jsonDataParse() {
        String districtStr = FileUtil.readString("source_data/district.txt", StandardCharsets.UTF_8);
        String[] splitArray = districtStr.split("\r\n");
        Map<String, Integer> countMap = new HashMap<>();
        int pre = 0;
        for (String str : splitArray) {
            String[] strs = str.split(":");
            int count = Integer.parseInt(strs[2]) - pre;
            pre = Integer.parseInt(strs[2]);
            countMap.put(strs[0], count);
        }
        List<File> files = FileUtil.loopFiles("source_data/json/district");
        for (File file : files) {
            JSONArray objects = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
            for (Object object : objects) {
                JSONObject jsonObject = (JSONObject) object;
                String fileName = jsonObject.getStr("fileName");
                String data = jsonObject.getStr("data");
                FileUtil.writeString(data,"D:\\Projects\\idea\\jetgeo\\jetgeo-core\\src\\main\\resources\\district\\" + fileName + ".json" , StandardCharsets.UTF_8);
            }
            String adcode = FileUtil.getPrefix(file);
            Integer count = countMap.get(adcode);
            if (count == null) {
                System.out.println(adcode + " not count");
            } else if (count != objects.size()) {
                System.out.println(adcode + " not equal count: " + count + " size: " + objects.size());
            }
        }
    }
}
