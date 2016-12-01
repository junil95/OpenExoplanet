/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;

import ModelStarSystems.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static UpdateTools.DifferenceDetector.getNamesOEC;
import static UpdateTools.DifferenceDetector.onlyAlphaNumeric;
import static UpdateTools.UpdateStorage.planetUpdates;
import static UpdateTools.UpdateStorage.starUpdates;
import static UpdateTools.UpdateStorage.systemUpdates;


/**
 * @author junil
 */
public class UpdateClassifier {
  public static void classify() {
    ArrayList temp;
    for (Systems s : UpdateStorage.updates) {
      //get planet and star objects along with their names for each system
      Star star = (Star) s.getChild();
      Planet planet = (Planet) star.getChild();
      String check1;
      String check2;
      boolean added = false;
      if (!(SystemFinder.systemCheck(s))) {
        //need to add entire system
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.systems.add(temp);
        added = true;
      }
      if (!(added)) {
        try {
          check1 = SystemFinder.getSystem(star);
        } catch (SystemFinder.MissingCelestialObjectException e) {
          //if no name is retrieved from getSystem then we need to add star
          temp = new ArrayList();
          temp.add(s);
          UpdateStorage.stars.add(temp);
          added = true;
        }
      }
      if (!(added)) {
        try {
          check2 = SystemFinder.getSystem(planet);
        } catch (SystemFinder.MissingCelestialObjectException e1) {
          //if no name is retrieved from getSystem then we need to add star
          temp = new ArrayList();
          temp.add(s);
          UpdateStorage.planets.add(temp);
          //added = true;
        }
      }
      
      added = false;
    }
    //clear updates set so we know we already completed classifying these systems
    UpdateStorage.updates.clear();
  }
  
  //TODO, case where there are changes in attributes but, the system, star or planet doesn't even
  //exist in OEC. Also need to do assignOECsyName when junil works with system attributes
  /**
   * Classify the updates into new planets, stars or systems in the Update storage using the
   * updates list. Note this will also change the name of the system object to the primary
   * name found in OEC
   */
  public static void classifyUpdates() {
    ArrayList temp;
    Set<String> names = DifferenceDetector.getNamesOEC();
    for (Systems s : UpdateStorage.updates) {
      //If the system doesn't exist, then the update is a new system
      if (!names.contains(onlyAlphaNumeric(s.getName()))) {
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.systems.add(temp);
        //otherwise check if it is a new star
      } else if (!names.contains(onlyAlphaNumeric(s.getChild().getName()))){
        //need to assign primary system name found in OEC so we can find the individual xml file
        assignOecSyName(s, "system");
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.stars.add(temp);
        //otherwise check if it is a new planet. Need to do this because the planet might already
        //exist as well, in which case it is not a new update
      } else if (!names.contains(onlyAlphaNumeric(s.getChild().getChild().getName()))){
        //need to assign primary system name found in OEC
        assignOecSyName(s, "system");
        //doing this because the system and planet are the same in oec
        assignOecSyName(s, "star");
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.planets.add(temp);
      }
    }
    //clear updates set so we know we already completed classifying these systems
    UpdateStorage.updates.clear();
  }
  
  /**
   * Remove attribute updates where the system, star or planet doesn't exist in oec, because
   * these updates won't apply. Also if the updates do apply to oec, change the
   */
  public static void removeInvalidUpdatesAndAssignOecNames() {
    //remove invalid from system updates
    Set<String> names = getNamesOEC();
    Set<ArrayList<Systems>> discard = new HashSet<>();
    for (ArrayList<Systems> as : UpdateStorage.systemUpdates) {
      for (Systems s : as) {
        //Invalid update since system doesn't exist in oec
        if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getName()))) {
          discard.add(as);
        }else{
          //system does exist, so assign the oec system name to the object
          assignOecSyName(s, "system");
        }
      }
    }
    UpdateStorage.systemUpdates.removeAll(discard);
    discard = new HashSet<>();
  
    //remove invalid star updates
    for (ArrayList<Systems> as : UpdateStorage.starUpdates) {
      for (Systems s : as) {
        //Invalid update since system doesn't exist in oec
        if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getChild().getName()))) {
          discard.add(as);
        }else{
          //system does exist, so assign the oec system name to the object
          assignOecSyName(s, "system");
          //star name is same as system name in eu and nasa so this will also be the same
          assignOecSyName(s, "star");
        }
      }
    }
    UpdateStorage.starUpdates.removeAll(discard);
    discard = new HashSet<>();
  
    //remove invalid star updates
    for (ArrayList<Systems> as : UpdateStorage.planetUpdates) {
      for (Systems s : as) {
        //Invalid update since system doesn't exist in oec
        if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()))) {
          discard.add(as);
        }else{
          //system does exist, so assign the oec system name to the object
          assignOecSyName(s, "system");
          //star name is same as system name in eu and nasa so this will also be the same
          assignOecSyName(s, "star");
          assignOecSyName(s.getChild().getChild(), "planet");
        }
      }
    }
    UpdateStorage.planetUpdates.removeAll(discard);
  }
  
  private static void findSysPlanetAttributesCommon(ArrayList<ArrayList<Systems>> sysUpdates, String element, String label) {
    HashMap<String, String> OECData;
    HashMap<String, String> props;
    Set<ArrayList<Systems>> discard = new HashSet<>();
    for (int i = 0; i < sysUpdates.size(); i++) {
      ArrayList<Systems> curr = sysUpdates.get(i);
      Systems curs = curr.get(0);
      OECData = new HashMap<>();
      OECData.put("sy_name", curs.getName());
      OECData.put("pl_name", curs.getChild().getName());
      OECData.put("st_name", curs.getChild().getChild().getName());
      String objName;
      if (element.equals("system")) {
        props = curs.getModifiableProperties();
        objName = curs.getName();
        // for element tag "planet"
      } else {
        props = curs.getChild().getChild().getModifiableProperties();
        objName = curs.getChild().getChild().getName();
      }
      for (String key : props.keySet()) {
        if (props.get(key) != null) {
          String property = key.replace("_", "");
          //System.out.println("propname : " +property);
          
          //GET OEC DATA FOR SPECIFIED SYSTEM FOR NOT NULL PROPERTIES
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          boolean sysFound = false;
          Element elm = null;
          try {
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PullingTools.localOecFile);
            doc.getDocumentElement().normalize();
            
            NodeList systemlist = doc.getElementsByTagName(element);
            for (int j = 0; j < systemlist.getLength(); j++) {
              elm = (Element) systemlist.item(j);
              NodeList names = elm.getElementsByTagName("name");
              
              for (int k = 0; k < names.getLength(); k++) {
                Node name = names.item(k);
                String sys_name = name.getTextContent();
                if (DifferenceDetector.onlyAlphaNumeric(objName).equals(DifferenceDetector.onlyAlphaNumeric(sys_name))) {
                  sysFound = true;
                  break;
                }
              }
              if (sysFound) {
                break;
              }
            }
            if (sysFound) {
              NodeList oecPropList = elm.getElementsByTagName(property);
              if (oecPropList.getLength() > 0) {
                Node oecProp = oecPropList.item(0);
                String prop = oecProp.getTextContent();
                //System.out.println("OEC "+DifferenceDetector.onlyAlphaNumeric(prop));
                //System.out.println("LIST "+ DifferenceDetector.onlyAlphaNumeric(props.get(key)));
                if (!(DifferenceDetector.onlyAlphaNumeric(prop).equals(DifferenceDetector.onlyAlphaNumeric(props.get(key))))) {
                  OECData.put(label + key, prop);
                } else {
                  props.put(key, null);
                }
                //case where tag is not in oec, so we should add it
              }else{
                OECData.put(label + key, "");
              }
            }
            //System.out.println(OECData);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      try {
        if (OECData.size() == 3) {
          discard.add(curr);
          //sysUpdates.remove(i);
        } else {
          sysUpdates.get(i).add(SystemBuilder.buildSystemWithHashMap(OECData, "OEC"));
//          System.out.println("OEC" + OECData);
//          System.out.println("Given" + props);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
    sysUpdates.removeAll(discard);
  }
  
  
  /*
  Method adds data from OEC for each system in systemUpdates
   */
  public static void addSysOECData() {
    findSysPlanetAttributesCommon(systemUpdates, "system", "sy_");
  }
  
  /*
Method adds data from OEC for each planet in planetUpdates
 */
  public static void addPlanetOECData() {
    findSysPlanetAttributesCommon(planetUpdates, "planet", "pl_");
  }
  
  /*
Method adds data from OEC for each star in starUpdates
 */
  public static void addStarOECData() {
    HashMap<String, String> OECData;
    HashMap<String, String> props;
    Set<ArrayList<Systems>> discard = new HashSet<>();
    for (int i = 0; i < starUpdates.size(); i++) {
      
      ArrayList<Systems> curr = starUpdates.get(i);
      Systems curs = curr.get(0);
      OECData = new HashMap<>();
      OECData.put("sy_name", curs.getName());
      OECData.put("pl_name", curs.getChild().getName());
      OECData.put("st_name", curs.getChild().getChild().getName());
      //System.out.println("JUNIL"+OECData);
      //System.out.println("JUNIL properties"+ curs.getProperties());
      props = curs.getChild().getModifiableProperties();
      for (String key : props.keySet()) {
        if (props.get(key) != null) {
          String property = key.replace("_", "");
          //System.out.println("propname : " +property);
          //GET OEC DATA FOR SPECIFIED SYSTEM FOR NOT NULL PROPERTIES
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          boolean starFound = false;
          Element elm;
          Node star = null;
          try {
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PullingTools.localOecFile);
            doc.getDocumentElement().normalize();
            
            NodeList systemlist = doc.getElementsByTagName("star");
            for (int j = 0; j < systemlist.getLength(); j++) {
              elm = (Element) systemlist.item(j);
              star = systemlist.item(j);
              NodeList names = elm.getElementsByTagName("name");
              
              for (int k = 0; k < names.getLength(); k++) {
                Node name = names.item(k);
                String star_name = name.getTextContent();
                if (DifferenceDetector.onlyAlphaNumeric(curs.getChild().getName()).equals(DifferenceDetector.onlyAlphaNumeric(star_name))) {
                  starFound = true;
                  break;
                }
              }
              if (starFound) {
                break;
              }
            }
            if (starFound) {
              //NodeList oecPropList = elm.getElementsByTagName(property);
              //Node oecProp = oecPropList.item(0);
              //String prop = oecProp.getTextContent();
              NodeList children = star.getChildNodes();
              String prop = "";
              for (int p = 0; p < children.getLength(); p++) {
                if (children.item(p).getNodeName().equalsIgnoreCase(property)) {
                  prop = children.item(p).getTextContent();
                  break;
                }
              }
              //System.out.println("OEC "+DifferenceDetector.onlyAlphaNumeric(prop));
              //System.out.println("LIST "+ DifferenceDetector.onlyAlphaNumeric(props.get(key)));
              if (!(DifferenceDetector.onlyAlphaNumeric(prop).equals(DifferenceDetector.onlyAlphaNumeric(props.get(key))))) {
                OECData.put("st_" + key, prop);
              } else {
                props.put(key, null);
              }
            }
            //System.out.println(OECData);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      try {
        if (OECData.size() == 3) {
          //System.out.println("in here");
          discard.add(curr);
        } else {
          starUpdates.get(i).add(SystemBuilder.buildSystemWithHashMap(OECData, "OEC"));
          //System.out.println(OECData);
          //System.out.println(props);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
    starUpdates.removeAll(discard);
  }
  
  
  
  /**
   * Assigns the oec system, star and planet names to the given celestial object
   * @param c
   * @return
   */
  public static CelestialObjects assignOecSyName(CelestialObjects c, String label) {
    //Get a list of planet names
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(PullingTools.localOecFile);
      doc.getDocumentElement().normalize();
      NodeList tagNl = doc.getElementsByTagName(label);
      NodeList nameNl;
      Element element;
      String mainName = "";
      for (int i = 0; i < tagNl.getLength(); i++) {
        element = (Element) tagNl.item(i);
        nameNl = element.getElementsByTagName("name");
        for (int j = 0; j < nameNl.getLength(); j++) {
          if (j == 0) {
            mainName = nameNl.item(j).getTextContent();
          }
          if (onlyAlphaNumeric(nameNl.item(j).getTextContent()).
                  equals(onlyAlphaNumeric(c.getName()))) {
            //Found the system, now assign the main name to the system object
            c.setName(mainName);
            return c;
          }
        }
      }
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
    return c;
  }
  
  
  
  
  
  
  public static void main(String[] args) throws SAXException, XPathExpressionException {
    
    try {
      ReadCSV.mapIndexes();
    } catch (IOException | ReadCSV.MissingColumnNameException ex) {
      Logger.getLogger(SystemFinder.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    HashMap<String, String> updates = new HashMap<>();
    //normal planet and stars test
    Planet p2 = new Planet("YBP1194 b", updates, ReadCSV.EU);
    HashMap<String, String> update2 = new HashMap<>();
    Star s2 = new Star("nGC 2682 YBP 1514", update2, p2, ReadCSV.EU);
    Systems sys2 = new Systems("YBP1514", update2, s2, ReadCSV.EU);
    //double binary planet and stars
    Planet p3 = new Planet("HD 186427 B b", updates, ReadCSV.EU);
    Star s3 = new Star("WDS J19418+5032 Aa", update2, p3, ReadCSV.EU);
    Systems sys3 = new Systems("16 Cygni", update2, s3, ReadCSV.EU);
    //single binary
    Planet p1 = new Planet("XO-2N c", updates, ReadCSV.EU);
    Star s1 = new Star("XO-2ac", update2, p1, ReadCSV.EU);
    Systems sys1 = new Systems("XO-2", update2, s1, ReadCSV.EU);
    //planet and stars that dont exist
    Planet p4 = new Planet("Abcdsfa", updates, ReadCSV.EU);
    Star s4 = new Star("kasfi", update2, p4, ReadCSV.EU);
    HashMap<String, String> update3 = new HashMap<>();
    Systems sys4 = new Systems("16 gni", update3, s4, ReadCSV.EU);
    //System.out.println(SystemFinder.getSystem(s1));
    //System.out.println(systemCheck(sys));
    
    UpdateStorage.updates.add(sys1);
    UpdateStorage.updates.add(sys2);
    UpdateStorage.updates.add(sys3);
    UpdateStorage.updates.add(sys4);
    System.out.println("updates");
    for (Systems s : UpdateStorage.updates) {
      System.out.println(s.getName());
      
    }
    classify();
    System.out.println("updates systems");
    for (ArrayList<Systems> s : UpdateStorage.systems) {
      System.out.println(s.get(0).getName());
      
    }
    System.out.println("updates stars");
    for (ArrayList<Systems> s : UpdateStorage.stars) {
      System.out.println(s.get(0).getChild().getName());
      
    }
    System.out.println("updates planets");
    for (ArrayList<Systems> s : UpdateStorage.planets) {
      System.out.println(s.get(0).getChild().getChild().getName());
      
    }
    //System.out.println("planets"+ new_updates.planets);
    //System.out.println("stars" + new_updates.stars);
    //System.out.println("systems" + new_updates.systems);
    //System.out.println("updates" + new_updates.updates);
    //TESTING JUNIL'S METHOD
    try {
      HashMap<String, String> test1 = new HashMap<>();
      test1.put("sy_name", "14 Her");
      test1.put("st_name", "14 Her");
      test1.put("pl_name", "14 her b");
      test1.put("st_mass", "10");
      test1.put("st_temperature", "1766");
      ArrayList<Systems> sarray = new ArrayList<>();
      sarray.add(SystemBuilder.buildSystemWithHashMap(test1, "eu"));
      starUpdates.add(sarray);
      test1 = new HashMap<>();
      test1.put("sy_name", "14 Her");
      test1.put("st_name", "14 her");
      test1.put("pl_name", "14 her b");
      test1.put("st_mass", "5975");
      test1.put("st_temperature", "5311.0");
      //test1.put("st_period", "1766");
      sarray = new ArrayList<>();
      sarray.add(SystemBuilder.buildSystemWithHashMap(test1, "eu"));
      starUpdates.add(sarray);
      System.out.println(starUpdates.size());
    } catch (SystemBuilder.MissingCelestialObjectNameException e1) {
      e1.printStackTrace();
    }
  
  
    //sarray.add(s1);
    addStarOECData();
    System.out.println(starUpdates.size());
  
    for (ArrayList<Systems> as : starUpdates) {
      for (Systems s : as) {
        System.out.println(s.getChild().getProperties());
      }
    }
    
  }
  
  
}
