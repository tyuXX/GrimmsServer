package org.gsdistance.grimmsServer.Constructable;

public class Data<T1, T2> {
    public T1 key;
    public T2 value;

    public Data(T1 data1, T2 data2) {
        this.key = data1;
        this.value = data2;
    }
}
