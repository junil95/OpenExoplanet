import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dhrumil on 11/10/16.
 */
public class DetectUpdates {
  private static void renameOldCatalogueVersion(String oldPath, String newPath) throws IOException {
    //rename the old catalogues
    File oldName = new File(oldPath);
    File newName = new File(newPath);
    //remove exoplanetEuOld if it exists
    if (newName.exists())
      newName.delete();
    //Now rename
    if (!oldName.renameTo(newName)) {
      throw new IOException("Unable to rename " + oldPath + " to " + newPath);
    }
  }
  
  public static void createLatestCatalogueCopy() throws IOException {
    renameOldCatalogueVersion("Data/exoplanetEu/exoplanetEu.csv", "Data/exoplanetEu/exoplanetEuOld.csv");
    PullingTools.pullExoplanetEu();
    renameOldCatalogueVersion("Data/nasaArchive/nasaArchive.csv", "Data/nasaArchive/nasaArchiveOld.csv");
    PullingTools.pullNasaArchive();
  }
  
  //TODO: Won't work if there are new column additions. Can we do anything about this? Maybe, just check the columns in the old list. What about cases where the planet names are repeated so key isnt unique?
  public static ArrayList<HashMap<String, ArrayList<String>>> detectUpdates(
          HashMap<String, ArrayList<String>> oldCopy, HashMap<String, ArrayList<String>> newCopy) {
    
    HashMap<String, ArrayList<String>> newPlanets = new HashMap<>();
    HashMap<String, ArrayList<String>> beforeUpdate = new HashMap<>();
    HashMap<String, ArrayList<String>> afterUpdate = new HashMap<>();
    for (String key : newCopy.keySet()) {
      if (oldCopy.containsKey(key)) {
        if (!(oldCopy.get(key).equals(newCopy.get(key)))) {
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
  
  //TODO: What if column orders are different in the two databases? Also, we just need to worry about the important columns, ignore the others
  //Assuming everything is in order of the columnNames parameter
  public static ArrayList<HashMap<String, HashMap<String, String>>> getSpecificColumnUpdates(
          HashMap<String, ArrayList<String>> beforeUpdate,
          HashMap<String, ArrayList<String>> afterUpdate, ArrayList<String> columnNames) {
    
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
      for (int i = 0; i < columnNames.size(); i++) {
        //Check if the values associated with the columns are the same
        if (!(beforeUpdate.get(planet).get(i).equals(afterUpdate.get(planet).get(i)))) {
          //if they are not the same, add them
          tmpAfter.put(columnNames.get(i), afterUpdate.get(planet).get(i));
          tmpBefore.put(columnNames.get(i), beforeUpdate.get(planet).get(i));
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
//    try {
//      createLatestCatalogueCopy();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    System.out.println("hello");
  }
  
}