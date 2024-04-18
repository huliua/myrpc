package com.myrpc.handler;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.alibaba.fastjson2.JSON;
import com.myrpc.domain.Invocation;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            Class<?> serviceClass = ClassUtil.getClassLoader().loadClass(invocation.getClassName());
            Object serviceImpl = ServiceLoaderUtil.loadFirstAvailable(serviceClass);

            // 服务调用
            Method method = serviceClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
            Object result = method.invoke(serviceImpl, invocation.getArgs());

            // 写入响应
            IOUtils.write(JSON.toJSONString(result), res.getOutputStream());
        } catch (FileNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
