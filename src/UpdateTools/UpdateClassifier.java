/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;

import ModelStarSystems.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import static UpdateTools.DifferenceDetector.onlyAlphaNumeric;


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
        s = assignOecSyName(s);
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.stars.add(temp);
        //otherwise check if it is a new planet. Need to do this because the planet might already
        //exist as well, in which case it is not a new update
      } else if (!names.contains(onlyAlphaNumeric(s.getChild().getChild().getName()))){
        //need to assign primary system name found in OEC
        s = assignOecSyName(s);
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.planets.add(temp);
      }
    }
    //clear updates set so we know we already completed classifying these systems
    UpdateStorage.updates.clear();
  }
  
  /**
   * If the system has an alternate name in the other catalogues, this will assign the main name
   * that oec uses. This will only work if the system isn't brand new
   * @param s
   * @return
   */
  public static Systems assignOecSyName(Systems s) {
    //Get a list of planet names
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(PullingTools.localOecFile);
      doc.getDocumentElement().normalize();
      NodeList tagNl = doc.getElementsByTagName("system");
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
                  equals(onlyAlphaNumeric(s.getName()))) {
            //Found the system, now assign the main name to the system object
            s.setName(mainName);
            return s;
          }
        }
      }
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
    return s;
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
    
    
  }
  
  
}
