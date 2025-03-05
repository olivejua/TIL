package com.example.aopproxy.v2;

import com.example.aopproxy.pojo.Pojo;
import com.example.aopproxy.pojo.SimplePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

@Service
public class V2Service {

    // JDK Dynamic Proxy
    public void methodA() {
        System.out.println("V2Service.methodA");

        Pojo pojo = new SimplePojo();

        Pojo proxyInstance = (Pojo) Proxy.newProxyInstance(
                Pojo.class.getClassLoader(),
                new Class[]{Pojo.class},
                new ServiceInvocationHandler(pojo));

        proxyInstance.foo();
    }

    // CGLIB
    public void methodB() {
        SimplePojo simplePojo = new SimplePojo();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SimplePojo.class);
        enhancer.setCallback(new CglibProxyHandler(simplePojo));

        SimplePojo proxyInstance = (SimplePojo) enhancer.create();
        proxyInstance.foo();
    }

    public static void main(String[] args) {
        V2Service service = new V2Service();

        service.methodA();
        service.methodB();
    }
}
