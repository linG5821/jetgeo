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
public class JetGeoExample {
    public static final JetGeo jetGeo;
    static {
        JetGeoProperties properties = new JetGeoProperties();
        properties.setGeoDataParentPath("D:\\Projects\\idea\\jetgeo\\jetgeo-core\\src\\main\\resources");
        properties.setLevel(LevelEnum.province);
        properties.setLevel(LevelEnum.city);
        properties.setLevel(LevelEnum.district);
        jetGeo = new JetGeo(properties);
    }

    public static void main(String[] args) {
        GeoInfo geoInfo = jetGeo.getGeoInfo(32.053197915979325,118.85999259252777);
        System.out.println(geoInfo);
    }
}
