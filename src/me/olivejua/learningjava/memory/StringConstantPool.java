package me.olivejua.learningjava.memory;

public class StringConstantPool {

    public static void main(String[] args) {
        Integer int1 = new Integer(100);
        Integer int2 = Integer.valueOf(100);
        Integer int3 = Integer.valueOf(100);
        int int4 = 100; // Integer.valueOf(100) 자동박싱

        System.out.println(int1 == int2);
        System.out.println(int2 == int3);
        System.out.println(int3 == int4);

        Integer int5 = Integer.valueOf(200);
        Integer int6 = Integer.valueOf(200);

        System.out.println(int5 == int6);


//        String str1 = "Hello, World!";
//        String str2 = "Hello, World!";
//        String str3 = new String("Hello, World!");
//
//        System.out.println("str1 == str2: " + (str1 == str2)); // true
//        System.out.println("str1 == str3: " + (str1 == str3)); // false
//        System.out.println("str1.equals(str3): " + str1.equals(str3)); // true
    }
}
