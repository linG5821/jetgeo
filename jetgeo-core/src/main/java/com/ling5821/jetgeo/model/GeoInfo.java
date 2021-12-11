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
    private String province = "";
    private String provinceCode = "";
    private String city = "";
    private String cityCode = "";
    private String district = "";
    private String districtCode = "";
    private String street = "";
    private String streetCode = "";

    /**
     * 最低一级的行政区代码
     */
    private String adcode;
    private LevelEnum level;

    /* 获取当前具有的等级, 如果搜索下级未找到,能保证此时Level标识能找到的最低等级 */
    public void setLevel(LevelEnum level) {
        if (level != null) {
            this.level = level;
        }
    }

    public void setRegionName(LevelEnum level, String regionName, String regionCode) {
        if (level != null) {
            switch (level) {
                case province:
                    this.province = regionName;
                    this.provinceCode = regionCode;
                    break;
                case city:
                    this.city = regionName;
                    this.cityCode = regionCode;
                    break;
                case district:
                    this.district = regionName;
                    this.districtCode = regionCode;
                    break;
                default:
                    break;
            }
        }
    }
}
