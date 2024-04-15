package com.myrpc.server;

import com.myrpc.dispatcher.DispatcherServlet;
import com.myrpc.domain.ServiceMetaInfo;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:24
 */
public class HttpServer {

    public void start(ServiceMetaInfo service) {
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        org.apache.catalina.Service tomcatService = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(service.getPort());

        Engine engine = new StandardEngine();
        engine.setDefaultHost(service.getHost());

        Host host = new StandardHost();
        host.setName(service.getHost());

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        tomcatService.setContainer(engine);
        tomcatService.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }
}
