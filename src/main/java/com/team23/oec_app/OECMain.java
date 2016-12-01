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
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.team23.Driver;
import com.team23.UpdateTools.SendPullRequest;

public class OECMain extends HttpServlet
{
	
	private boolean updating = false;
	private int counter = 0;

	/**
	 * Default method used to handle all GET requests given to the server.
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	if(req.getRequestURI().equals("/")){
    		resp.setContentType("text/html");
    		resp.getWriter().print(readFile("index.html", StandardCharsets.UTF_8));
        	resp.getWriter().close();

    	}
    	else if(req.getRequestURI().contains(".css")){
    		resp.setContentType("text/css");

    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
        	resp.getWriter().close();
    	}
    	else if(req.getRequestURI().contains(".js")){
    		resp.setContentType("text/javascript");
    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
    		resp.getWriter().close();
    	}
    	else if (req.getRequestURI().equals("/update")){
    		updating = false;
    		counter = 1;
        	if(!Driver.isInitialMergeDone()){
        		Driver.initialSetupOrResetLocalCopies();
        	}
        	updating = true;
        	System.out.println("Flag is raised to true");
    	}
    	else if (req.getRequestURI().equals("/request")){
    		if(counter > 101){
     			resp.getWriter().print("Took too long..");
     			resp.getWriter().flush();
     			System.out.println("Took too long");
     		}
    		else if(updating == false){
    			counter += 1;
    			resp.getWriter().print("Still updating...");
    			resp.getWriter().flush();
    			System.out.println("Still updating");
    		}
    		else {
    			Driver.detectInitialUpdates();
    			System.out.println("Finished Updating");
    			resp.getWriter().print("[");
        		resp.getWriter().print(Driver.getNewSystemConflicts());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getNewStarConflicts());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getNewPlanetConflicts());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getNewPlanets());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getNewStars());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getNewSystems());
        		resp.getWriter().print(",");
        		resp.getWriter().print(Driver.getSystemAttributeUpdates());
                resp.getWriter().print(",");
                resp.getWriter().print(Driver.getStarAttributeUpdates());
                resp.getWriter().print(",");
                resp.getWriter().print(Driver.getPlanetAttributeUpdates());
                resp.getWriter().print(",");
                resp.getWriter().print(Driver.getSystemAttributeConflicts());
                resp.getWriter().print(",");
                resp.getWriter().print(Driver.getStarAttributeConflicts());
                resp.getWriter().print(",");
                resp.getWriter().print(Driver.getPlanetAttributeUpdates());
        		resp.getWriter().print("]");
        		resp.getWriter().flush();
    		}
    		resp.getWriter().close();
    		
    		// Calling from Driver to get all the new updated celestial objects
        	// Doing the initial merge and setting up local repos

    		/*
    		ArrayList<String> list = new ArrayList<String>();
    		
    		list.add(Driver.getNewSystems());
    		list.add(Driver.getNewPlanetConflicts());
    		
    		list.add(Driver.getNewPlanets());
    		list.add(Driver.getNewPlanetConflicts());
    		
    		list.add(Driver.getNewStars());
    		list.add(Driver.getNewStarConflicts());
    		
    		*/
    		//System.out.println(Driver.getNewPlanetConflicts());	
    	}
    }
    
    /**
     * Default method used to handle all POST requests given to the server.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	if (req.getRequestURI().equals("/upload")){		
    		// The data from the key
    		String data = req.getParameter("result");
		String name = Driver.distributeData(data);
    		resp.getWriter().close();
		System.out.println(name);
    		System.out.println(data);
    	}
    	else if (req.getRequestURI().equals("/setkey")){
    		// The key from the github upload
    		String key = req.getParameter("key");
		resp.getWriter().print(key);
    		System.out.println(key);
    	}
    	else{
    		super.doPost(req, resp);
    	}
    }
    
    /**
     * Helper function used to convert all contents of a file to a single string.
     * @param path Path of the file
     * @param encoding The encoding format of the file
     * @return A String with all contents of the file
     * @throws IOException If the file doesn't exist
     */
    public static String readFile(String path, Charset encoding) 
    		  throws IOException 
    		{
    		  byte[] encoded = Files.readAllBytes(Paths.get(path));
    		  return new String(encoded, encoding);
    		}

    public static void main(String[] args) throws Exception{    	
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        
        ServerConnector http = new ServerConnector(server);
        http.setPort(8080);
        http.setIdleTimeout(3000000);
        server.addConnector(http);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.getSessionHandler().getSessionManager().setMaxInactiveInterval(30000);
        server.setHandler(context);
        context.addServlet(new ServletHolder(new OECMain()),"/*");
        
        server.start();
        server.join(); 
    }
}
 
