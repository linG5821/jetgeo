package com.ling5821.jetgeo.utils;

import com.google.common.geometry.S2Region;
import com.ling5821.jetgeo.enums.LevelEnum;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.ling5821.jetgeo.model.RegionCache;
import com.ling5821.jetgeo.model.RegionInfo;
import com.ling5821.jetgeo.model.JsonPoint;
import com.ling5821.jetgeo.model.Tuple2;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lsj
 * @date 2021/11/20 3:19
 */
public class Utils {

    public static List<List<JsonPoint>> readJsonPoint(File file) {
        return JSONUtil.readJSONArray(
            file,
            StandardCharsets.UTF_8).toBean(new TypeReference<List<List<JsonPoint>>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

    public static List<List<Tuple2<Double, Double>>> jsonPointLists2Tuple2Lists(
        List<List<JsonPoint>> jsonPointLists) {
        return jsonPointLists.stream().map((jsonPointList) -> jsonPointList.stream()
            .map((geoData) -> Tuple2.tuple(geoData.getLat(), geoData.getLng())).collect(
                Collectors.toList())).collect(Collectors.toList());
    }

    public static RegionInfo getFileNameRegionInfo(String fileName) {
        RegionInfo regionInfo = new RegionInfo();
        String filePrefixName = FileUtil.getPrefix(fileName);
        String[] info = filePrefixName.split("_");
        LevelEnum level;
        try {
            level = LevelEnum.valueOf(info[0]);
        } catch (Exception e) {
            throw new RuntimeException("Unknown region level [" + info[0] + "]");
        }
        regionInfo.setRegionLevel(level);
        switch (level) {
            case province:
                regionInfo.setRegionCode(info[1]);
                regionInfo.setRegionName(info[2]);
                break;
            case city:
                regionInfo.setParentCode(info[1]);
                regionInfo.setRegionCode(info[2]);
                regionInfo.setRegionName(info[3]);
                break;
            case district:
                regionInfo.setParentCode(info[1]);
                regionInfo.setRegionCode(info[2]);
                regionInfo.setRegionName(info[3]);
                break;
            default:
                throw new RuntimeException("Unknown region level [" + level + "]");
        }
        return regionInfo;
    }

    /**
     * 加载RegionCache对象
     *
     * @param file
     * @return
     */
    public static RegionCache loadRegionCache(File file) {
        RegionInfo regionInfo = Utils.getFileNameRegionInfo(file.getName());
        List<List<Tuple2<Double, Double>>> tuple2Lists = Utils.jsonPointLists2Tuple2Lists(
            Utils.readJsonPoint(file));

        List<S2Region> s2Regions = new ArrayList<>();
        for (List<Tuple2<Double, Double>> tuple2List : tuple2Lists) {
            S2Region s2Region = S2Util.getS2RegionByPolygon(tuple2List);
            s2Regions.add(s2Region);
        }
        RegionCache regionCache = new RegionCache();
        regionCache.setRegionLevel(regionInfo.getRegionLevel());
        regionCache.setParentCode(regionInfo.getParentCode());
        regionCache.setRegionCode(regionInfo.getRegionCode());
        regionCache.setRegionName(regionInfo.getRegionName());
        regionCache.setS2RegionList(s2Regions);
        return regionCache;
    }

    public static List<RegionCache> loadRegionCaches(List<File> files) {
        List<RegionCache> regionCacheList = new ArrayList<>();
        for (File file : files) {
            regionCacheList.add(loadRegionCache(file));
        }
        return regionCacheList;
    }

}
