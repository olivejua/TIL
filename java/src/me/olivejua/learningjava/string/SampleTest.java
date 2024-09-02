package me.olivejua.learningjava.string;

public class SampleTest {
    public static void main(String[] args) {
        SampleTest sampleTest = new SampleTest();
//        sampleTest.internCheck();

//        sampleTest.appendStringBuilderToStringBuilder();
        sampleTest.appendStringBuilderToStringBuffer();
    }

    public void internCheck() {
        String text1 = "Java Basic";
        String text2 = "Java Basic";
        String text3 = new String("Java Basic");
        text3 = text3.intern();

        System.out.println(text1 == text2); //true
        System.out.println(text1 == text3); //false
        System.out.println(text1.equals(text3)); //true
    }

    public void appendStringBuilderToStringBuilder() {
        StringBuilder sb1 = new StringBuilder("abc");

        StringBuilder sb2 = new StringBuilder("def");

        sb1.append(sb2);

        System.out.println(sb1);
    }

    public void appendStringBuilderToStringBuffer() {
        StringBuffer sf1 = new StringBuffer("abc");

        StringBuilder sb2 = new StringBuilder("def");

        sf1.append(sb2);

        System.out.println(sf1);
    }
}
