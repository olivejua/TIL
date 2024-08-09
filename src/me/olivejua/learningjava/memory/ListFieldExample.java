package me.olivejua.learningjava.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * main 함수 실행
 * 1. main 메서드의 Stack Frame 생성
 * 2. 인자 args, 지역변수 elements, example 을 Stack Frame에 추가
 * 3. Arrays.asList()를 실행
 * 4. new ArrayList의 객체 생성, 내부 필드로
 */
public class ListFieldExample {
    private final List<String> list;

    public ListFieldExample(List<String> elements) {
        this.list = elements;
    }

    public static void main(String[] args) {
//        List<String> elements = Arrays.asList("A", "B", "C", "D", "E", "F");
//        new ArrayList<>();
//
//        ListFieldExample example = new ListFieldExample(elements);

        // 현재 사이즈 + (현재사이즈 >> 1)

        HashMap<Integer, String> map = new HashMap<>();

        System.out.println(1 >> 1);
    }
}
