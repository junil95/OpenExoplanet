import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;

import net.lingala.zip4j.exception.ZipException;



/**
 * Created on 12/10/2016
 * @author John
 * A class used to highlight the difference between the local/master/ and pulled databases from Nasa and Exoplanet.eu.
 */
public class DifferenceDetector {
	
  /**
   * Url for getting oec as seperate files per system
   */
public static final String localOecFiles = "./Data/oec/oec.xml";
  
  /**
   * Url for getting the exoplanet Eu catalogue
   */
public static final String LocalExoplanetEu = "./Data/exoplanetEu/exoplanetEu.csv";
  
  /**
   * Url for getting the nasa Archive catalogue
   */
public static final String LocalNasaArchive = "./Data/nasaArchive/nasaArchive.csv";
	  

	/**
	 * Updates the local copy to the latest updated master copy from Nasa and Exoplanet.eu.
	 * @throws IOException 
	 */
	public static HashMap<String, List<String[]>> readCSVs() throws IOException{
		HashMap<String, List<String[]>> info = new HashMap<String, List<String[]>>();
		
		CSVReader reader = null;
		
		try{
			// Reading the local exoplanets
			reader = new CSVReader(new FileReader(LocalExoplanetEu));
		    List<String[]> exoplanetEntries = reader.readAll();
		    info.put("exoplanetEntries", exoplanetEntries);
		    
		    // Reading the local nasa archives
		    reader = new CSVReader(new FileReader(LocalNasaArchive));
		    List nasaEntries = reader.readAll();
		    info.put("nasaArchives", nasaEntries);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
		    // Closes the Reader
		    reader.close();
		}

		
		return info;
	};
	
	/**
	 * Return the names of all new planets not in the oce.
	 * @return A list of new names not in the oce.
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static ArrayList<String> getNewPlanetIDs() throws IOException, SAXException, ParserConfigurationException{
		ArrayList<String> newPlanets = new ArrayList<String>();
		HashMap<String, List<String[]>> info = readCSVs();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(localOecFiles);
		
		doc.getDocumentElement().normalize();
	
		// Getting all known names in a set
	    Set<String> total = new HashSet<String>();
	    
	    NodeList nodeList = doc.getElementsByTagName("name");
	    for (int i=0; i<nodeList.getLength(); i++) {
	        // Get element
	    	String name = nodeList.item(i).getTextContent();
	    	total.add(name);
	    }
	    
	    // Getting names of our unknown/new planets
	    for(int i = 0; i < info.get("exoplanetEntries").size(); i++){    	
	    	if(total.contains(info.get("exoplanetEntries").get(i)[0]) == false){
	    		newPlanets.add(info.get("exoplanetEntries").get(i)[0]);
	    	}
	    }
	    
	    // Getting names of our unknown/new planets
	    for(int i = 0; i < info.get("nasaArchives").size(); i++){
	    	if(total.contains(info.get("nasaArchives").get(i)[270]) == false){
	    		newPlanets.add(info.get("nasaArchives").get(i)[270]);
	    	}
	    }
	    
	    for(int i = 0; i < newPlanets.size(); i++){
	    	System.out.println(newPlanets.get(i));
	    }
	    
	    return newPlanets;
	}
	
	public static void main(String[] args) {
	    try {
	    	getNewPlanetIDs();
	    	
	      } catch (IOException | SAXException | ParserConfigurationException e) {
	        e.printStackTrace();
	      }
	}
}
