package com.ling5821.jetgeo.model;

import com.ling5821.jetgeo.enums.LevelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lsj
 * @date 2021/11/20 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionInfo {

    private LevelEnum regionLevel;
    private String parentCode;
    private String regionCode;
    private String regionName;

}
