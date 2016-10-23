import com.sun.org.apache.xpath.internal.SourceTree;

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
  //TODO: long decimals, maybe round them?
  public static ArrayList<HashMap<String, ArrayList<String>>> detectUpdates(
          HashMap<String, ArrayList<String>> oldCopy, HashMap<String, ArrayList<String>> newCopy) {
    
    HashMap<String, ArrayList<String>> newPlanets = new HashMap<>();
    HashMap<String, ArrayList<String>> beforeUpdate = new HashMap<>();
    HashMap<String, ArrayList<String>> afterUpdate = new HashMap<>();
    boolean isSame;
    for (String key : newCopy.keySet()) {
      if (oldCopy.containsKey(key)) {
        isSame = true;
        for (int i = 0; i < oldCopy.get(key).size(); i++) {
          if (!(DifferenceDetector.onlyAlphaNumeric(oldCopy.get(key).get(i)).equals(DifferenceDetector.onlyAlphaNumeric(newCopy.get(key).get(i))))) {
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
    ArrayList<HashMap<String, ArrayList<String>>> detectedUpdates = new ArrayList<>();
    //Deleted Planets
    detectedUpdates.add(oldCopy);
    detectedUpdates.add(newPlanets);
    detectedUpdates.add(beforeUpdate);
    detectedUpdates.add(afterUpdate);
    return detectedUpdates;
  }
  
//  //TODO: What if column orders are different in the two databases?
//  //Assuming everything is in order of the columnNames parameter
//  public static ArrayList<HashMap<String, HashMap<String, String>>> getSpecificColumnUpdates(
//          HashMap<String, ArrayList<String>> beforeUpdate,
//          HashMap<String, ArrayList<String>> afterUpdate) throws IOException, ReadCSV.MissingColumnNameException {
//
//    HashMap<String, HashMap<String, String>> diffAfterUpdate = new HashMap<>();
//    HashMap<String, HashMap<String, String>> diffBeforeUpdate = new HashMap<>();
//    HashMap<String, String> tmpBefore;
//    HashMap<String, String> tmpAfter;
//    ArrayList<HashMap<String, HashMap<String, String>>> diffBetweenData = new ArrayList<>();
//    Set<String> sigCol = ReadCSV.significantColumns();
//    Iterator itsigCol.iterator();
//    //Scroll through all planets
//    for (String planet : beforeUpdate.keySet()) {
//      tmpAfter = new HashMap<>();
//      tmpBefore = new HashMap<>();
//      //Scroll through all planet columns
//      for (int i = 0; i < sigCol.size(); i++) {
//        //Check if the values associated with the columns are the same
//        if (!(beforeUpdate.get(planet).get(i).equals(afterUpdate.get(planet).get(i)))) {
//          //if they are not the same, add them
//          sigCol.
//          tmpAfter.put(sigCol.get(i), afterUpdate.get(planet).get(i));
//          tmpBefore.put(columnNames.get(i), beforeUpdate.get(planet).get(i));
//        }
//      }
//      //Add by planet, the changes in before and after catalogues
//      diffAfterUpdate.put(planet, tmpAfter);
//      diffBeforeUpdate.put(planet, tmpBefore);
//    }
//    //Add both hashmaps to array
//    diffBetweenData.add(diffBeforeUpdate);
//    diffBetweenData.add(diffAfterUpdate);
//    return diffBetweenData;
//  }
  
  //Temp for testing purposes, remove after
  public static void main(String[] args) {
    try {
      ArrayList<HashMap<String, ArrayList<String>>> data = detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, "eu"), ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, "eu"));
      //PullingTools.createLatestCatalogueCopy();
      System.out.println(data.get(1));
      System.out.println(data.get(0));
      System.out.println(data.get(2));
      System.out.println(data.get(3));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
  
  }
  
}