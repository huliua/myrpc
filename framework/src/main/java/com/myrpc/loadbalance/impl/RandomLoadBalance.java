package com.myrpc.loadbalance.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.loadbalance.LoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡
 *
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 16:25
 */
public class RandomLoadBalance implements LoadBalance {

    public ServiceMetaInfo loadBalance(List<ServiceMetaInfo> serviceList) {
        if (CollectionUtil.isEmpty(serviceList)) {
            return null;
        }
        ThreadLocalRandom random = RandomUtil.getRandom();
        int index = random.nextInt(serviceList.size());
        return serviceList.get(index);
    }
}
