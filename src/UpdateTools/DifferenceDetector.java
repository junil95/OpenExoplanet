package UpdateTools;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import static UpdateTools.DifferenceDetector.getNewPlanetIDs;
import static UpdateTools.ReadCSV.EU;
import static UpdateTools.ReadCSV.NASA;
import static UpdateTools.ReadCSV.getIndexMappings;
import static UpdateTools.ReadCSV.mapPlanetToData;
import static UpdateTools.ReadCSV.onlyAlphanumericList;


/**
 * Created on 12/10/2016
 *
 * @author John
 *         A class used to highlight the difference between the local/master/ and pulled
 *         databases from Nasa and Exoplanet.eu.
 */
public class DifferenceDetector {
  
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
   * Finds planets from provided database that are not in OEC. This will add the new updates to
   * the updates list in the UpdateStorage
   * @param databasePath
   * @param database
   * @throws IOException
   */
  public static void getNewPlanetIDs(String databasePath, String database) throws IOException {
    HashMap<String, HashMap<String, String>> planetDataOther = mapPlanetToData(databasePath, database);
    //will get only alphanumeric planet names
    Set<String> oecPlanetNames = getNamesOEC();
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
          UpdateStorage.updates.add(sys);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  /**
   * Get a set of all system, star and planet names in oec
   *
   * @return Set of all possible names in oec, stripped of special characters
   */
  public static Set<String> getNamesOEC() {
    //Get a list of planet names
    Set<String> tagNames = new HashSet<>();
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(PullingTools.localOecFile);
      doc.getDocumentElement().normalize();
      NodeList nameNl = doc.getElementsByTagName("name");
      
      for (int j = 0; j < nameNl.getLength(); j++) {
        tagNames.add(onlyAlphaNumeric(nameNl.item(j).getTextContent()));
      }
    } catch (SAXException | IOException |
            ParserConfigurationException e) {
      e.printStackTrace();
    }
    return tagNames;
  }
  
  public static void main(String[] args) {
    try {
     ReadCSV.mapIndexes();
//      getNewPlanetIDs(PullingTools.localNasaArchive, NASA);
//      for (Systems s : UpdateStorage.updates) {
//        System.out.println(s.getChild().getChild().getName());
//      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
  //Create a system
    HashMap<String, String> build = new HashMap<>();
    build.put("pl_name", "hi");
    build.put("st_name", "alright");
    build.put("sy_name", "HD 107383");
    try {
      Systems s = UpdateClassifier.assignOecSyName(SystemBuilder.buildSystemWithHashMap(build, ReadCSV.EU));
      System.out.println(s.getName());
    } catch (SystemBuilder.MissingCelestialObjectNameException e) {
      e.printStackTrace();
    }
  
  
  }
}
