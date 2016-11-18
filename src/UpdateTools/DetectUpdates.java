package UpdateTools;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

/**
 * Created by dhrumil on 11/10/16.
 */
public class DetectUpdates {
  
  
  //TODO: Won't work if there are new column additions. Can we do anything about this? Maybe, just check the columns in the old list. What about cases where the planet names are repeated so key isnt unique?
  //TODO: Problems when columns are removed. long decimals, maybe round them?, also if planet name slightly changes Problem with numbers, scientific notation if it exists, date formats since we are doing only alphanumeric
//  public static UpdateStorage detectUpdates(
//          HashMap<String, HashMap<String, String>> oldCopy, HashMap<String,
//          HashMap<String, String>> newCopy, UpdateStorage us, String database) {
//
//    HashMap<String, HashMap<String, String>> beforeUpdate = new HashMap<>();
//    HashMap<String, HashMap<String, String>> afterUpdate = new HashMap<>();
//    boolean isSame;
//    for (String key : newCopy.keySet()) {
//      if (oldCopy.containsKey(key)) {
//        isSame = true;
//        //check if the significant columns are the same in the catalogue
//        for (String col : oldCopy.get(key).keySet()) {
//          if (!(DifferenceDetector.onlyAlphaNumeric(oldCopy.get(key).get(col)).equals(
//                  DifferenceDetector.onlyAlphaNumeric(newCopy.get(key).get(col))))) {
//            isSame = false;
//            break;
//          }
//        }
//        if (!isSame) {
//          //If there was a change in the data, store the changes
//          beforeUpdate.put(key, oldCopy.get(key));
//          afterUpdate.put(key, newCopy.get(key));
//        }
//        //Remove if key was found in new Copy. This will make the oldcopy.containskey faster
//        oldCopy.remove(key);
//      } else {
//        //new planet was added
//        Systems s;
//        try {
//          s = SystemBuilder.buildSystemWithHashMap(newCopy.get(key), database);
//          us.updates.add(s);
//        } catch (SystemBuilder.MissingCelestialObjectNameException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//
//    //Now find what specifically updated in the attributes
//    ArrayList<HashMap<String, HashMap<String, String>>> detectedUpdates = getSpecificColumnUpdates(
//            beforeUpdate, afterUpdate);
//    //Overwrite the updates with the specific differences found in get specific column updates
//    beforeUpdate = detectedUpdates.get(0);
//    afterUpdate = detectedUpdates.get(1);
//    Systems s;
//    for (String planet : beforeUpdate.keySet()) {
//      try {
//        s = SystemBuilder.buildSystemWithHashMap(beforeUpdate.get(planet), database);
//        us.oldAttributes.add(s);
//      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
//        e.printStackTrace();
//      }
//    }
//
//    for (String planet : afterUpdate.keySet()) {
//      try {
//        s = SystemBuilder.buildSystemWithHashMap(afterUpdate.get(planet), database);
//        us.newAttributes.add(s);
//      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
//        e.printStackTrace();
//      }
//    }
//
//    return us;
//  }
//
//
  
  //TODO Can improve by comparing numbers differently
  public static UpdateStorage detectUpdates(
          HashMap<String, HashMap<String, String>> oldCopy, HashMap<String,
          HashMap<String, String>> newCopy, UpdateStorage us, String database) {
    
    HashMap<String, HashMap<String, String>> beforeUpdate = new HashMap<>();
    HashMap<String, HashMap<String, String>> afterUpdate = new HashMap<>();
    boolean isSame;
    for (String key : newCopy.keySet()) {
      if (oldCopy.containsKey(key)) {
        isSame = true;
        //check if the significant columns are the same in the catalogue
        for (String col : oldCopy.get(key).keySet()) {
          if (!(DifferenceDetector.onlyAlphaNumeric(oldCopy.get(key).get(col)).equals(
                  DifferenceDetector.onlyAlphaNumeric(newCopy.get(key).get(col))))) {
            //System.out.println(col);
            //System.out.println(oldCopy.get(key).get(col));
            //System.out.println(newCopy.get(key).get(col));
            isSame = false;
            break;
          }
        }
        if (!isSame) {
          //If there was a change in the data, store the changes
//          System.out.println(newCopy.get(key));
//          System.out.println(oldCopy.get(key));
          beforeUpdate.put(key, oldCopy.get(key));
          afterUpdate.put(key, newCopy.get(key));
        }
        //Remove if key was found in new Copy. This will make the oldcopy.containskey faster
        oldCopy.remove(key);
      } else {
        //new planet was added
        Systems s;
        try {
          s = SystemBuilder.buildSystemWithHashMap(newCopy.get(key), database);
          us.updates.add(s);
        } catch (SystemBuilder.MissingCelestialObjectNameException e) {
          e.printStackTrace();
        }
      }
    }
    //Now find what specifically updated in the attributes
    ArrayList<HashMap<String, HashMap<String, String>>> detectedUpdates = getSpecificColumnUpdates(
            beforeUpdate, afterUpdate);
    //Overwrite the updates with the specific differences found in get specific column updates
    us = sortByUpdate(detectedUpdates, us, database);
    return us;
    
  }
  
  public static UpdateStorage sortByUpdate(ArrayList<HashMap<String, HashMap<String, String>>> detectedUpdates,
                                           UpdateStorage us, String database) {
    HashMap<String, HashMap<String, String>> beforeUpdate;
    HashMap<String, HashMap<String, String>> afterUpdate;
    beforeUpdate = detectedUpdates.get(0);
    afterUpdate = detectedUpdates.get(1);
//    System.out.println(beforeUpdate.size());
//    System.out.println(afterUpdate.size());
    Set<String> systemLabels;
    Set<String> planetLabels;
    Set<String> starLabels;
    //look through all the updates, sort them into the three different arraylists
    for (String planet : afterUpdate.keySet()) {
      //loop through all properties
      systemLabels = new HashSet<>();
      planetLabels = new HashSet<>();
      starLabels = new HashSet<>();
      //split properties into different sets
      for (String property : afterUpdate.get(planet).keySet()) {
        //check if a system property changed. Just add the relevant properties to each set
        if (property.contains("sy_") && !property.equals("sy_name")) {
          systemLabels.add(property);
        } else if (property.contains("st_") && !property.equals("st_name")) {
          starLabels.add(property);
        } else if (property.contains("pl_") && !property.equals("pl_name")) {
          planetLabels.add(property);
        }
      }
//      System.out.println(systemLabels);
//      System.out.println(starLabels);
//      System.out.println(planetLabels);
      sortByUpdateTuple(systemLabels, us.systemUpdates, beforeUpdate, afterUpdate, planet, database, "sy_name");
      sortByUpdateTuple(starLabels, us.starUpdates, beforeUpdate, afterUpdate, planet, database, "st_name");
      sortByUpdateTuple(planetLabels, us.planetUpdates, beforeUpdate, afterUpdate, planet, database, "pl_name");
    }
    return us;
  }
  
  private static void sortByUpdateTuple(Set<String> labelType, ArrayList<ArrayList<Systems>> updateList,
                                        HashMap<String, HashMap<String, String>> beforeUpdate,
                                        HashMap<String, HashMap<String, String>> afterUpdate, String planet, String database, String labelTypeString) {
    boolean exist;
    HashMap<String, String> mapForBuilder;
    HashMap<String, String> mapForBuilderBefore;
    // used to store new and old attribute pairs
    ArrayList<Systems> tuples;
    Systems s;
    String objectName = "";
    //Means there is a changed attribute
    if (labelType.size() >= 1) {
      //Now check if system with the same name and the same database doesn't already exist in the update object
      exist = false;
      for (ArrayList<Systems> systemStore : updateList) {
        //bad way of doing it but, can't think of anything else. Would require a lot of work to get around this
        if (labelTypeString.equals("sy_name")) {
          objectName = systemStore.get(0).getName();
        } else if (labelTypeString.equals("st_name")) {
          objectName = systemStore.get(0).getChild().getName();
        } else if (labelTypeString.equals("pl_name")) {
          objectName = systemStore.get(0).getChild().getChild().getName();
        }
        if (DifferenceDetector.onlyAlphaNumeric(objectName).equals(
                DifferenceDetector.onlyAlphaNumeric(afterUpdate.get(planet).get(labelTypeString))) && systemStore.get(0).
                getSource().equals(database)) {
          exist = true;
          break;
        }
      }
      //if the object doesn't exist, then add it
      if (!exist) {
        //Only insert the system labels and the star and planet name, although the star and planet
        //names are really worthless in this case
        mapForBuilder = new HashMap<>();
        mapForBuilderBefore = new HashMap<>();
        tuples = new ArrayList<>();
        mapForBuilder.put("pl_name", afterUpdate.get(planet).get("pl_name"));
        mapForBuilderBefore.put("pl_name", beforeUpdate.get(planet).get("pl_name"));
        mapForBuilder.put("st_name", afterUpdate.get(planet).get("st_name"));
        mapForBuilderBefore.put("st_name", beforeUpdate.get(planet).get("st_name"));
        mapForBuilder.put("sy_name", afterUpdate.get(planet).get("sy_name"));
        mapForBuilderBefore.put("sy_name", beforeUpdate.get(planet).get("sy_name"));
        //The rest of the required labels should just be in systemlabels
        for (String label : labelType) {
          mapForBuilder.put(label, afterUpdate.get(planet).get(label));
          mapForBuilderBefore.put(label, beforeUpdate.get(planet).get(label));
        }
        //once the maps are constructed, build the system object with them
        try {
          s = SystemBuilder.buildSystemWithHashMap(mapForBuilder, database);
          tuples.add(s);
          s = SystemBuilder.buildSystemWithHashMap(mapForBuilderBefore, database);
          tuples.add(s);
          //Add the tuple to the System updates list
          updateList.add(tuples);
        } catch (SystemBuilder.MissingCelestialObjectNameException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  //TODO: What if column orders are different in the two databases?
  public static ArrayList<HashMap<String, HashMap<String, String>>> getSpecificColumnUpdates(
          HashMap<String, HashMap<String, String>> beforeUpdate,
          HashMap<String, HashMap<String, String>> afterUpdate) {
    
    HashMap<String, HashMap<String, String>> diffAfterUpdate = new HashMap<>();
    HashMap<String, HashMap<String, String>> diffBeforeUpdate = new HashMap<>();
    HashMap<String, String> tmpBefore;
    HashMap<String, String> tmpAfter;
    ArrayList<HashMap<String, HashMap<String, String>>> diffBetweenData = new ArrayList<>();
    //Scroll through all planets
    for (String planet : beforeUpdate.keySet()) {
      tmpAfter = new HashMap<>();
      tmpBefore = new HashMap<>();
      //Scroll through all planet columns
      for (String col : beforeUpdate.get(planet).keySet()) {
        //Check if the values associated with the columns are the same
        if (!(DifferenceDetector.onlyAlphaNumeric(beforeUpdate.get(planet).get(col)).
                equals(DifferenceDetector.onlyAlphaNumeric(afterUpdate.get(planet).get(col)))) || col.contains("_name")) {
          // Add them if the values are not the same or if the key is some sort of name. We will need this key later for the
          // system object
          tmpAfter.put(col, afterUpdate.get(planet).get(col));
          tmpBefore.put(col, beforeUpdate.get(planet).get(col));
        }
      }
      //Add by planet, the changes in before and after catalogues
      diffAfterUpdate.put(planet, tmpAfter);
      diffBeforeUpdate.put(planet, tmpBefore);
    }
    //Add both hashmaps to array
    diffBetweenData.add(diffBeforeUpdate);
    diffBetweenData.add(diffAfterUpdate);
    return diffBetweenData;
  }
  
  //Temp for testing purposes, remove after
  public static void main(String[] args) {
    try {
      //UpdateTools.PullingTools.createLatestCatalogueCopy();
      ReadCSV.mapIndexes();
      UpdateStorage us = new UpdateStorage();
      us = detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, ReadCSV.EU),
              ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, ReadCSV.EU), us, ReadCSV.EU);
      
      System.out.println("Planets\n");
      for (ArrayList<Systems> as : us.planetUpdates) {
        System.out.println(as.get(0).getChild().getChild().getName());
        System.out.println(as.get(0).getChild().getChild().getProperties());
        System.out.println(as.get(1).getChild().getChild().getProperties());
      }
  
      System.out.println("Stars\n");
      for (ArrayList<Systems> as : us.starUpdates) {
        System.out.println(as.get(0).getChild().getName());
        System.out.println(as.get(0).getChild().getProperties());
        System.out.println(as.get(1).getChild().getProperties());
      }
  
      System.out.println("Systems\n");
      for (ArrayList<Systems> as : us.systemUpdates) {
        System.out.println(as.get(0).getName());
        System.out.println(as.get(0).getProperties());
        System.out.println(as.get(1).getProperties());
      }
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
    
  }
  
}