import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ModelStarSystems.Systems;
import UpdateTools.DifferenceDetector;
import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import UpdateTools.UpdateClassifier;
import UpdateTools.UpdateStorage;

import static UpdateTools.DetectUpdates.detectUpdates;

/**
 * Created by dhrumil on 06/11/16.
 *
 * This class will provide api methods for the front end gui
 */
public class Driver {
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   *
   * @return JSON string containing new planets
   */
  public static String getNewPlanets() {
    return convertToMap(UpdateStorage.planets);
  }
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   *
   * @return JSON string containing new stars
   */
  public static String getNewStars() {
    return convertToMap(UpdateStorage.stars);
  }
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   *
   * @return JSON string containing new Systems
   */
  public static String getNewSystems() {
    return convertToMap(UpdateStorage.systems);
  }
  
  private static String convertToMap(ArrayList<ArrayList<Systems>> allData) {
    ArrayList<ArrayList<HashMap<String, String>>> convertToMap = new ArrayList<>();
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> diffCatalogueData;
    Gson gson = new Gson();
    for (ArrayList<Systems> as : allData) {
      diffCatalogueData = new ArrayList<>();
      for (Systems s : as) {
        //Collapse system hierarchy and store all of the properties in a hashmap
        map = new HashMap<>();
        map.put("sy_name", s.getName());
        map.put("st_name", s.getChild().getName());
        map.put("pl_name", s.getChild().getChild().getName());
        map.put("src", s.getSource());
        //Need to recreate the label identifiers
        for (String label : s.getProperties().keySet()) {
          if (s.getProperties().get(label) == null)
            map.put("sy_" + label, "");
          else
            map.put("sy_" + label, s.getProperties().get(label));
        }
        
        for (String label : s.getChild().getProperties().keySet()) {
          if (s.getChild().getProperties().get(label) == null)
            map.put("st_" + label, "");
          else
            map.put("st_" + label, s.getChild().getProperties().get(label));
        }
        
        for (String label : s.getChild().getChild().getProperties().keySet()) {
          if (s.getChild().getChild().getProperties().get(label) == null) {
            map.put("pl_" + label, "");
          } else
            map.put("pl_" + label, s.getChild().getChild().getProperties().get(label));
        }
        diffCatalogueData.add(map);
      }
      convertToMap.add(diffCatalogueData);
    }
    return gson.toJson(convertToMap);
  }
  
  public static void main(String[] args) {
    try {
      //UpdateTools.PullingTools.createLatestCatalogueCopy();
      ReadCSV.mapIndexes();
      detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, ReadCSV.EU),
              ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, ReadCSV.EU), ReadCSV.EU);
      DifferenceDetector.getNewPlanetIDs(PullingTools.localExoplanetEuOld, ReadCSV.EU);
      UpdateClassifier.classify();

//      System.out.println("Planets\n");
//      for (ArrayList<Systems> as : UpdateStorage.planetUpdates) {
//        System.out.println(as.get(0).getChild().getChild().getName());
//        System.out.println(as.get(0).getChild().getChild().getProperties());
//        //System.out.println(as.get(1).getChild().getChild().getProperties());
//      }
//
//      System.out.println("Stars\n");
//      for (ArrayList<Systems> as : UpdateStorage.starUpdates) {
//        System.out.println(as.get(0).getChild().getName());
//        System.out.println(as.get(0).getChild().getProperties());
//        //System.out.println(as.get(1).getChild().getProperties());
//      }
//
//      System.out.println("Systems\n");
//      for (ArrayList<Systems> as : UpdateStorage.systemUpdates) {
//        System.out.println(as.get(0).getName());
//        System.out.println(as.get(0).getProperties());
//        //System.out.println(as.get(1).getProperties());
//      }
      
      System.out.println(getNewPlanets());
      System.out.println(getNewStars());
      System.out.println(getNewSystems());
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
  }
}
