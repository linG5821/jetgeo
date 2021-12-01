package com.ling5821.jetgeo.model;

import com.ling5821.jetgeo.enums.LevelEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lsj
 * @date 2021/11/20 2:52
 */
@Data
@NoArgsConstructor
public class GeoInfo {

    private String formatAddress;
    private String province;
    private String city;
    private String district;
    private String street;
    private String adcode;
    private LevelEnum level;

    /* 获取当前具有的等级, 如果搜索下级未找到,能保证此时Level标识能找到的最低等级 */
    public void setLevel(LevelEnum level) {
        if (this.level != null) {
            this.level = level;
        }
    }

    public void setRegionName(LevelEnum level, String regionName) {
        if (level != null) {
            switch (level) {
                case province:
                    this.province = regionName;
                    break;
                case city:
                    this.city = regionName;
                    break;
                case district:
                    this.district = regionName;
                    break;
                default:
                    break;
            }
        }
    }
}
