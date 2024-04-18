package com.myrpc.dispatcher;

import com.myrpc.handler.HttpServerHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:34
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        log.info("有新的请求待处理...");
        new HttpServerHandler().handler(req, res);
    }
}
