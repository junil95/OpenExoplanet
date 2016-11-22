package com.team23.oec_app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class OECMain 
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(server, "/app");
        handler.addServlet(OECServlet.class, "/");
        server.start();
    }
}
 