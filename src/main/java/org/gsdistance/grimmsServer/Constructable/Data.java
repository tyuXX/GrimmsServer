package org.gsdistance.grimmsServer.Constructable;

public record Data<T1, T2>(T1 key, T2 value) {

    public static <T1, T2> Data<T1, T2> of(T1 data1, T2 data2) {
        return new Data<>(data1, data2);
    }
}
