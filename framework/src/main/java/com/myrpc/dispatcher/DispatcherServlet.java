package com.myrpc.dispatcher;

import com.myrpc.handler.HttpServerHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:34
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("有任务啦！");
        new HttpServerHandler().handler(req, res);
    }
}
