package com.example.aopproxy.v2;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyHandler implements MethodInterceptor {
    private final Object target;

    public CglibProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before CglibProxyHandler.intercept");

        Object result = proxy.invoke(target, args);

        System.out.println("After CglibProxyHandler.intercept");

        return result;
    }
}
