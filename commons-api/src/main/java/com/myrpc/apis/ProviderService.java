package com.myrpc.apis;

import com.myrpc.bo.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 19:38
 */
public interface ProviderService {

    public ResponseResult<List<Map<String, Object>>> say(String name);

    public ResponseResult<List<Map<String, Object>>> say();
}
