package UpdateTools;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import ModelStarSystems.Planet;
import ModelStarSystems.Star;
import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

import com.opencsv.CSVReader;

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
        if (name1.equals(name2)) {
          System.out.println("In here");
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
    findConflictsCommon(planets, newPlanetConflicts, Planet.class);
  }
  
  public static void findNewStarConflicts() {
    findConflictsCommon(stars, newStarConflicts, Star.class);
  }
  
  public static void findNewSystemConflicts() {
    findConflictsCommon(systems, newSystemConflicts, Systems.class);
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
        System.out.print(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");

      }
      //System.out.println(u.findPlanetConflicts());
      
      
      //Systems s = SystemBuilder.buildSystemWithHashMap(test, "eu");
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    
  }
}
