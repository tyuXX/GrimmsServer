package org.gsdistance.grimmsServer.Constructable;

public class Data<T1, T2> {
    public final T1 key;
    public final T2 value;

    public Data(T1 data1, T2 data2) {
        this.key = data1;
        this.value = data2;
    }

    public static <T1, T2> Data<T1, T2> of(T1 data1, T2 data2) {
        return new Data<>(data1, data2);
    }
}
