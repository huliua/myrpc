package com.myrpc.proxy;

import com.myrpc.client.HttpClient;
import com.myrpc.domain.Invocation;

import java.lang.reflect.Proxy;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 21:59
 */
public class ProxyFactory {

    public static <T> T getProxy(String serviceName, Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            Invocation invocation = new Invocation();
            invocation.setServiceName(serviceName);
            invocation.setClassName(interfaceClass.getName());
            invocation.setMethodName(method.getName());
            invocation.setParamTypes(method.getParameterTypes());
            invocation.setArgs(args);
            invocation.setReturnType(method.getReturnType());

            return new HttpClient().send(invocation);
        });
        return (T) proxyInstance;
    }
}
