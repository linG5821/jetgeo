package com.ling5821.jetgeo.model;

/**
 * @author by https://blog.csdn.net/qq_43777978/article/details/116800460?ivk_sa=1024320u
 * @date 2021/11/18 16:37
 */
public class Tuple2<A, B> {

    private A a;
    private B b;

    public static Tuple2<Double, Double> tuple(Double a, Double b) {
        Tuple2<Double, Double> tuple2 = new Tuple2<>();
        tuple2.setA(a);
        tuple2.setB(b);
        return tuple2;
    }

    private void setA(A a) {
        this.a = a;
    }

    private void setB(B b) {
        this.b = b;
    }

    public A getVal1() {
        return a;
    }

    public B getVal2() {
        return b;
    }

}