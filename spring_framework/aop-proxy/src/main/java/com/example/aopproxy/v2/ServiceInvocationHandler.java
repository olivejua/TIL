package com.example.aopproxy.v2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceInvocationHandler implements InvocationHandler {
    private final Object target;

    public ServiceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before ServiceInvocationHandler.invoke");

        Object result = method.invoke(target, args);

        System.out.println("After ServiceInvocationHandler.invoke");

        return result;
    }
}
