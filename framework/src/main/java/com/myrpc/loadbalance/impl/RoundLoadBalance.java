package com.myrpc.loadbalance.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.loadbalance.LoadBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮询负载均衡
 * @author huliua
 * @version 1.0
 * @date 2024-04-16 20:27
 */
public class RoundLoadBalance implements LoadBalance {

    /**
     * 负载均衡缓存
     * key:服务名称
     * value:服务调用次数
     */
    private static final Map<String, Integer> loadBalanceCacheMap = new HashMap<>();

    /**
     * 服务调用次数的最大值，当调用次数超过这个值，会重新从0开始
     */
    private static final Integer MAX = Integer.MAX_VALUE;

    @Override
    public ServiceMetaInfo loadBalance(List<ServiceMetaInfo> serviceMetaInfoList) {
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            return null;
        }

        // 获取服务名称
        String serviceName = serviceMetaInfoList.get(0).getServiceName();
        // 获取服务调用次数
        int count = 0;
        if (loadBalanceCacheMap.containsKey(serviceName)) {
            count = MapUtil.getInt(loadBalanceCacheMap, serviceName);
        }

        // 服务提供者列表的长度取余，得到最终的服务提供者索引
        int serviceIndex = count % serviceMetaInfoList.size();

        // 调用次数+1,如果超过MAX则重新从0开始
        count = ++count % MAX;
        // 更新缓存
        loadBalanceCacheMap.put(serviceName, count);

        // 返回服务提供者
        return serviceMetaInfoList.get(serviceIndex);
    }
}
