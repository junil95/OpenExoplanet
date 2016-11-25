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
<<<<<<< HEAD
=======

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
>>>>>>> master

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
<<<<<<< HEAD
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
=======

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
        for (int index : decreasing)   {
            systems.remove(index);
        }
    }

    public static void findSystemPropertyConflicts(){

        //stores indexes of conflict arrays
        Set<Integer> exclude = new HashSet<>();
        ArrayList<Systems> triplets;

        String namesy1 = "";
        String namesy2 = "";

        Systems s1,sysOEC1;
        Systems s2,sysOEC2;

        for (int i = 0; i < systemUpdates.size(); i++) {
            if (!exclude.contains(i)) {
                for (int j = i + 1; j < systemUpdates.size(); j++) {
                    if (!exclude.contains(j)) {
                        s1 = systemUpdates.get(i).get(0);
                        s2 = systemUpdates.get(j).get(0);

                        namesy1 = s1.getName();
                        namesy2 = s2.getName();

                        triplets = new ArrayList<>();
                        //check for conflicts
                        //same name
                        if (DifferenceDetector.onlyAlphaNumeric(namesy1).equals(DifferenceDetector.onlyAlphaNumeric(namesy2))) {
                            //found conflict
                            //add indexes of respective arrays into the conflict list
                            exclude.add(i);
                            exclude.add(j);

                            //find source of respective system
                            //insert list into respect position
                            //[eu,nasa,oec]
                            if (s1.getSource().equals("NASA")) {
                                triplets.add(s2);
                                triplets.add(s1);
                            } else {
                                triplets.add(s1);
                                triplets.add(s2);
                            }
                            //need to merge(ie.update) property
                            sysOEC1 = systemUpdates.get(i).get(1);
                            sysOEC2 = systemUpdates.get(j).get(1);
                            for (String key : sysOEC1.getProperties().keySet()) {
                                //dont do anything if both are null
                                //dont do anything if the value of second cataloge is null
                                //only update if value of first cataloge is null but second one isnt
                                if ((sysOEC1.getProperties().get(key) == null) && (sysOEC2.getProperties().get(key) != null))
                                    sysOEC1.getModifiableProperties().put(key, sysOEC2.getProperties().get(key));
                            }

                            //add the updated version of the OEC system into the array
                            triplets.add(sysOEC1);

                            //add the merge system into the syPropConflicts
                            syPropConflicts.add(triplets);
                        }

                    }
                }
            }
        }
        //remove from starting from the end of the list, so dont get index out of bounds
        ArrayList<Integer> decreasing = new ArrayList<>(exclude);
        Collections.sort(decreasing, Collections.reverseOrder());
        for (int i : decreasing)
            systemUpdates.remove(i);
    }


    public static void findStarPropertyConflicts(){

        //stores indexes of conflict arrays
        Set<Integer> exclude = new HashSet<>();
        ArrayList<Systems> triplets;

        String namest1 = "";
        String namest2 = "";

        Systems st1,stOEC1;
        Systems st2,stOEC2;

        for (int i = 0; i < starUpdates.size(); i++) {
            if (!exclude.contains(i)) {
                for (int j = i + 1; j < starUpdates.size(); j++) {
                    if (!exclude.contains(j)) {
                        st1 = starUpdates.get(i).get(0);
                        st2 = starUpdates.get(j).get(0);

                        namest1 = st1.getChild().getName();
                        namest2 = st2.getChild().getName();

                        triplets = new ArrayList<>();
                        //check for conflicts
                        //same name
                        if (DifferenceDetector.onlyAlphaNumeric(namest1).equals(DifferenceDetector.onlyAlphaNumeric(namest2))) {
                            //found conflict
                            //add indexes of respective arrays into the conflict list
                            exclude.add(i);
                            exclude.add(j);

                            //find source of respective star
                            //insert list into respect position
                            //[eu,nasa,oec]
                            if (st1.getChild().getSource().equals("NASA")) {
                                triplets.add(st2);
                                triplets.add(st1);
                            } else {
                                triplets.add(st1);
                                triplets.add(st2);
                            }

                            //get OEC values of the 2 catalogues and compare property values
                            stOEC1 = starUpdates.get(i).get(1);
                            stOEC2 = starUpdates.get(j).get(1);
                            for (String key : stOEC1.getChild().getProperties().keySet()) {
                                //dont do anything if both are null
                                //dont do anything if the value of second cataloge is null
                                //only update(ie.merge) if value of first cataloge is null but second one isnt
                                if ((stOEC1.getChild().getProperties().get(key) == null) && (stOEC2.getChild().getProperties().get(key) != null))
                                    stOEC1.getChild().getModifiableProperties().put(key, stOEC2.getChild().getProperties().get(key));
                            }
                            //add the updated version of the OEC system into the array
                            triplets.add(stOEC1);

                            //add the merge system into the syPropConflicts
                            stPropConflicts.add(triplets);
                        }
                    }
                }
            }
        }
        //remove from starting from the end of the list, so dont get index out of bounds
        ArrayList<Integer> decreasing = new ArrayList<>(exclude);
        Collections.sort(decreasing, Collections.reverseOrder());
        for (int i : decreasing)
            starUpdates.remove(i);
    }


    public static void findPlanetPropertyConflicts(){

        //stores indexes of conflict arrays
        Set<Integer> exclude = new HashSet<>();
        ArrayList<Systems> triplets;

        String namepl1 = "";
        String namepl2 = "";

        Systems pl1,plOEC1;
        Systems pl2,plOEC2;

        for (int i = 0; i < planetUpdates.size(); i++) {
            if (!exclude.contains(i)) {
                for (int j = i + 1; j < planetUpdates.size(); j++) {
                    if (!exclude.contains(j)) {
                        pl1 = planetUpdates.get(i).get(0);
                        pl2 = planetUpdates.get(j).get(0);

                        namepl1 = pl1.getChild().getChild().getName();
                        namepl2 = pl2.getChild().getChild().getName();

                        triplets = new ArrayList<>();
                        //check for conflicts
                        //same name
                        if (DifferenceDetector.onlyAlphaNumeric(namepl1).equals(DifferenceDetector.onlyAlphaNumeric(namepl2))) {
                            //found conflict
                            //add indexes of respective arrays into the conflict list
                            exclude.add(i);
                            exclude.add(j);

                            //find source of respective star
                            //insert list into respect position
                            //[eu,nasa,oec]
                            if (pl1.getChild().getChild().getSource().equals("NASA")) {
                                triplets.add(pl2);
                                triplets.add(pl1);
                            } else {
                                triplets.add(pl1);
                                triplets.add(pl2);
                            }

                            //get OEC values of the 2 catalogues and compare property values
                            plOEC1 = planetUpdates.get(i).get(1);
                            plOEC2 = planetUpdates.get(j).get(1);
                            for (String key : plOEC1.getChild().getChild().getProperties().keySet()) {
                                //dont do anything if both are null
                                //dont do anything if the value of second cataloge is null
                                //only update(ie.merge) if value of first cataloge is null but second one isnt
                                if ((plOEC1.getChild().getChild().getProperties().get(key) == null) && (plOEC2.getChild().getChild().getProperties().get(key) != null))
                                    plOEC1.getChild().getChild().getModifiableProperties().put(key, plOEC2.getChild().getChild().getProperties().get(key));
                            }
                            //add the updated version of the OEC system into the array
                            triplets.add(plOEC1);

                            //add the merge system into the syPropConflicts
                            plPropConflicts.add(triplets);
                        }
                    }
                }
            }
        }
        //remove from starting from the end of the list, so dont get index out of bounds
        ArrayList<Integer> decreasing = new ArrayList<>(exclude);
        Collections.sort(decreasing, Collections.reverseOrder());
        for (int i : decreasing)
            planetUpdates.remove(i);
>>>>>>> master
    }
    

    public static void main(String[] args) {
        try {
            mapIndexes();
//      CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//      List<String[]> allData1 = r1.readAll();
//      Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
//      //System.out.println(Arrays.asList((allData1.get(678))));
//
//      CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//      List<String[]> allData2 = r2.readAll();
//      Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//      //System.out.println();
//      //System.out.println(Arrays.asList((allData2.get(1))));
//      s2.getChild().getChild().setName(s1.getChild().getChild().getName());
//      Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
//      ArrayList<Systems> as = new ArrayList<>();
//      as.add(s1);
//      UpdateStorage.planets.add(as);
//      as = new ArrayList<>();
//      as.add(s2);
//      UpdateStorage.planets.add(as);
//      as = new ArrayList<>();
//      as.add(s3);
//      UpdateStorage.planets.add(as);

            //System.out.print("Planets Added: ");
            //findNewPlanetConflicts();
//      for (ArrayList<Systems> each : UpdateStorage.planets) {
//        System.out.print(each.get(0).getChild().getChild().getName() + "   ");
//      }

//      System.out.println();
//      System.out.print("Planet Conflicts: ");
//      System.out.println(UpdateStorage.newPlanetConflicts.size());
//      for (int i = 0; i < UpdateStorage.newPlanetConflicts.size(); i++) {
//        System.out.println(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");
//
//      }
            //System.out.println(us.findPlanetConflicts());


        } catch (
                IOException e)

        {
            e.printStackTrace();
        } catch (
                ReadCSV.MissingColumnNameException e)

        {
            e.printStackTrace();
        } catch (
                ArrayIndexOutOfBoundsException e)

        {
            e.printStackTrace();
        }

    }
}