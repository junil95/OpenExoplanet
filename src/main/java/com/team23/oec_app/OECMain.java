package com.team23.oec_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class OECMain extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().print("Hello from Java!\n");
        
        System.out.println("Getting a Request");
        
        resp.getWriter().close();
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));

    	WebAppContext context = new WebAppContext();
        context.setDescriptor(context+"/WEB-INF/web.xml");
        context.setResourceBase("../webapp");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        
        context.addServlet(new ServletHolder(new OECMain()),"/*");
        server.start();
        server.join();   
    }
}
 