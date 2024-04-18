package com.myrpc.register;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.myrpc.domain.ServiceBean;
import com.myrpc.domain.ServiceMetaInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务注册中心
 * 1.服务注册
 * 2.服务发现
 * 3.服务下线的动态更新（未实现）
 *
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 15:27
 */
public class ServiceRegister {

    private static final String filePath = "/Users/liuhu/CodingFolder/JAVA/myrpc/register.txt";

    /**
     * 本地服务元信息列表
     */
    private static final Map<String, List<ServiceMetaInfo>> localServiceMetaInfoMap = new HashMap<>();

    /**
     * 本地服务列表
     */
    private static final Map<String, List<ServiceBean>> localServiceBeanMap = new HashMap<>();

    /**
     * 服务注册
     */
    public static void register(ServiceMetaInfo serviceMetaInfo, List<ServiceBean> serviceList) throws IOException {
        // 先实现本地注册
        List<ServiceMetaInfo> services = localServiceMetaInfoMap.get(serviceMetaInfo.getServiceName());
        if (CollectionUtil.isEmpty(services)) {
            services = new ArrayList<>();
        }
        services.add(serviceMetaInfo);
        localServiceMetaInfoMap.put(serviceMetaInfo.getServiceName(), services);

        // 保存该服务名下提供的服务列表
        localServiceBeanMap.put(serviceMetaInfo.getKey(), serviceList);

        // 远程服务注册(暂时存入本地文件中)
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(localServiceMetaInfoMap);
    }

    /**
     * 根据服务名获取服务信息
     *
     * @param serviceName 服务名
     * @return 返回注册中心的服务列表
     */
    @SuppressWarnings("unchecked")
    public static List<ServiceMetaInfo> getService(String serviceName) {
        // 优先从本地读取
        List<ServiceMetaInfo> serviceList = localServiceMetaInfoMap.get(serviceName);
        if (CollUtil.isNotEmpty(serviceList)) {
            return serviceList;
        }
        // 从本地文件中读取出服务列表
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            objectInputStream = new ObjectInputStream(fileInputStream);
            Map<String, List<ServiceMetaInfo>> resMap = (Map<String, List<ServiceMetaInfo>>) objectInputStream.readObject();
            return resMap.get(serviceName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
                assert objectInputStream != null;
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
