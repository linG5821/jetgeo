package com.ling5821.jetgeo.config;

import com.ling5821.jetgeo.enums.LevelEnum;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lsj
 * @date 2021/11/20 2:28
 */
@Data
@NoArgsConstructor
public class JetGeoProperties {

    /**
     * 区域地理边界坐标数据所在的父目录
     */
    private String geoDataParentPath;

    /**
     * 逆地理转换到的最低级别
     */
    private LevelEnum level;

    /**
     * S2 最小/最大单元格级别 调整前请务必先了解S2算法
     */
    private int s2MinLevel = 11;
    private int s2MaxLevel = 16;

    /**
     * S2 最大单元格个数 调整前请务必先了解S2算法
     */
    private int s2MaxCells = 100;

    /**
     * 只对市级/县级缓存有效, 省级缓存在初始化时已经全部加载到内存
     */
    private int loadCacheInitialCapacity = 20;

    private int loadCacheMaximumSize = 100;

    /**
     * 在指定的过期时间内没有读写，缓存数据即失效 建议与 loadCacheRefreshAfterWrite 设置为 5:1 / 3:1 的关系
     */
    private Duration loadCacheExpireAfterAccess = Duration.of(5, ChronoUnit.MINUTES);

    /**
     * 在指定的过期时间之后访问时，刷新缓存数据，在刷新任务未完成之前，其他线程返回旧值 建议与 loadCacheRefreshAfterWrite 设置为 1:5 / 1:3 的关系
     */
    private Duration loadCacheRefreshAfterWrite = Duration.of(1, ChronoUnit.MINUTES);
}
