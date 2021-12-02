package com.ling5821.jetgeo.utils;

import com.google.common.collect.Lists;
import com.google.common.geometry.S2;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
import com.google.common.geometry.S2Region;
import com.google.common.geometry.S2RegionCoverer;
import com.ling5821.jetgeo.model.Tuple2;
import com.ling5821.jetgeo.model.WGS84Point;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by https://blog.csdn.net/qq_43777978/article/details/116800460?ivk_sa=1024320u
 * @date 2021/11/18 16:37
 */
public enum S2Util {
    /**
     * 实例
     */
    INSTANCE;

    private static int minLevel = 11;
    private static int maxLevel = 16;
    private static int maxCells = 100;


    private static S2RegionCoverer COVERER;

    public static void init() {
        COVERER = S2RegionCoverer.builder()
            .setMinLevel(minLevel)
            .setMinLevel(maxLevel)
            .setMaxCells(maxCells)
            .build();
    }

    /**
     * 将单个cellId转换为多个指定level的cellId
     *
     * @param s2CellId
     * @param desLevel
     * @return
     */
    public static List<S2CellId> childrenCellId(S2CellId s2CellId, Integer desLevel) {
        return childrenCellId(s2CellId, s2CellId.level(), desLevel);
    }

    private static List<S2CellId> childrenCellId(S2CellId s2CellId, Integer curLevel,
        Integer desLevel) {
        if (curLevel < desLevel) {
            long interval = (s2CellId.childEnd().id() - s2CellId.childBegin().id()) / 4;
            List<S2CellId> s2CellIds = Lists.newArrayList();
            for (int i = 0; i < 4; i++) {
                long id = s2CellId.childBegin().id() + interval * i;
                s2CellIds.addAll(childrenCellId(new S2CellId(id), curLevel + 1, desLevel));
            }
            return s2CellIds;
        } else {
            return Lists.newArrayList(s2CellId);
        }
    }

    /**
     * 将cellToken转换为经纬度
     *
     * @param token
     * @return
     */
    public static Tuple2<Double, Double> toLatLon(String token) {
        S2LatLng latLng = new S2LatLng(S2CellId.fromToken(token).toPoint());
        return Tuple2.tuple(latLng.latDegrees(), latLng.lngDegrees());
    }

    /**
     * 将经纬度转换为cellId
     *
     * @param lat
     * @param lon
     * @return
     */
    public static S2CellId toCellId(double lat, double lon) {
        return S2CellId.fromLatLng(S2LatLng.fromDegrees(lat, lon));
    }

    /**
     * 判断region是否包含指定cellToken
     *
     * @param region
     * @param cellToken
     * @return
     */
    public static boolean contains(S2Region region, String cellToken) {
        return region.contains(new S2Cell(S2CellId.fromToken(cellToken)));
    }

    /**
     * 判断region是否包含指定经纬度坐标
     *
     * @param region
     * @param lat
     * @param lon
     * @return
     */
    public static boolean contains(S2Region region, double lat, double lon) {
        S2LatLng s2LatLng = S2LatLng.fromDegrees(lat, lon);
        try {
            boolean contains = region.contains(new S2Cell(s2LatLng));
            return contains;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据region获取cellId列表
     *
     * @param region
     * @return
     */
    public static List<S2CellId> getCellIdList(S2Region region) {
        List<S2CellId> primeS2CellIdList = COVERER.getCovering(region).cellIds();
        return primeS2CellIdList.stream()
            .flatMap(s2CellId -> S2Util.childrenCellId(s2CellId, S2Util.minLevel).stream()).collect(
                Collectors.toList());
    }

    /**
     * 根据region获取合并后的cellId列表
     *
     * @param region
     * @return
     */
    public static List<S2CellId> getCompactCellIdList(S2Region region) {
        List<S2CellId> primeS2CellIdList = COVERER.getCovering(region).cellIds();
        return primeS2CellIdList;
    }

    //获取圆形region
    public static S2Region getS2RegionByCircle(double lat, double lon, double radius) {
        double capHeight = (2 * S2.M_PI) * (radius / 40075017);
        S2Cap cap = S2Cap.fromAxisHeight(S2LatLng.fromDegrees(lat, lon).toPoint(),
            capHeight * capHeight / 2);
        S2CellUnion s2CellUnion = COVERER.getCovering(cap);
        return cap;
    }

    public static S2Region getS2RegionByCircle(WGS84Point point, double radius) {
        return getS2RegionByCircle(point.getLatitude(), point.getLongitude(), radius);
    }

    //获取矩形region
    public static S2Region geS2RegionByRect(WGS84Point point1, WGS84Point point2) {
        return getS2RegionByRect(point1.getLatitude(), point1.getLongitude(), point2.getLatitude(),
            point2.getLongitude());
    }

    public static S2Region getS2RegionByRect(Tuple2<Double, Double> point1,
        Tuple2<Double, Double> point2) {
        return getS2RegionByRect(point1.getVal1(), point1.getVal2(), point2.getVal1(),
            point2.getVal2());
    }

    public static S2Region getS2RegionByRect(double lat1, double lon1, double lat2, double lon2) {
        List<Tuple2<Double, Double>> latLonTuple2List = Lists.newArrayList(Tuple2.tuple(lat1, lon1),
            Tuple2.tuple(lat1, lon2), Tuple2.tuple(lat2, lon2), Tuple2.tuple(lat2, lon1));
        return getS2RegionByPolygon(latLonTuple2List);
    }

    //获取多边形region

    public static S2Region getS2RegionByPolygon(WGS84Point[] pointArray) {
        List<Tuple2<Double, Double>> latLonTuple2List = Lists.newArrayListWithExpectedSize(
            pointArray.length);
        for (int i = 0; i < pointArray.length; ++i) {
            latLonTuple2List.add(
                Tuple2.tuple(pointArray[i].getLatitude(), pointArray[i].getLongitude()));
        }
        return getS2RegionByPolygon(latLonTuple2List);
    }

    public static S2Region getS2RegionByPolygon(Tuple2<Double, Double>[] tuple2Array) {
        return getS2RegionByPolygon(Lists.newArrayList(tuple2Array));
    }


    /**
     * 注意需要以逆时针方向添加坐标点,多边形内部区域
     */
    public static S2Region getS2RegionByPolygon(List<Tuple2<Double, Double>> latLonTuple2List) {
        List<S2Point> pointList = Lists.newArrayList();
        for (Tuple2<Double, Double> latlonTuple2 : latLonTuple2List) {
            pointList.add(
                S2LatLng.fromDegrees(latlonTuple2.getVal1(), latlonTuple2.getVal2()).toPoint());

        }
        S2Loop s2Loop = new S2Loop(pointList);
        S2Polygon s2Polygon = new S2Polygon(s2Loop);
        return s2Polygon;

        /*S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
        builder.addLoop(s2Loop);
        return builder.assemblePolygon();*/
    }

    public static void setMinLevel(int minLevel) {
        S2Util.minLevel = minLevel;
    }

    public static void setMaxLevel(int maxLevel) {
        S2Util.maxLevel = maxLevel;
    }

    public static void setMaxCells(int maxCells) {
        S2Util.maxCells = maxCells;
    }
}
