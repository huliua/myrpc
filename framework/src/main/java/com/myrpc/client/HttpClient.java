package com.myrpc.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.myrpc.domain.Invocation;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.loadbalance.LoadBalance;
import com.myrpc.loadbalance.impl.RandomLoadBalance;
import com.myrpc.register.ServiceRegister;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 提供给服务调用端使用
 *
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 19:41
 */
public class HttpClient {

    /**
     * 客户端--服务列表缓存
     */
    private static final Map<String, List<ServiceMetaInfo>> serviceCacheMap = new HashMap<>();

    private final LoadBalance loadBalance;

    public HttpClient() {
        loadBalance = new RandomLoadBalance();
    }

    public Object send(Invocation invocation) {
        try {
            // 优先从本地缓存中获取服务
            List<ServiceMetaInfo> serviceList = serviceCacheMap.get(invocation.getServiceName());
            if (CollUtil.isEmpty(serviceList)) {
                // 本地缓存没有，则从注册中心获取
                serviceList = ServiceRegister.getService(invocation.getServiceName());
            }

            ServiceMetaInfo service = loadBalance.loadBalance(serviceList);
            if (null == service) {
                throw new RuntimeException("service not found");
            }

            URL url = new URL("http", service.getHost(), service.getPort(), "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            // 返回响应
            return JSON.parseObject(IOUtils.toString(inputStream), invocation.getReturnType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
