package me.olivejua.learningjava.memory;

/**
 * Primitive Time: 2,564,041
 * Wrapper Time: 16,354,500
 */

public class PerformanceExample {
    public static void main(String[] args) {
        int sumPrimitive = 0;
        long startTimePrimitive = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            sumPrimitive += i;
        }
        long endTimePrimitive = System.nanoTime();
        System.out.println("Primitive Time: " + (endTimePrimitive - startTimePrimitive));

        Integer sumWrapper = 0;
        long startTimeWrapper = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            sumWrapper += i;
        }
        long endTimeWrapper = System.nanoTime();
        System.out.println("Wrapper Time: " + (endTimeWrapper - startTimeWrapper));
    }
}
