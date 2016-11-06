package UpdateTools;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;


/**
 * Created on 12/10/2016
 * @author John
 * A class used to highlight the difference between the local/master/ and pulled databases from Nasa and Exoplanet.eu.
 */
public class DifferenceDetector {
	/**
	 * The index for the 
	 */
	public static final String nasaColumnID = "pl_name";
	  
	public static final String exoplanetColumnID = "# name";
	
	public static final String exoplanetAlternateColumnID = "	alternate_names";

	
	/**
	 * Updates the local copy to the latest updated master copy from Nasa and Exoplanet.eu.
	 * @throws IOException 
	 */
	public static HashMap<String, List<String[]>> readCSVs() throws IOException{
		HashMap<String, List<String[]>> info = new HashMap<String, List<String[]>>();
		
		CSVReader reader = null;
		
		try{
			// Reading the local exoplanets
			reader = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
		    List<String[]> exoplanetEntries = reader.readAll();
		    info.put("exoplanetEntries", exoplanetEntries);
		    
		    // Reading the local nasa archives
		    reader = new CSVReader(new FileReader(PullingTools.localNasaArchive));
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
	 * Returns the string ignoring non alphanumeric characters.
	 * @param line The line for characters to be removed
	 * @return A line with only alphanumeric characters
	 */
	public static String onlyAlphaNumeric(String line){
		// Regex stripping all non alphanumerics to blank
		line = line.replaceAll("[^A-Za-z0-9]", "");
		return line.toLowerCase();
	}
	
	/**
	 * Return the names of all new planets not in the oce.
	 * @return A list of new names not in the oce.
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static ArrayList<HashMap<String, String[]>> getNewPlanetIDs() throws IOException, SAXException, ParserConfigurationException{
		ArrayList<HashMap<String, String[]>> newPlanets = new ArrayList<HashMap<String, String[]>>();
		HashMap<String, List<String[]>> info = readCSVs();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(PullingTools.localOecFile);
		
		doc.getDocumentElement().normalize();
	
		// Getting all known names in a set
	    Set<String> total = new HashSet<String>();
	    
	    // Adding the default names
	    NodeList nodeList = doc.getElementsByTagName("name");
	    for (int i=0; i<nodeList.getLength(); i++) {
	        // Get element
	    	String name = nodeList.item(i).getTextContent();
	    	total.add(onlyAlphaNumeric(name));
	    }
	    
	    //Getting the index for our ids
	    int exoplanetIdIndex = 0;
	    int exoplanetAlternateIdIndex = 0;
	    // Failsafe for nasa index is 270
	    int nasaIdIndex = 270;
	    for(int i = 0; i < info.get("exoplanetEntries").get(0).length; i++){
	    	if(info.get("exoplanetEntries").get(0)[i].equals(exoplanetColumnID)){
	    		exoplanetIdIndex = i;
	    	}
	    	// Getting alternate name index
	    	else if(info.get("exoplanetEntries").get(0)[i].equals(exoplanetAlternateColumnID)){
	    		exoplanetAlternateIdIndex = i;
	    	}
	    }
	    for(int i = 0; i < info.get("nasaArchives").get(0).length; i++){
	    	if(info.get("nasaArchives").get(0)[i].equals(nasaColumnID)){
	    		nasaIdIndex = i;
	    		break;
	    	}
	    }
	    
	    // Temp for stroing current
	    HashMap<String, String[]> newPlanetsTempExoplanet = new HashMap<String, String[]>();
	    // Getting names of our unknown/new planets
	    for(int i = 0; i < info.get("exoplanetEntries").size(); i++){    	
	    	if(total.contains(info.get("exoplanetEntries").get(i)[exoplanetIdIndex]) == false && total.contains(info.get("exoplanetEntries").get(i)[exoplanetAlternateIdIndex]) == false){
	    		newPlanetsTempExoplanet.put(onlyAlphaNumeric((info.get("exoplanetEntries").get(i)[exoplanetIdIndex])), info.get("exoplanetEntries").get(i));
	    	}
	    }
	    
	    // Temp for stroing current
	    HashMap<String, String[]> newPlanetsTempNasa = new HashMap<String, String[]>();
	    // Getting names of our unknown/new planets
	    for(int i = 0; i < info.get("nasaArchives").size(); i++){
	    	if(total.contains(info.get("nasaArchives").get(i)[nasaIdIndex]) == false){
	    		newPlanetsTempNasa.put(onlyAlphaNumeric((info.get("nasaArchives").get(i)[nasaIdIndex])), info.get("nasaArchives").get(i));
	    	}
	    }
	    
	    // Dealing with the altnerate name column in exoplanet eu
	    
	    newPlanets.add(newPlanetsTempExoplanet);
	    newPlanets.add(newPlanetsTempNasa);
	    return newPlanets;
	}
	
	public static void main(String[] args) {
	    try {
			System.out.println(getNewPlanetIDs());
	    	
	      } catch (IOException | SAXException | ParserConfigurationException e) {
	        e.printStackTrace();
	      }
	}
}
