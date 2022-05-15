package me.olivejua.learningjava.autoboxing;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Character c = 'a';

        List<Integer> li = new ArrayList<>();
        for (int i = 1; i < 50; i += 2)
            li.add(i);


        int sum = sumEven(li);
        System.out.println("sum = " + sum);
    }

//    public static int sumEven(List<Integer> li) {
//        int sum = 0;
//        for (Integer i: li)
//            if (i % 2 == 0)
//                sum += i;
//        return sum;
//    }

    public static int sumEven(List<Integer> li) {
        int sum = 0;
        for (Integer i : li)
            if (i.intValue() % 2 == 0)
                sum += i.intValue();
        return sum;
    }
}
