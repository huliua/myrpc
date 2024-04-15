package com.myrpc;

import com.myrpc.apis.ProviderService;
import com.myrpc.bo.ResponseResult;
import com.myrpc.proxy.ProxyFactory;

import java.util.List;
import java.util.Map;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 15:26
 */
public class ConsumerMain {
    public static void main(String[] args) {
        ProviderService providerService = ProxyFactory.getProxy("provider", ProviderService.class);
        ResponseResult<List<Map<String, Object>>> result = providerService.say();
        System.out.println(result);
    }
}