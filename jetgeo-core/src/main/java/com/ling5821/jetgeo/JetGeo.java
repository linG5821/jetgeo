package com.ling5821.jetgeo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.ling5821.jetgeo.cache.RegionCacheLoader;
import com.ling5821.jetgeo.config.JetGeoProperties;
import com.ling5821.jetgeo.enums.LevelEnum;
import com.ling5821.jetgeo.model.GeoInfo;
import com.ling5821.jetgeo.model.RegionCache;
import com.ling5821.jetgeo.utils.S2Util;
import com.ling5821.jetgeo.utils.Utils;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author lsj
 * @date 2021/11/20 2:45
 */
public class JetGeo {

    public static final ConcurrentHashMap<String, RegionCache> PROVINCE_REGION_CACHE = new ConcurrentHashMap<>();
    private File geoDataParentFile;
    private LevelEnum level;
    private CacheBuilder<Object, Object> loadCacheBuilder;
    public LoadingCache<String, List<RegionCache>> CITY_REGION_CACHE;
    public LoadingCache<String, List<RegionCache>> DISTRICT_REGION_CACHE;

    public JetGeo(JetGeoProperties properties) {
        this.level = properties.getLevel();
        this.geoDataParentFile = new File(properties.getGeoDataParentPath());
        S2Util.setMinLevel(properties.getS2MinLevel());
        S2Util.setMaxLevel(properties.getS2MaxLevel());
        S2Util.setMaxCells(properties.getS2MaxCells());
        S2Util.init();

        this.loadCacheBuilder = CacheBuilder.newBuilder()
            .initialCapacity(properties.getLoadCacheInitialCapacity())
            .maximumSize(properties.getLoadCacheMaximumSize())
            .expireAfterAccess(properties.getLoadCacheExpireAfterAccess())
            .refreshAfterWrite(properties.getLoadCacheRefreshAfterWrite());
        initCache(LevelEnum.province, PROVINCE_REGION_CACHE);
        initLoadingCache();

    }

    /**
     * 初始化指定区域缓存
     *
     * @param level
     * @param cache
     */
    private void initCache(LevelEnum level, Map<String, RegionCache> cache) {
        List<File> files = FileUtil.loopFiles(new File(this.geoDataParentFile, level.name()),
            pathname -> pathname.getName().matches(".+\\.json"));
        for (File file : files) {
            RegionCache regionCache = Utils.loadRegionCache(file);
            cache.put(regionCache.getCacheKey(), regionCache);
        }
    }

    private void initLoadingCache() {
        if (this.level.lessThen(LevelEnum.city)) {
            CITY_REGION_CACHE = this.loadCacheBuilder.build(
                new RegionCacheLoader(this.geoDataParentFile, LevelEnum.city));
        }

        if (this.level.lessThen(LevelEnum.district)) {
            DISTRICT_REGION_CACHE = this.loadCacheBuilder.build(
                new RegionCacheLoader(this.geoDataParentFile, LevelEnum.district));
        }
    }


    /**
     * 获取逆地理编码信息
     *
     * @param lat
     * @param lng
     * @return
     */
    public GeoInfo getGeoInfo(double lat, double lng) {
        GeoInfo geoInfo = new GeoInfo();
        geoInfo.setProvince("");
        geoInfo.setCity("");
        geoInfo.setDistrict("");
        geoInfo.setStreet("");
        geoInfo.setLevel(LevelEnum.province);

        //定位省
        containsRegion(geoInfo, PROVINCE_REGION_CACHE, lat, lng);

        if (StrUtil.isEmpty(geoInfo.getProvince())) {
            return null;
        }

        if (this.level.lessThen(LevelEnum.city)) {
            containsRegionByLoadingCache(geoInfo, CITY_REGION_CACHE, lat, lng);
        }

        if (StrUtil.isNotEmpty(geoInfo.getCity()) && level.lessThen(LevelEnum.district)) {
            containsRegionByLoadingCache(geoInfo, DISTRICT_REGION_CACHE, lat, lng);
        }

        geoInfo.setFormatAddress(geoInfo.getProvince() + geoInfo.getCity() + geoInfo.getDistrict()
            + geoInfo.getStreet());
        return geoInfo;
    }

    private void containsRegion(GeoInfo geoInfo, Map<String, RegionCache> regionCacheMap,
        double lat, double lng) {
        String parentCode = geoInfo.getAdcode();
        String regionName = "";
        String regionCode = "";
        LevelEnum regionLevel = null;
        for (String cacheKey : regionCacheMap.keySet()) {
            if (StrUtil.isNotEmpty(parentCode) && !cacheKey.startsWith(parentCode)) {
                continue;
            }
            RegionCache regionCache = regionCacheMap.get(cacheKey);
            if (regionCache.contains(lat, lng)) {
                regionName = regionCache.getRegionName();
                regionCode = regionCache.getRegionCode();
                regionLevel = regionCache.getRegionLevel();
            }
            if (StrUtil.isNotEmpty(regionName) && StrUtil.isNotEmpty(regionCode)
                && regionLevel != null) {
                break;
            }
        }
        geoInfo.setAdcode(regionCode);
        geoInfo.setLevel(regionLevel);
        geoInfo.setRegionName(regionLevel, regionName);
    }

    private void containsRegionByLoadingCache(GeoInfo geoInfo,
        LoadingCache<String, List<RegionCache>> loadingCache, double lat, double lng) {
        String parentCode = geoInfo.getAdcode();
        String regionName = "";
        String regionCode = "";
        LevelEnum regionLevel = null;
        if (StrUtil.isNotEmpty(parentCode)) {
            try {
                List<RegionCache> regionCacheList = loadingCache.get(parentCode);
                for (RegionCache regionCache : regionCacheList) {
                    if (regionCache.contains(lat, lng)) {
                        regionName = regionCache.getRegionName();
                        regionCode = regionCache.getRegionCode();
                        regionLevel = regionCache.getRegionLevel();
                        break;
                    }
                }
                geoInfo.setAdcode(regionCode);
                geoInfo.setLevel(regionLevel);
                geoInfo.setRegionName(regionLevel, regionName);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
