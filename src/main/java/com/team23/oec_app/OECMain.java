package com.team23.oec_app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.Gson;
import com.team23.Driver;
import com.team23.ModelStarSystems.Systems;
import com.team23.UpdateTools.Merge;
import com.team23.UpdateTools.UpdateStorage;
import com.team23.UpdateTools.generateXML;

public class OECMain extends HttpServlet
{

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
        	
        	// Doing the initial merge and setting up local repos
        	if(!Driver.isInitialMergeDone()){
        		Driver.initialSetupOrResetLocalCopies();
        	}	
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
    	else if(req.getRequestURI().contains(".woff")){
    		resp.getWriter().print(readFile(req.getRequestURI().substring(1), StandardCharsets.UTF_8));
    		resp.getWriter().close();
    	}
    	else if (req.getRequestURI().equals("/update")){
    		// Calling from Driver to get all the new updated celestial objects
        	if(!Driver.isInitialMergeDone()){
        		Driver.initialSetupOrResetLocalCopies();
        	}
        	
        	// Feteching initial updates
    		Driver.detectInitialUpdates();
    		
    		ArrayList<String> list = new ArrayList<String>();
    		
    		list.add(Driver.getNewSystems());
    		list.add(Driver.getNewPlanetConflicts());
    		
    		list.add(Driver.getNewPlanets());
    		list.add(Driver.getNewPlanetConflicts());
    		
    		list.add(Driver.getNewStars());
    		list.add(Driver.getNewStarConflicts());
    		
    		// Writing back data
    		Gson gson = new Gson();	
    		
    		//System.out.println(Driver.getNewSystems() + "HI");	
    		resp.getWriter().print(Driver.getNewSystemConflicts());
    		resp.getWriter().close();
    	}
    }
    
    /**
     * Default method used to handle all POST requests given to the server.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	if (req.getRequestURI().equals("/upload")){
    		resp.getWriter().close();
    	}
    	else if (req.getRequestURI().equals("/upload")){
        	Driver.executeMerge();
    		resp.getWriter().close();
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
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new OECMain()),"/*");
        server.start();
        server.join(); 
    }
}
 