package com.ling5821.jetgeo.cache;

import cn.hutool.core.io.FileUtil;
import com.google.common.cache.CacheLoader;
import com.ling5821.jetgeo.enums.LevelEnum;
import com.ling5821.jetgeo.model.RegionCache;
import com.ling5821.jetgeo.utils.Utils;
import java.io.File;
import java.util.List;

/**
 * @author lsj
 * @date 2021/11/23 22:40
 */
public class RegionCacheLoader extends CacheLoader<String, List<RegionCache>> {

    private LevelEnum level;
    private File dataPath;

    public RegionCacheLoader(File geoDataParentPath, LevelEnum level) {
        this.level = level;
        this.dataPath = new File(geoDataParentPath, level.name());
    }

    @Override
    public List<RegionCache> load(String key) throws Exception {
        List<File> files = FileUtil.loopFiles(dataPath,
            pathname -> pathname.getName()
                .matches(level + "_" + key + "_.+\\.json"));
        return Utils.loadRegionCaches(files);
    }
}
