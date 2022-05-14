package me.olivejua.learningjava.primitive;

public class Test {

    public static void main(String[] args) {
        byte b = 1;
        System.out.println("b = " + b);
        byte b2 = 'a';
        System.out.println("b2 = " + b2);
        byte b3 = 'z';
        System.out.println("b3 = " + b3);
        byte b4 = 'A';
        System.out.println("b4 = " + b4);

        short s1 = 1234;
        System.out.println("s1 = " + s1);
        short s2 = 'ㄱ';
        System.out.println("s2 = " + s2);

        char c = '\u0000';
        System.out.println("c = " + c);
    }
}

class Aclass {
    char c;
    byte b;

    public char getC() {
        return c;
    }

    public byte getB() {
        return b;
    }
}