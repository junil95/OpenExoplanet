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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

import static UpdateTools.ReadCSV.EU;
import static UpdateTools.ReadCSV.getIndexMappings;
import static UpdateTools.ReadCSV.mapPlanetToData;
import static UpdateTools.ReadCSV.onlyAlphanumericList;


/**
 * Created on 12/10/2016
 *
 * @author John A class used to highlight the difference between the local/master/ and pulled
 *         databases from Nasa and Exoplanet.eu.
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
   */
  public static HashMap<String, List<String[]>> readCSVs() throws IOException {
    HashMap<String, List<String[]>> info = new HashMap<String, List<String[]>>();
    
    CSVReader reader = null;
    
    try {
      // Reading the local exoplanets
      reader = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
      List<String[]> exoplanetEntries = reader.readAll();
      info.put("exoplanetEntries", exoplanetEntries);
      
      // Reading the local nasa archives
      reader = new CSVReader(new FileReader(PullingTools.localNasaArchive));
      List nasaEntries = reader.readAll();
      info.put("nasaArchives", nasaEntries);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // Closes the Reader
      reader.close();
    }
    
    
    return info;
  }
  
  ;
  
  /**
   * Returns the string ignoring non alphanumeric characters.
   *
   * @param line The line for characters to be removed
   * @return A line with only alphanumeric characters
   */
  public static String onlyAlphaNumeric(String line) {
    // Regex stripping all non alphanumerics to blank
    line = line.replaceAll("[^A-Za-z0-9]", "");
    return line.toLowerCase();
  }
  
  /**
   * Return the names of all new planets not in the oce.
   *
   * @return A list of new names not in the oce.
   */
//	public static ArrayList<HashMap<String, String[]>> getNewPlanetIDs() throws IOException, SAXException, ParserConfigurationException{
//		ArrayList<HashMap<String, String[]>> newPlanets = new ArrayList<HashMap<String, String[]>>();
//		HashMap<String, List<String[]>> info = readCSVs();
//
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document doc = dBuilder.parse(PullingTools.localOecFile);
//
//		doc.getDocumentElement().normalize();
//
//		// Getting all known names in a set
//	    Set<String> total = new HashSet<String>();
//
//	    // Adding the default names
//	    NodeList nodeList = doc.getElementsByTagName("name");
//	    for (int i=0; i<nodeList.getLength(); i++) {
//	        // Get element
//	    	String name = nodeList.item(i).getTextContent();
//	    	total.add(onlyAlphaNumeric(name));
//	    }
//
//	    //Getting the index for our ids
//	    int exoplanetIdIndex = 0;
//	    int exoplanetAlternateIdIndex = 0;
//	    // Failsafe for nasa index is 270
//	    int nasaIdIndex = 270;
//	    for(int i = 0; i < info.get("exoplanetEntries").get(0).length; i++){
//	    	if(info.get("exoplanetEntries").get(0)[i].equals(exoplanetColumnID)){
//	    		exoplanetIdIndex = i;
//	    	}
//	    	// Getting alternate name index
//	    	else if(info.get("exoplanetEntries").get(0)[i].equals(exoplanetAlternateColumnID)){
//	    		exoplanetAlternateIdIndex = i;
//	    	}
//	    }
//	    for(int i = 0; i < info.get("nasaArchives").get(0).length; i++){
//	    	if(info.get("nasaArchives").get(0)[i].equals(nasaColumnID)){
//	    		nasaIdIndex = i;
//	    		break;
//	    	}
//	    }
//
//	    // Temp for stroing current
//	    HashMap<String, String[]> newPlanetsTempExoplanet = new HashMap<String, String[]>();
//	    // Getting names of our unknown/new planets
//	    for(int i = 0; i < info.get("exoplanetEntries").size(); i++){
//	    	if(total.contains(info.get("exoplanetEntries").get(i)[exoplanetIdIndex]) == false && total.contains(info.get("exoplanetEntries").get(i)[exoplanetAlternateIdIndex]) == false){
//	    		newPlanetsTempExoplanet.put(onlyAlphaNumeric((info.get("exoplanetEntries").get(i)[exoplanetIdIndex])), info.get("exoplanetEntries").get(i));
//	    	}
//	    }
//
//	    // Temp for stroing current
//	    HashMap<String, String[]> newPlanetsTempNasa = new HashMap<String, String[]>();
//	    // Getting names of our unknown/new planets
//	    for(int i = 0; i < info.get("nasaArchives").size(); i++){
//	    	if(total.contains(info.get("nasaArchives").get(i)[nasaIdIndex]) == false){
//	    		newPlanetsTempNasa.put(onlyAlphaNumeric((info.get("nasaArchives").get(i)[nasaIdIndex])), info.get("nasaArchives").get(i));
//	    	}
//	    }
//
//	    // Dealing with the altnerate name column in exoplanet eu
//
//	    newPlanets.add(newPlanetsTempExoplanet);
//	    newPlanets.add(newPlanetsTempNasa);
//	    return newPlanets;
//	}
  public static UpdateStorage getNewPlanetIDs(String databasePath, String database, UpdateStorage us) throws IOException {
    HashMap<String, HashMap<String, String>> planetDataOther = mapPlanetToData(databasePath, database);
    //will get only alphanumeric planet names
    Set<String> oecPlanetNames = getPlanetNamesOEC();
    String[] tempList;
    ArrayList<String> tempSet;
    Systems sys;
    boolean isNew;
    for (String planet : planetDataOther.keySet()) {
      // special case for EU which contains alternate planet names
      isNew = true;
      tempSet = new ArrayList<>();
      // add the planet names to the set. Should always check the most common planet name first
      tempSet.add(onlyAlphaNumeric(planet));
      if (database.equals(EU)) {
        //find the names in the alternate names column
        tempList = planetDataOther.get(planet).get("pl_alternatenames").split(",");
        //add them to set if not empty string
        for (String s : tempList) {
          s = onlyAlphaNumeric(s);
          if (!s.equals("")) {
            tempSet.add(s);
          }
        }
      }
      
      //Now go through set of names and check if they are in the oec name
      for (String name : tempSet) {
        if (oecPlanetNames.contains(name)) {
          isNew = false;
          break;
        }
      }
      
      //found new planet, create system object and add it to updates class
      try {
        if (isNew) {
          sys = SystemBuilder.buildSystemWithHashMap(planetDataOther.get(planet), database);
          us.updates.add(sys);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
    return us;
  }
  
  public static Set<String> getPlanetNamesOEC() {
    //Get a list of planet names
    Set<String> planetNames = new HashSet<>();
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(PullingTools.localOecFile);
      doc.getDocumentElement().normalize();
      NodeList planetNl = doc.getElementsByTagName("planet");
      NodeList nameNl;
      Element element;
      for (int i = 0; i < planetNl.getLength(); i++) {
        element = (Element) planetNl.item(i);
        nameNl = element.getElementsByTagName("name");
        for (int j = 0; j < nameNl.getLength(); j++) {
          planetNames.add(onlyAlphaNumeric(nameNl.item(j).getTextContent()));
        }
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return planetNames;
  }
  
  public static void main(String[] args) {
    try {
      ReadCSV.mapIndexes();
      UpdateStorage us = new UpdateStorage();
      us = getNewPlanetIDs(PullingTools.localExoplanetEu, EU, us);
      for (Systems s : us.updates) {
        System.out.println(s.getChild().getChild().getName());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
  }
}
