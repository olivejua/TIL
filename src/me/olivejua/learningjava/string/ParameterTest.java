package me.olivejua.learningjava.string;

public class ParameterTest {

    public static void main(String[] args) {
        ParameterTest test = new ParameterTest();
        test.addString("DEF");
        test.appendString("HIJ");
    }

//    void saveParameter(String param) {
//        String var = "varABC";
//    }

    void addString(String param) {
        String var = "ABC";
        var = var + param;
    }

    void appendString(String param) {
        StringBuilder sb = new StringBuilder("EFG");
        sb.append(param);
    }
}
