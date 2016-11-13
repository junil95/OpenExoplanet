package UpdateTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

/**
 * Created by dhrumil on 11/10/16.
 */
public class DetectUpdates {
  
  
  //TODO: Won't work if there are new column additions. Can we do anything about this? Maybe, just check the columns in the old list. What about cases where the planet names are repeated so key isnt unique?
  //TODO: Problems when columns are removed. long decimals, maybe round them?, also if planet name slightly changes Problem with numbers, scientific notation if it exists, date formats since we are doing only alphanumeric
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
            isSame = false;
            break;
          }
        }
        if (!isSame) {
          //If there was a change in the data, store the changes
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
    beforeUpdate = detectedUpdates.get(0);
    afterUpdate = detectedUpdates.get(1);
    Systems s;
    for (String planet : beforeUpdate.keySet()) {
      try {
        s = SystemBuilder.buildSystemWithHashMap(beforeUpdate.get(planet), database);
        us.oldAttributes.add(s);
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
  
    for (String planet : afterUpdate.keySet()) {
      try {
        s = SystemBuilder.buildSystemWithHashMap(beforeUpdate.get(planet), database);
        us.newAttributes.add(s);
      } catch (SystemBuilder.MissingCelestialObjectNameException e) {
        e.printStackTrace();
      }
    }
    
    return us;
  }
//
//
//  //TODO: What if column orders are different in the two databases?
  public static ArrayList<HashMap<String, HashMap<String, String>>> getSpecificColumnUpdates(
          HashMap<String, HashMap<String, String>> beforeUpdate,
          HashMap<String, HashMap<String, String>> afterUpdate){

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
      for (String col:beforeUpdate.get(planet).keySet()) {
        //Check if the values associated with the columns are the same
        if (!(DifferenceDetector.onlyAlphaNumeric(beforeUpdate.get(planet).get(col)).
                equals(DifferenceDetector.onlyAlphaNumeric(afterUpdate.get(planet).get(col))))||col.contains("name")) {
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
      us = detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, ReadCSV.EU), ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, ReadCSV.EU), us, ReadCSV.EU);
      
      for (Systems s : us.oldAttributes) {
        System.out.println(s.getName());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }

  }
  
}