package me.olivejua.learningjava.memory;

import javax.crypto.Cipher;

public class A {
    public static String field1 = new B().execute();

    public static void main(String[] args) {
        String bootClassPath = System.getProperty("sun.boot.class.path");
        System.out.println("Bootstrap Class Path: " + bootClassPath);
        String extDirs = System.getProperty("java.ext.dirs");
        System.out.println("Extension Class Path: " + extDirs);

    }
}

class B {

    String execute() {
        return "Hello";
    }
}
