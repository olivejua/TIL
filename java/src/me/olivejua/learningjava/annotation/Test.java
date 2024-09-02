package me.olivejua.learningjava.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * https://www.geeksforgeeks.org/class-getannotation-method-in-java-with-examples/
 */

//@Annotation(key = "GFG", value = "GeeksForGeeks")
public class Test {

    public static void main(String[] args)
            throws ClassNotFoundException
    {

        // returns the Class object for this class
        Class myClass = Test.class;

        System.out.println("Class represented by myClass: "
                + myClass.toString());

        // Get the annotation
        // using getAnnotation() method
        System.out.println(
                "Annotation of myClass: "
                        + myClass.getAnnotation(
                        Annotation.class));
        Annotation annotation = (Annotation) myClass.getAnnotation(Annotation.class);
        System.out.println("annotation.key() = " + annotation.key());
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Annotation {
    String key();
    String value();
}