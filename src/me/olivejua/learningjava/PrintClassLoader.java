package me.olivejua.learningjava;

import me.olivejua.learningjava.cl.Logging;
import me.olivejua.learningjava.cl.OuterClassA;

import java.util.ArrayList;

public class PrintClassLoader {

    public static void main(String[] args) {

        System.out.println("Classloader of this class: " + PrintClassLoader.class.getClassLoader());
        System.out.println("Classloader of Logging: " + PrintClassLoader.class.getClassLoader().getParent());
        System.out.println("Classloader of ArrayList: " + ArrayList.class.getClassLoader());


        OuterClassA outerClass = new OuterClassA();
        OuterClassA.InnerClassA innerClassA = new OuterClassA.InnerClassA();

        System.out.println("logging = " + new Logging());
        System.out.println("OuterClass = " + outerClass);
        System.out.println("OuterClass.InnerClass = " + innerClassA);
    }
}
