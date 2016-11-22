package com.team23.oec_app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class OECMain extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	if(req.getRequestURI().equals("/")){
    		resp.setContentType("text/html");
    		resp.getWriter().print(readFile("index.html", StandardCharsets.UTF_8));
        	resp.getWriter().close();
    	}
    	else if(req.getRequestURI().contains("css")){
    		resp.setContentType("text/css");

    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
        	resp.getWriter().close();
    	}
    	else if(req.getRequestURI().contains("js")){
    		resp.setContentType("text/javascript");
    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
    		resp.getWriter().close();
    	}
    	else if(req.getRequestURI().contains("woff")){
    		resp.setContentType("application/font-woff");
    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
    		resp.getWriter().close();
    	}
    	else if(req.getRequestURI().contains("/")){
    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
    		resp.getWriter().close();
    	}
    	else {
    		resp.getWriter().print("Some random java text!");
    		resp.getWriter().close();
    	}
    }
    
    static String readFile(String path, Charset encoding) 
    		  throws IOException 
    		{
    		  byte[] encoded = Files.readAllBytes(Paths.get(path));
    		  return new String(encoded, encoding);
    		}

    public static void main(String[] args) throws Exception{    	
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new OECMain()),"/*");
        server.start();
        server.join(); 
    }
}
 