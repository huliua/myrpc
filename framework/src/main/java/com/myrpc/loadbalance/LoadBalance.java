package com.myrpc.loadbalance;

import com.myrpc.domain.ServiceMetaInfo;

import java.util.List;

/**
 * 负载均衡接口
 * @author huliua
 * @version 1.0
 * @date 2024-04-16 20:21
 */
public interface LoadBalance {

   ServiceMetaInfo loadBalance(List<ServiceMetaInfo> serviceMetaInfoList);
}
