package UpdateTools;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import ModelStarSystems.Planet;
import ModelStarSystems.Star;
import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

import TestSuite.DifferenceDetectorTest;
import com.opencsv.CSVReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static UpdateTools.ReadCSV.NASA;
import static UpdateTools.ReadCSV.mapIndexes;
import static UpdateTools.ReadCSV.mapPlanetToData;

/**
 * Created by Tirth Shah on 2016-11-14.
 */

public class UpdateStorage {
  
  //created HashSets and array lists to store various system objects
  public static Set<Systems> updates = new HashSet<>();
  /**
   * Store new stars. The inner lists are singleton lists containing item. Having all of the
   * data stored in the same format will make it easier to communicate with the front end
   * ui
   */
  public static ArrayList<ArrayList<Systems>> stars = new ArrayList<>();
  /**
   * Store new systems. The inner lists are singleton lists containing item. Having all of the
   * data stored in the same format will make it easier to communicate with the front end
   * ui
   */
  public static ArrayList<ArrayList<Systems>> systems = new ArrayList<>();
  /**
   * Store new planets. The inner lists are singleton lists containing item. Having all of the
   * data stored in the same format will make it easier to communicate with the front end
   * ui
   */
  public static ArrayList<ArrayList<Systems>> planets = new ArrayList<>();
  
  /**
   * Stores attribute changes in star. Index 0 is new data, index 1 is the same data but from OEC
   */
  public static ArrayList<ArrayList<Systems>> starUpdates = new ArrayList<>();
  
  /**
   * Stores attribute changes in system. Index 0 is new data, index 1 is the same data but from OEC
   */
  public static ArrayList<ArrayList<Systems>> systemUpdates = new ArrayList<>();
  
  /**
   * Stores attribute changes in planet. Index 0 is new data, index 1 is the same data but from OEC
   */
  public static ArrayList<ArrayList<Systems>> planetUpdates = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA
   */
  public static ArrayList<ArrayList<Systems>> newPlanetConflicts = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA
   */
  public static ArrayList<ArrayList<Systems>> newStarConflicts = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA
   */
  public static ArrayList<ArrayList<Systems>> newSystemConflicts = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA, Index 2 is OEC
   */
  public static ArrayList<ArrayList<Systems>> syPropConflicts = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA, Index 2 is OEC
   */
  public static ArrayList<ArrayList<Systems>> stPropConflicts = new ArrayList<>();
  
  /**
   * Stores conflicts. Index 0 is data from EU, Index 1 is data from NASA, Index 2 is OEC
   */
  public static ArrayList<ArrayList<Systems>> plPropConflicts = new ArrayList<>();
  
  //Make the initialize private, so no one can initialize it
  private UpdateStorage() {
  }
  
  /**
   * Reinitialize all storage, so it can be reused for the next iteration
   */
  public static void clearAll() {
    updates = new HashSet<>();
    stars = new ArrayList<>();
    systems = new ArrayList<>();
    planets = new ArrayList<>();
    systemUpdates = new ArrayList<>();
    starUpdates = new ArrayList<>();
    planetUpdates = new ArrayList<>();
  }
  
  /**
   * Finds conflicts in the searchIn list and stores them in the storeIn list as tuples. Index
   * 0 of the tuple stores eu data and index 1 stores nasa data. Also those conflicts are removed
   * from the searchIn list
   */
  private static void findConflictsCommon(ArrayList<ArrayList<Systems>> searchIn,
                                          ArrayList<ArrayList<Systems>> storeIn, Class c) {
    ArrayList<Systems> tuple;
    //This will store the arrays to delete from the searchIn List
    Set<ArrayList<Systems>> deleteItems = new HashSet<>();
    String name1 = "";
    String name2 = "";
    //convert set to arrayList so its easy to traverse and will also help find the number of objects there
    //are in the set
    //nested for loop to traverse through each element in the list
    //compare each element with the remaining elements in the list and find duplicates
    //will take O(n) since length of list decreases by 1 every iteration
    for (int i = 0; i < searchIn.size(); i++) {
      tuple = new ArrayList<>();
      for (int j = i + 1; j < searchIn.size(); j++) {
        //found duplicate
        // bad to do it this way, but more important stuff to finish
        if (c == System.class) {
          name1 = searchIn.get(i).get(0).getName();
          name2 = searchIn.get(j).get(0).getName();
        } else if (c == Star.class) {
          name1 = searchIn.get(i).get(0).getChild().getName();
          name2 = searchIn.get(j).get(0).getChild().getName();
        } else if (c == Planet.class) {
          name1 = searchIn.get(i).get(0).getChild().getChild().getName();
          name2 = searchIn.get(j).get(0).getChild().getChild().getName();
        }
        if (DifferenceDetector.onlyAlphaNumeric(name1).equals(DifferenceDetector.onlyAlphaNumeric(name2))) {
          //These will need to be deleted from the searchIn list in the end
          deleteItems.add(searchIn.get(i));
          deleteItems.add(searchIn.get(j));
          //find the source of both duplicates and place them into their respective array lists
          if (searchIn.get(i).get(0).getSource().equals(NASA)) {
            //add eu first and Nasa second
            tuple.add(searchIn.get(j).get(0));
            tuple.add(searchIn.get(i).get(0));
          } else {
            tuple.add(searchIn.get(i).get(0));
            tuple.add(searchIn.get(j).get(0));
          }
          storeIn.add(tuple);
          break;
        }
      }
    }
    
    //Need to remove items with conflicts from the main list
    searchIn.removeAll(deleteItems);
  }
  
  public static void findNewPlanetConflicts() {
    ArrayList<Systems> tuple;
    //will store indexes in the list that have already been
    Set<Integer> exclude = new HashSet<>();
    String name1 = "";
    String name2 = "";
    for (int i = 0; i < planets.size(); i++) {
      //in the previous iterations if j is already identified as having a conflict, dont need
      //to check it again
      if (!exclude.contains(i)) {
        for (int j = i + 1; j < planets.size(); j++) {
          if (!exclude.contains(j)) {
            
            
            //found duplicate
            // bad to do it this way, but more important stuff to finish
            name1 = planets.get(i).get(0).getChild().getChild().getName();
            name2 = planets.get(j).get(0).getChild().getChild().getName();
            if (DifferenceDetector.onlyAlphaNumeric(name1).equals(DifferenceDetector.onlyAlphaNumeric(name2))) {
              //These will need to be deleted from the searchIn list in the end
              exclude.add(i);
              exclude.add(j);
              tuple = new ArrayList<>();
              //find the source of both duplicates and place them into their respective array lists
              if (planets.get(i).get(0).getSource().equals(NASA)) {
                //add eu first and Nasa second
                tuple.add(planets.get(j).get(0));
                tuple.add(planets.get(i).get(0));
              } else {
                tuple.add(planets.get(i).get(0));
                tuple.add(planets.get(j).get(0));
              }
              newPlanetConflicts.add(tuple);
              break;
            }
          }
        }
      }
    }
    //remove from starting from the end of the list, so dont get index out of bounds
    ArrayList<Integer> decreasing = new ArrayList<>(exclude);
    Collections.sort(decreasing, Collections.reverseOrder());
    //Need to remove items with conflicts from the main list
    for (int i : decreasing) {
      planets.remove(i);
    }
  }
  
  public static void findNewStarConflicts() {
    //don't really have to do this since there will never be new planets, the system names and star
    //names are the same in EU and NASA. But there will still have to find conflicts for the
    //attributes. Leaving this here in case, we need to add something here
  }
  
  public static void findNewSystemConflicts() {
    ArrayList<Systems> tuple;
    //will store indexes in the list where conflicts have alreayd been found
    Set<Integer> exclude = new HashSet<>();
    //Will need to look at the the planets as well because tL can be multiple new systems found
    //with new planets in them
    String namepl = "";
    String namepl2 = "";
    String namesy = "";
    String namesy2 = "";
    int i, j;
    for (i = 0; i < systems.size(); i++) {
      if (!exclude.contains(i)) {
        for (j = i + 1; j < systems.size(); j++) {
          if (!exclude.contains(j)) {
            //found duplicate
            // bad to do it this way, but more important stuff to finish
            namesy = systems.get(i).get(0).getName();
            namesy2 = systems.get(j).get(0).getName();
            namepl = systems.get(i).get(0).getChild().getChild().getName();
            namepl2 = systems.get(j).get(0).getChild().getChild().getName();
            if (DifferenceDetector.onlyAlphaNumeric(namesy).equals(DifferenceDetector.onlyAlphaNumeric(namesy2))) {
              //exactly the same system if the planet names are the same as well
              if (DifferenceDetector.onlyAlphaNumeric(namepl).equals(DifferenceDetector.onlyAlphaNumeric(namepl2))) {
                //These will need to be deleted from the searchIn list in the end
                exclude.add(i);
                exclude.add(j);
                //find the source of both duplicates and place them into their respective array lists
                tuple = new ArrayList<>();
                if (systems.get(i).get(0).getSource().equals(NASA)) {
                  //add eu first and Nasa second
                  tuple.add(systems.get(j).get(0));
                  tuple.add(systems.get(i).get(0));
                } else {
                  tuple.add(systems.get(i).get(0));
                  tuple.add(systems.get(j).get(0));
                }
                newSystemConflicts.add(tuple);
              } else {
                //same system but different planet, add this planet in the new planets list
                //make sure to detect the conflicts of planets after detecting the conflicts of systems
                //also make sure to merge the systems first
                tuple = new ArrayList<>();//this wont really be a tuple, just a singleton
                tuple.add(systems.get(j).get(0));
                //need to delete this from the systems list after
                exclude.add(j);
                //once we create the system at i, the object at index j will really be a new planet
                planets.add(tuple);
              }
              //Do another iteration and compare just the planet names, I discovered that sometimes the
              //system names are different but planets are still the same, if this is the case,
              //there is a conflict. It is also better to do this after, because we need to add some of the
              //new systems as new planets for the case where there are multiple same new systems but
              //just with different planets
            } else if (DifferenceDetector.onlyAlphaNumeric(namepl).equals(DifferenceDetector.onlyAlphaNumeric(namepl2))) {
              //These will need to be deleted from the searchIn list in the end
              exclude.add(i);
              exclude.add(j);
              //find the source of both duplicates and place them into their respective array lists
              tuple = new ArrayList<>();
              if (systems.get(i).get(0).getSource().equals(NASA)) {
                //add eu first and Nasa second
                tuple.add(systems.get(j).get(0));
                tuple.add(systems.get(i).get(0));
              } else {
                tuple.add(systems.get(i).get(0));
                tuple.add(systems.get(j).get(0));
              }
              newSystemConflicts.add(tuple);
            }
          }
        }
      }
    }
    
    //remove from starting from the end of the list, so dont get index out of bounds
    ArrayList<Integer> decreasing = new ArrayList<>(exclude);
    Collections.sort(decreasing, Collections.reverseOrder());
    //Need to remove items with conflicts from the main list
    //Need to remove items with conflicts from the main list
    for (
            int index : decreasing)
    
    {
      systems.remove(index);
    }
    
  }
  
  
  //TODO: Tell tirth to store everything as tuple instead, so basically append arraylists of size 2
//to the big arraylist everytime. The arraylist of size 2 would contain the corresponding eu and
//nasa data. This will break tests so will have to fix them
    /*
    Method to find store and find conflicts in a given set of planets. After classifying repeated planets, the method
    will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
    with the two different source lists.
     */
  public static ArrayList<ArrayList<Systems>> findPlanetConflicts() {
    ArrayList<Systems> nasaPlanets = new ArrayList<Systems>();
    ArrayList<Systems> euPlanets = new ArrayList<Systems>();
    ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<
            Systems>>();
    
    //convert set to arrayList so its easy to traverse and will also help find the number of objects there
    //are in the set
    ArrayList<Systems> planetList = new ArrayList(planets);
    
    //nested for loop to traverse through each element in the list
    //compare each element with the remaining elements in the list and find duplicates
    //will take O(n) since length of list decreases by 1 every iteration
    for (int i = 0; i < planetList.size(); i++) {
      for (int j = i + 1; j < planetList.size(); j++) {
        //found duplicate
        //each elemement is of type Systems, so need to get the child of child (ie.planet of the system) to
        //compare
        if (planetList.get(i).getChild().getChild().getName().equals(planetList.get(j).getChild().getChild().
                getName())) {
          //find the source of both duplicates and place them into their respective array lists
          if (planetList.get(i).getSource().equals(NASA)) {
            nasaPlanets.add(planetList.get(i));
            euPlanets.add(planetList.get(j));
          } else {
            euPlanets.add(planetList.get(i));
            nasaPlanets.add(planetList.get(j));
          }
          
        }
      }
    }
    
    planetConflicts.add(nasaPlanets);
    planetConflicts.add(euPlanets);
    
    return planetConflicts;
  }
  
  
  /*
  Method to find store and find conflicts in a given set of stars. After classifying repeated stars, the method
  will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
  with the two different source lists.
   */
  public static ArrayList<ArrayList<Systems>> findStarConflicts() {
    ArrayList<Systems> nasaStars = new ArrayList<Systems>();
    ArrayList<Systems> euStars = new ArrayList<Systems>();
    ArrayList<ArrayList<Systems>> starConflicts = new ArrayList<ArrayList<
            Systems>>();
    
    //convert set to arrayList so its easy to traverse and will also help find the number of objects there
    //are in the set
    ArrayList<Systems> starList = new ArrayList(stars);
    
    //nested for loop to traverse through each element in the list
    //compare each element with the remaining elements in the list and find duplicates
    //will take O(n) since length of list decreases by 1 every iteration
    for (int i = 0; i < starList.size(); i++) {
      for (int j = i + 1; j < starList.size(); j++) {
        //found duplicate
        //each element is of type Systems, so need to get the child(ie.star of the system) to
        //compare
        if (starList.get(i).getChild().getName().equals(starList.get(j).getChild().
                getName())) {
          //find the source of both duplicates and place them into their respective array lists
          if (starList.get(i).getSource().equals(NASA)) {
            nasaStars.add(starList.get(i));
            euStars.add(starList.get(j));
          } else {
            euStars.add(starList.get(i));
            nasaStars.add(starList.get(j));
          }
          
        }
      }
    }
    starConflicts.add(nasaStars);
    starConflicts.add(euStars);
    
    return starConflicts;
  }
  
  /*
 Method to find store and find conflicts in a given set of systems. After classifying repeated systems, the method
 will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
 with the two different source lists.
  */
  public static ArrayList<ArrayList<Systems>> findSystemConflicts() {
    ArrayList<Systems> nasaSystems = new ArrayList<Systems>();
    ArrayList<Systems> euSystems = new ArrayList<Systems>();
    ArrayList<ArrayList<Systems>> systemConflicts = new ArrayList<ArrayList<
            Systems>>();
    
    //convert set to arrayList so its easy to traverse and will also help find the number of objects there
    //are in the set
    ArrayList<Systems> starList = new ArrayList(stars);
    
    //nested for loop to traverse through each element in the list
    //compare each element with the remaining elements in the list and find duplicates
    //will take O(n) since length of list decreases by 1 every iteration
    for (int i = 0; i < starList.size(); i++) {
      for (int j = i + 1; j < starList.size(); j++) {
        //found duplicate
        //each element is of type Systems
        //use __equals__ to compare
        if (starList.get(i).getName().equals(starList.get(j).
                getName())) {
          //find the source of both duplicates and place them into their respective array lists
          if (starList.get(i).getSource().equals(NASA)) {
            nasaSystems.add(starList.get(i));
            euSystems.add(starList.get(j));
          } else {
            euSystems.add(starList.get(i));
            nasaSystems.add(starList.get(j));
          }
          
        }
      }
    }
    systemConflicts.add(nasaSystems);
    systemConflicts.add(euSystems);
    
    return systemConflicts;
  }

  /*
  Method adds data from OEC for each system in systemUpdates
   */
  public static void addSysOECData(ArrayList<ArrayList<Systems>> sysUpdates){
      HashMap<String,String> OECData = new HashMap<>();
      HashMap<String,String> props = new HashMap<>();
      for(int i=0;i<sysUpdates.size();i++){
          ArrayList curr = new ArrayList();
          curr = sysUpdates.get(i);
          Systems curs = (Systems) curr.get(0);
          OECData.put("sy_name", curs.getName());
          OECData.put("pl_name",curs.getChild().getName());
          OECData.put("st_name",curs.getChild().getChild().getName());
          //System.out.println("JUNIL"+OECData);
          //System.out.println("JUNIL properties"+ curs.getProperties());
          props = curs.getProperties();
          for(String key : props.keySet()) {
            if (props.get(key) != null) {
              String property = key.replace("_", "");
              //System.out.println("propname : " +property);

              //GET OEC DATA FOR SPECIFIED SYSTEM FOR NOT NULL PROPERTIES
              DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
              boolean sysFound = false;
              Element elm = null;
              Node sys = null;
              try {
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(PullingTools.localOecFile);
                doc.getDocumentElement().normalize();

                NodeList systemlist = doc.getElementsByTagName("system");
                for (int j = 0; j < systemlist.getLength(); j++) {
                  elm = (Element) systemlist.item(j);
                  sys = systemlist.item(j);
                  NodeList names = elm.getElementsByTagName("name");

                  for (int k = 0; k < names.getLength(); k++) {
                    Node name = names.item(k);
                    String sys_name = name.getTextContent();
                    if (DifferenceDetector.onlyAlphaNumeric(curs.getName()).equals(DifferenceDetector.onlyAlphaNumeric(sys_name))) {
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
                  Node oecProp = oecPropList.item(0);
                  String prop = oecProp.getTextContent();
                  //System.out.println("OEC "+DifferenceDetector.onlyAlphaNumeric(prop));
                  //System.out.println("LIST "+ DifferenceDetector.onlyAlphaNumeric(props.get(key)));
                  if (!(DifferenceDetector.onlyAlphaNumeric(prop).equals(DifferenceDetector.onlyAlphaNumeric(props.get(key))))) {
                    OECData.put("sy_" + key, prop);
                  }else{
                    props.put(key,null);
                  }
                }
                //System.out.println(OECData);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
            try {
              if(OECData.isEmpty()){
                sysUpdates.remove(i);
              }else{
                sysUpdates.get(i).add(SystemBuilder.buildSystemWithHashMap(OECData,"OEC"));
                System.out.println(OECData);
                System.out.println(props);
              }
            } catch (SystemBuilder.MissingCelestialObjectNameException e) {
              e.printStackTrace();
            }
      }
  }

  /*
Method adds data from OEC for each planet in planetUpdates
 */
  public static void addPlanetOECData(ArrayList<ArrayList<Systems>> planetUpdates){
    HashMap<String,String> OECData = new HashMap<>();
    HashMap<String,String> props = new HashMap<>();
    for(int i=0;i<planetUpdates.size();i++){
      ArrayList curr = new ArrayList();
      curr = planetUpdates.get(i);
      Systems curs = (Systems) curr.get(0);
      OECData.put("sy_name", curs.getName());
      OECData.put("pl_name",curs.getChild().getName());
      OECData.put("st_name",curs.getChild().getChild().getName());
      props = curs.getProperties();
      for(String key : props.keySet()) {
        if (props.get(key) != null) {
          String property = key.replace("_", "");
          //GET OEC DATA FOR SPECIFIED SYSTEM FOR NOT NULL PROPERTIES
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          boolean plnFound = false;
          Element elm = null;
          Node pln = null;
          try {
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PullingTools.localOecFile);
            doc.getDocumentElement().normalize();

            NodeList systemlist = doc.getElementsByTagName("planet");
            for (int j = 0; j < systemlist.getLength(); j++) {
              elm = (Element) systemlist.item(j);
              pln = systemlist.item(j);
              NodeList names = elm.getElementsByTagName("name");

              for (int k = 0; k < names.getLength(); k++) {
                Node name = names.item(k);
                String pln_name = name.getTextContent();
                if (DifferenceDetector.onlyAlphaNumeric(curs.getName()).equals(DifferenceDetector.onlyAlphaNumeric(pln_name))) {
                  plnFound = true;
                  break;
                }
              }
              if (plnFound) {
                break;
              }
            }
            if (plnFound) {
              NodeList oecPropList = elm.getElementsByTagName(property);
              Node oecProp = oecPropList.item(0);
              String prop = oecProp.getTextContent();
              //System.out.println("OEC "+DifferenceDetector.onlyAlphaNumeric(prop));
              //System.out.println("LIST "+ DifferenceDetector.onlyAlphaNumeric(props.get(key)));
              if (!(DifferenceDetector.onlyAlphaNumeric(prop).equals(DifferenceDetector.onlyAlphaNumeric(props.get(key))))) {
                OECData.put("pl_" + key, prop);
              }else{
                props.put(key,null);
              }
            }
            //System.out.println(OECData);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      try {
        if(OECData.isEmpty()){
          planetUpdates.remove(i);
        }else{
          planetUpdates.get(i).add(SystemBuilder.buildSystemWithHashMap(OECData,"OEC"));
          System.out.println(OECData);
          System.out.println(props);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
  }
  /*
Method adds data from OEC for each star in starUpdates
 */
  public static void addStarOECData(ArrayList<ArrayList<Systems>> starUpdates){
    HashMap<String,String> OECData = new HashMap<>();
    HashMap<String,String> props = new HashMap<>();
    for(int i=0;i<starUpdates.size();i++){
      ArrayList curr = new ArrayList();
      curr = starUpdates.get(i);
      Systems curs = (Systems) curr.get(0);
      OECData.put("sy_name", curs.getName());
      OECData.put("pl_name",curs.getChild().getName());
      OECData.put("st_name",curs.getChild().getChild().getName());
      //System.out.println("JUNIL"+OECData);
      //System.out.println("JUNIL properties"+ curs.getProperties());
      props = curs.getProperties();
      for(String key : props.keySet()) {
        if (props.get(key) != null) {
          String property = key.replace("_", "");
          //System.out.println("propname : " +property);

          //GET OEC DATA FOR SPECIFIED SYSTEM FOR NOT NULL PROPERTIES
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          boolean starFound = false;
          Element elm = null;
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
                if (DifferenceDetector.onlyAlphaNumeric(curs.getName()).equals(DifferenceDetector.onlyAlphaNumeric(star_name))) {
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
              for(int p=0;p<children.getLength();p++){
                if(children.item(p).getNodeName().equalsIgnoreCase(property)){
                  prop = children.item(p).getTextContent();
                }
              }
              //System.out.println("OEC "+DifferenceDetector.onlyAlphaNumeric(prop));
              //System.out.println("LIST "+ DifferenceDetector.onlyAlphaNumeric(props.get(key)));
              if (!(DifferenceDetector.onlyAlphaNumeric(prop).equals(DifferenceDetector.onlyAlphaNumeric(props.get(key))))) {
                //if(!prop.equals("")){
                  OECData.put("st_" + key, prop);
                //}
              }else{
                props.put(key,null);
              }
            }
            //System.out.println(OECData);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      try {
        if(OECData.isEmpty()){
          starUpdates.remove(i);
        }else{
          starUpdates.get(i).add(SystemBuilder.buildSystemWithHashMap(OECData,"OEC"));
          System.out.println(OECData);
          System.out.println(props);
        }
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
  }
  public static void main(String[] args) {
    try {
      mapIndexes();
      CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
      List<String[]> allData1 = r1.readAll();
      Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
      //System.out.println(Arrays.asList((allData1.get(678))));
      
      CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
      List<String[]> allData2 = r2.readAll();
      Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
      //System.out.println();
      //System.out.println(Arrays.asList((allData2.get(1))));
      s2.getChild().getChild().setName(s1.getChild().getChild().getName());
      Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
      ArrayList<Systems> as = new ArrayList<>();
      as.add(s1);
      UpdateStorage.planets.add(as);
      as = new ArrayList<>();
      as.add(s2);
      UpdateStorage.planets.add(as);
      as = new ArrayList<>();
      as.add(s3);
      UpdateStorage.planets.add(as);
      
      System.out.print("Planets Added: ");
      findNewPlanetConflicts();
      for (ArrayList<Systems> each : UpdateStorage.planets) {
        System.out.print(each.get(0).getChild().getChild().getName() + "   ");
      }
      
      System.out.println();
      System.out.print("Planet Conflicts: ");
      System.out.println(UpdateStorage.newPlanetConflicts.size());
      for (int i = 0; i < UpdateStorage.newPlanetConflicts.size(); i++) {
        System.out.println(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");
        
      }
      //System.out.println(u.findPlanetConflicts());
      
      
      //Systems s = SystemBuilder.buildSystemWithHashMap(test, "eu");
      //TESTING JUNIL'S METHODS 
      //ArrayList<ArrayList<Systems>> sysUps = new ArrayList<>();
      //ArrayList<Systems> sarray = new ArrayList<>();
      //sarray.add(s1);
      //sysUps.add(sarray);
        //addSysOECData(sysUps);
        //addPlanetOECData(sysUps);
        //addStarOECData(sysUps);
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    
  }
}
