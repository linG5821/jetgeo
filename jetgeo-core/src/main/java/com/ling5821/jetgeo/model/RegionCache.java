package com.ling5821.jetgeo.model;

import com.google.common.geometry.S2Region;
import com.ling5821.jetgeo.enums.LevelEnum;
import com.ling5821.jetgeo.utils.S2Util;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lsj
 * @date 2021/11/20 3:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionCache {
    private String regionName;
    private String parentCode;
    private String regionCode;
    private LevelEnum regionLevel;
    private List<S2Region> s2RegionList;

    public String getCacheKey() {
        return parentCode != null && parentCode.length() > 0 ? parentCode + ":" + regionCode : regionCode;
    }

    public boolean contains(double lat,double lng) {
        for (S2Region s2Region : this.s2RegionList) {
            if (S2Util.contains(s2Region, lat, lng)) {
                return true;
            }
        }
        return false;
    }



}
