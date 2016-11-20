/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;

import ModelStarSystems.*;

import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


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
  //exist in OEC
  /**
   * Classify the updates into new planets, stars or systems in the Update storage using the
   * updates list
   */
  public static void classifyUpdates() {
    ArrayList temp;
    Set<String> names = DifferenceDetector.getNamesOEC();
    for (Systems s : UpdateStorage.updates) {
      //If the system doesn't exist, then the update is a new system
      if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getName()))) {
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.systems.add(temp);
        //otherwise check if it is a new star
      } else if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getChild().getName()))) {
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.stars.add(temp);
        //otherwise check if it is a new planet. Need to do this because the planet might already
        //exist as well, in which case it is not a new update
      } else if (!names.contains(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()))){
        temp = new ArrayList();
        temp.add(s);
        UpdateStorage.planets.add(temp);
      }
    }
    //clear updates set so we know we already completed classifying these systems
    UpdateStorage.updates.clear();
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
