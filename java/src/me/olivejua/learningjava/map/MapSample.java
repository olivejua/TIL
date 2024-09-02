package me.olivejua.learningjava.map;

import java.util.HashMap;

public class MapSample {
    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {
//        int result = tableSizeFor(16);
//        System.out.println("result = " + result);

//        HashMap.md<String, String> map = new HashMap.md<>();
//        map.put("key1", "value1");
//        System.out.println();

        for (int i = 0; i < 10; ++i) {
            System.out.println("i = " + i);
        }
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}