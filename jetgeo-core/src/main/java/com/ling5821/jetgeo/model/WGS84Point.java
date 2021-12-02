package com.ling5821.jetgeo.model;

/**
 * @author by https://blog.csdn.net/qq_43777978/article/details/116800460?ivk_sa=1024320u
 * @date 2021/11/18 16:37
 */
public class WGS84Point {

    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public WGS84Point setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public WGS84Point setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
}