

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by dhrumil on 11/10/16.
 */
public class DetectUpdates {
  
  
  //TODO: Won't work if there are new column additions. Can we do anything about this? Maybe, just check the columns in the old list. What about cases where the planet names are repeated so key isnt unique?
  //TODO: Problems when columns are removed. long decimals, maybe round them?, also if planet name slightly changes Problem with numbers, scientific notation if it exists, date formats since we are doing only alphanumeric
  public static ArrayList<HashMap<String, HashMap<String, String>>> detectUpdates(
          HashMap<String, HashMap<String, String>> oldCopy, HashMap<String,
          HashMap<String, String>> newCopy) {
    
    HashMap<String, HashMap<String, String>> newPlanets = new HashMap<>();
    HashMap<String, HashMap<String, String>> beforeUpdate = new HashMap<>();
    HashMap<String, HashMap<String, String>> afterUpdate = new HashMap<>();
    boolean isSame;
    for (String key : newCopy.keySet()) {
      if (oldCopy.containsKey(key)) {
        isSame = true;
        //check if the significant columns are in the catalogue
        for (String col : oldCopy.get(key).keySet()){
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
        //Remove if key was found in new Copy, in the end only deleted keys will be left in old copy
        oldCopy.remove(key);
      } else {
        //new planet was added
        newPlanets.put(key, newCopy.get(key));
      }
    }
    ArrayList<HashMap<String, HashMap<String, String>>> detectedUpdates = new ArrayList<>();
    //Deleted Planets
    detectedUpdates.add(oldCopy);
    detectedUpdates.add(newPlanets);
    detectedUpdates.add(beforeUpdate);
    detectedUpdates.add(afterUpdate);
    return detectedUpdates;
  }

  
  //TODO: What if column orders are different in the two databases?
  public static ArrayList<HashMap<String, HashMap<String, String>>> getSpecificColumnUpdates(
          HashMap<String, HashMap<String, String>> beforeUpdate,
          HashMap<String, HashMap<String, String>> afterUpdate) throws IOException,
          ReadCSV.MissingColumnNameException {

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
                equals(DifferenceDetector.onlyAlphaNumeric(afterUpdate.get(planet).get(col))))) {
          //if they are not the same, add them
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
      PullingTools.createLatestCatalogueCopy();
      ArrayList<HashMap<String, HashMap<String, String>>> data = detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, "eu"), ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, "eu"));
      System.out.println(data.get(1));
      System.out.println(data.get(0));
      System.out.println(data.get(2));
      System.out.println(data.get(3));
      System.out.println(getSpecificColumnUpdates(data.get(2), data.get(3)));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
    
  }
  
}