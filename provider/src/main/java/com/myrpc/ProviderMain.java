package com.myrpc;

import com.myrpc.apis.ProviderService;
import com.myrpc.domain.ServiceBean;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.register.ServiceRegister;
import com.myrpc.server.HttpServer;
import com.myrpc.service.impl.ProviderServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 15:26
 */
public class ProviderMain {
    public static void main(String[] args) {
        // 构建服务元信息
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setKey(UUID.randomUUID().toString());
        serviceMetaInfo.setServiceName("provider");
        serviceMetaInfo.setVersion("1.0");
        serviceMetaInfo.setHost("localhost");
        serviceMetaInfo.setPort(8080);

        // 构建提供的服务列表
        List<ServiceBean> beanList = new ArrayList<>();
        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setBeanName(ProviderService.class.getName());
        serviceBean.setBeanClass(ProviderServiceImpl.class);
        beanList.add(serviceBean);

        try {
            // 服务注册
            ServiceRegister.register(serviceMetaInfo, beanList);

            // 启动服务
            HttpServer httpServer = new HttpServer();
            httpServer.start(serviceMetaInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}