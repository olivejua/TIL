package com.example.aopproxy.pojo;

public class SimplePojo implements Pojo {

    public void foo() {
        this.bar();
    }

    public void bar() {
        System.out.println("SimplePojo.bar");
    }
}
