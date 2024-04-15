package com.myrpc.handler;

import com.alibaba.fastjson2.JSON;
import com.myrpc.domain.Invocation;
import com.myrpc.domain.ServiceBean;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.loadbalance.LoadBalance;
import com.myrpc.register.ServiceRegister;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:35
 */
@SuppressWarnings("all")
public class HttpServerHandler {

    public void handler(ServletRequest req, ServletResponse res) {
        System.out.println("任务处理中。。。HttpServerHandler");
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(req.getInputStream());
            Invocation invocation = (Invocation) objectInputStream.readObject();

            // 根据服务名获取对应的服务列表
            List<ServiceMetaInfo> serviceList = ServiceRegister.getService(invocation.getServiceName());

            // 负载均衡
            ServiceMetaInfo service = LoadBalance.random(serviceList);

            // 根据调用信息，在服务中获取对应的bean信息
            List<ServiceBean> serviceBeans = ServiceRegister.getServiceBeanList(service.getKey());
            ServiceBean serviceBean = serviceBeans.stream().filter(bean -> bean.getBeanName().equals(invocation.getClassName())).findFirst().get();

            // 服务调用
            Method method = serviceBean.getBeanClass().getMethod(invocation.getMethodName(), invocation.getParamTypes());
            Object result = method.invoke(serviceBean.getBeanClass().newInstance(), invocation.getArgs());

            // 写入响应
            IOUtils.write(JSON.toJSONString(result), res.getOutputStream());
        } catch (FileNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
