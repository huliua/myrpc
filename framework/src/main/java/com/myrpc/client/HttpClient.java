package com.myrpc.client;

import com.alibaba.fastjson2.JSON;
import com.myrpc.domain.Invocation;
import com.myrpc.domain.ServiceMetaInfo;
import com.myrpc.loadbalance.LoadBalance;
import com.myrpc.register.ServiceRegister;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 提供给服务调用端使用
 *
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 19:41
 */
public class HttpClient {

    public Object send(Invocation invocation) {
        try {
            List<ServiceMetaInfo> serviceList = ServiceRegister.getService(invocation.getServiceName());
            ServiceMetaInfo service = LoadBalance.random(serviceList);
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
