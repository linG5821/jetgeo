package com.ling5821.jetgeo.enums;

/**
 * @author lsj
 * @date 2021/11/20 2:32
 */
public enum LevelEnum {
    /**
     * 省/直辖市/自治区
     */
    province,

    /**
     * 市
     */
    city,

    /**
     * 区/县
     */
    district,

    /**
     * 乡镇/街道(暂不支持定位到街道)
     */
//    street
    ;

    public boolean lessThen(LevelEnum levelEnum) {
        return this.ordinal() >= levelEnum.ordinal();
    }

}
