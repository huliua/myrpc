package com.myrpc.proxy;

import com.myrpc.client.HttpClient;
import com.myrpc.domain.Invocation;
import com.myrpc.domain.RpcResponse;
import com.myrpc.retry.Retryer;

import java.lang.reflect.Proxy;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 21:59
 */
public class ProxyFactory {

    public static <T> T getProxy(String serviceName, Class<?> interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            // 构建方法调用信息
            Invocation invocation = new Invocation();
            invocation.setServiceName(serviceName);
            invocation.setClassName(interfaceClass.getName());
            invocation.setMethodName(method.getName());
            invocation.setParamTypes(method.getParameterTypes());
            invocation.setArgs(args);
            invocation.setReturnType(method.getReturnType());

            HttpClient httpClient = new HttpClient();
            // 服务重试
            RpcResponse response = Retryer.doRetry(() -> httpClient.send(invocation));
            if (response.getData() != null) {
                return response.getData();
            } else {
                // TODO: 重试多次后，服务降级
                throw new RuntimeException(response.getException());
            }
        });
        return (T) proxyInstance;
    }
}
