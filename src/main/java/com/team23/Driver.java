import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.SystemBuilder;

import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ModelStarSystems.Systems;
import UpdateTools.DifferenceDetector;
import UpdateTools.Merge;
import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import UpdateTools.UpdateClassifier;
import UpdateTools.UpdateStorage;
import UpdateTools.generateXML;
import static UpdateTools.DetectUpdates.detectUpdates;
import static UpdateTools.PullingTools.pullExoplanetEu;
import static UpdateTools.PullingTools.pullNasaArchive;
import static UpdateTools.PullingTools.pullOecOneFile;
import static UpdateTools.PullingTools.pullOecSeperateFiles;
import static UpdateTools.UpdateStorage.findNewPlanetConflicts;

/**
 * Created by dhrumil on 06/11/16.
 *
 * This class will provide api methods for the front end gui
 */
public class Driver {
  
  /**
   * Will store settings such as whether or not the initial merge has been performed yet. It will
   * be indicated by a 0 or 1
   */
  public static final String configPath = "Data/config.txt";
  
  /**
   * Use this to determine whether or not the initial merge still needs to be done. If it has
   * already been done, the initial merge button should be grayed out in the UI
   */
  public static boolean isInitialMergeDone() {
    boolean initialMerge = false;
    File file = new File(configPath);
    String cLine;
    
    if (file.isFile()) {
      try {
        //initial merge is done if the config file exists and the value inside is 1
        BufferedReader br = new BufferedReader(new FileReader(configPath));
        if ((cLine = br.readLine()) != null && (cLine.trim()).equals("1")) {
          initialMerge = true;
        }
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return initialMerge;
  }
  
  /**
   * Perform the initial pull to create local copies of the databases. This can also be used to
   * reset the state of the local copy and start over. This method also creates the config file
   * required to determine if the initial merge is done
   */
  public static void initialSetupOrResetLocalCopies() {
    //pull local files
    try {
      pullExoplanetEu();
      pullNasaArchive();
      pullOecSeperateFiles();
      pullOecOneFile();
    } catch (IOException | ZipException e) {
      e.printStackTrace();
    }
    
    //Create or overwrite the config file.This will reset it
    try {
      PrintWriter writer = new PrintWriter(configPath, "UTF-8");
      writer.println("0");
      writer.close();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Call this when user clicks on initial merge button. This will find the initial differences
   * and store them in lists. Call newSystems, newStars, newPlanets, newPlanetConflicts,
   * newStarConflicts and newSystemConflicts to retrieve the data found by this initial merge.
   */
  public static void detectInitialUpdates() {
    try {
      //Need to do this to find the important columns in the other databases
      ReadCSV.mapIndexes();
      //Get the new planets from NASA and EU
      UpdateStorage.clearAll();
      DifferenceDetector.getNewPlanetIDs(PullingTools.localExoplanetEu, ReadCSV.EU);
      DifferenceDetector.getNewPlanetIDs(PullingTools.localNasaArchive, ReadCSV.NASA);
      //Determine if they are really new planets by looking at OEC
      UpdateClassifier.classifyUpdates();
      //Find conflicts in the classified updates
      //Order matters when finding conflicts, do the systems first, since there can be more than
      //two system conflicts.
      UpdateStorage.findNewSystemConflicts();
      UpdateStorage.findNewStarConflicts();
      UpdateStorage.findNewPlanetConflicts();
    } catch (ReadCSV.MissingColumnNameException | IOException e) {
      e.printStackTrace();
    }
  }
  
  //TODO: store the file names that updated in the merge somewhere. Also need to add the merge
  //part for the other stuff, once Rishi is done
  
  /**
   * Once setPlanets, setStars, setSystems and all of the other sets are called with information
   * from the user, call this to execute the merge. This will merge all of the data from the
   * different lists
   */
  public static void executeMerge() {
    //Need to go through all of the lists and merge them one by one
    //Always merge from top to bottom because some of the new planets require the system to new
    //system to exist
    //merge the new things
    for (ArrayList<Systems> as : UpdateStorage.systems) {
      Merge.newSystem(as.get(0), generateXML.xmlSystem(as.get(0)));
    }
    for (ArrayList<Systems> as : UpdateStorage.stars) {
      Merge.newStar(as.get(0), generateXML.xmlStar(as.get(0)));
    }
    for (ArrayList<Systems> as : UpdateStorage.planets) {
      Merge.newPlanet(as.get(0), generateXML.xmlPlanet(as.get(0)));
    }
  }
  
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
   * Add the new planets based on the user selection
   *
   * The input JSON string should be in the format List<List<Hashmap<String,String>>.
   * In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   */
  public static void setNewPlanets(String json) {
    createObjectFromJson(json, UpdateStorage.planets);
  }
  
  /**
   * Add the new stars based on the user selection
   *
   * The input JSON string should be in the format List<List<Hashmap<String,String>>.
   * In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   */
  public static void setNewStars(String json) {
    createObjectFromJson(json, UpdateStorage.stars);
  }
  
  /**
   * Add the new systems based on the user selection
   *
   * The input JSON string should be in the format List<List<Hashmap<String,String>>.
   * In this case, the inner
   * list is singleton lists with dictionaries. The dictionaries contain the information of a single
   * system. The inner lists are not really required but are used to keep the format consistent
   * with the other methods that return json strings.
   */
  public static void setNewSystems(String json) {
    createObjectFromJson(json, UpdateStorage.systems);
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
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a tuple of dictionaries. The dictionary at index 0 contains new planet in EU catalogue
   * and the dictionary at index 1 contains the same new planet in the NASA catalogue which is
   * causing the conflict.
   */
  public static String getNewPlanetConflicts() {
    return convertToMap(UpdateStorage.newPlanetConflicts);
  }
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a tuple of dictionaries. The dictionary at index 0 contains new system in EU catalogue
   * and the dictionary at index 1 contains the same new system in the NASA catalogue which is
   * causing the conflict.
   */
  public static String getNewSystemConflicts() {
    return convertToMap(UpdateStorage.newSystemConflicts);
  }
  
  /**
   * The JSON string is in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a tuple of dictionaries. The dictionary at index 0 contains new star in EU catalogue
   * and the dictionary at index 1 contains the same new Star in the NASA catalogue which is
   * causing the conflict.
   */
  public static String getNewStarConflicts() {
    return convertToMap(UpdateStorage.newStarConflicts);
  }
  
  //TODO: Can probably remove these three since after the conflicts are resolved, the data
  //can be passed in just the 3 regular lists
  /**
   * Populate star conflicts based on user selection
   * The JSON string should be in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a singleton containing a dictionary of the changes that the user made. The inner list isn't
   * required but it is being used to keep the format consistent
   */
  public static void setNewStarConflicts(String json) {
    createObjectFromJson(json, UpdateStorage.newStarConflicts);
  }
  
  /**
   * Populate system conflicts based on user selection
   * The JSON string should be in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a singleton containing a dictionary of the changes that the user made. The inner list isn't
   * required but it is being used to keep the format consistent
   */
  public static void setNewSystemConflicts(String json) {
    createObjectFromJson(json, UpdateStorage.newSystemConflicts);
  }
  
  /**
   * Populate planet conflicts based on user selection
   * The JSON string should be in the format List<List<Hashmap<String,String>>. In this case, the inner
   * list is a singleton containing a dictionary of the changes that the user made. The inner list isn't
   * required but it is being used to keep the format consistent
   */
  public static void setNewPlanetConflicts(String json) {
    createObjectFromJson(json, UpdateStorage.newPlanetConflicts);
  }
  
  /**
   * Assumes that format of the input json is List<List<Hashmap<String,String>>. Converts data
   * in terms of systems again
   */
  private static void createObjectFromJson(String json, ArrayList<ArrayList<Systems>> allData) {
    //Make sure the provided list is empty
    allData.clear();
    Gson gson = new Gson();
    Systems s;
    Type collectionType = new TypeToken<ArrayList<ArrayList<HashMap<String, String>>>>() {
    }.getType();
    ArrayList<ArrayList<HashMap<String, String>>> map = gson.fromJson(json, collectionType);
    ArrayList<Systems> temp;
    for (ArrayList<HashMap<String, String>> as : map) {
      temp = new ArrayList<>();
      for (HashMap<String, String> m : as) {
        try {
          s = SystemBuilder.buildSystemWithHashMap(m, m.get("src"));
          temp.add(s);
        } catch (SystemBuilder.MissingCelestialObjectNameException e) {
          e.printStackTrace();
        }
      }
      allData.add(temp);
    }
  }
  
  /**
   * Converts the List<List<Systems>> to JSON Strings
   */
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
          //checking the null condition b/c gson removes keys with null values
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
//      detectUpdates(ReadCSV.mapPlanetToData(PullingTools.localExoplanetEuOld, ReadCSV.EU),
//              ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, ReadCSV.EU), ReadCSV.EU);
//      DifferenceDetector.getNewPlanetIDs(PullingTools.localExoplanetEuOld, ReadCSV.EU);
//      UpdateClassifier.classifyUpdates();

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
//
//      System.out.println("Planets\n");
//      for (ArrayList<Systems> as : UpdateStorage.planets) {
//        System.out.println(as.get(0).getChild().getChild().getName());
//        //System.out.println(as.get(0).getChild().getChild().getProperties());
//        //System.out.println(as.get(1).getChild().getChild().getProperties());
//      }
//
//      System.out.println("Stars\n");
//      for (ArrayList<Systems> as : UpdateStorage.stars) {
//        System.out.println(as.get(0).getChild().getName());
//        //System.out.println(as.get(0).getChild().getProperties());
//        //System.out.println(as.get(1).getChild().getProperties());
//      }
//
//      System.out.println("Systems\n");
//      for (ArrayList<Systems> as : UpdateStorage.systems) {
//        System.out.println(as.get(0).getName());
//        //System.out.println(as.get(0).getProperties());
//        //System.out.println(as.get(1).getProperties());
//      }
//
//      System.out.println(getNewPlanets());
//      System.out.println(getNewStars());
//      System.out.println(getNewSystems());
      
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
//
//      System.out.print("Planets Added: ");
//      findNewPlanetConflicts();
//      for (ArrayList<Systems> each : UpdateStorage.planets) {
//        System.out.print(each.get(0).getChild().getChild().getName() + "   ");
//      }

//      System.out.print("Planet Conflicts: ");
//      System.out.println(UpdateStorage.newPlanetConflicts.size());
//      for (int i = 0; i < UpdateStorage.newPlanetConflicts.size(); i++) {
//        System.out.print(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");
//
//      }
      
      //System.out.println(getNewPlanetConflicts());
      //System.out.println(getNewPlanets());
      //initialSetupOrResetLocalCopies();
//      System.out.println(isInitialMergeDone());
//      String json = getNewPlanetConflicts();
//      createObjectFromJson(json);
      ArrayList<String> sorted = new ArrayList<>();
      detectInitialUpdates();
      System.out.println();
      System.out.println("planets");
      System.out.println();
      for (ArrayList<Systems> as : UpdateStorage.planets) {
        sorted.add(as.get(0).getChild().getChild().getName());
      }
      Collections.sort(sorted);
      for (String str : sorted) {
        System.out.println(str);
      }
  
      System.out.println();
      System.out.println("planet conflicts ");
      System.out.println();
      sorted = new ArrayList<>();
      for (ArrayList<Systems> as : UpdateStorage.newPlanetConflicts) {
        sorted.add(as.get(0).getChild().getChild().getName());
      }
      Collections.sort(sorted);
      for (String str : sorted) {
        System.out.println(str);
      }
     
      System.out.println();
      System.out.println("systems");
      System.out.println();
      sorted = new ArrayList<>();
      for (ArrayList<Systems> as : UpdateStorage.systems) {
        sorted.add(as.get(0).getName());
      }
      Collections.sort(sorted);
      for (String str : sorted) {
        System.out.println(str);
      }
      sorted = new ArrayList<>();
      System.out.println();
      System.out.println("systems conflicts");
      System.out.println();
      for (ArrayList<Systems> as : UpdateStorage.newSystemConflicts) {
        sorted.add(as.get(0).getName());
      }
      Collections.sort(sorted);
      for (String str : sorted) {
        System.out.println(str);
      }
      executeMerge();
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ReadCSV.MissingColumnNameException e) {
      e.printStackTrace();
    }
  }
}