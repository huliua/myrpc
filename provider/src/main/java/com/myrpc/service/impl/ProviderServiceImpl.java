package com.myrpc.service.impl;

import com.myrpc.apis.ProviderService;
import com.myrpc.bo.ResponseResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 16:32
 */
public class ProviderServiceImpl implements ProviderService, Serializable {
    @Override
    public ResponseResult<List<Map<String, Object>>> say(String name) {
        System.out.println("com.myrpc.service.impl.ProviderServiceImpl.say(java.lang.String) executed");
        List<Map<String, Object>> list = new ArrayList<>();

        // 模拟业务逻辑
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = Map.of("name", name + i, "age", 18 + i);
            list.add(map);
        }
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> say() {
        List<Map<String, Object>> list = new ArrayList<>();

        // 模拟业务逻辑
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = Map.of("name", "null" + i, "age", 18 + i);
            list.add(map);
        }
        return ResponseResult.success(list);
    }
}
