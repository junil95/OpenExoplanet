import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ReadCSV {
  
  /**
   * Path to the config file
   */
  public static final String configPath = "Data/columnNames.csv";
  
  /**
   * Paths to the catalogue, must be in the same order as the config file
   */
  private static final String[] cataloguePaths = new String[]{PullingTools.localExoplanetEu,
          PullingTools.localNasaArchive};
  
  private static HashMap<String, HashMap<String, Integer>> allCatalogueIndexes;
  
  /**
   * Names of the catalogues as in the config file
   */
  private static ArrayList<String> catalogueNames;
  
  public static ArrayList<String> onlyAlphanumericList(ArrayList<String> stringList) {
    String word;
    for (int i = 0; i < stringList.size(); i++) {
      word = stringList.get(i);
      stringList.remove(i);
      stringList.add(i, DifferenceDetector.onlyAlphaNumeric(word));
    }
    return stringList;
  }
  
  private static void mapIndexes()
          throws IOException, MissingColumnNameException {
    CSVReader r;
    ArrayList<ArrayList<String>> catColNames = new ArrayList<>();
    catalogueNames = new ArrayList<>();
    // get the column names for each catalogue
    for (String cat : cataloguePaths) {
      r = new CSVReader(new FileReader(cat));
      catColNames.add(onlyAlphanumericList(new ArrayList<>(Arrays.asList(r.readNext()))));
      r.close();
    }
  
    //read config file column names
    r = new CSVReader(new FileReader(configPath));
    
    //store the entire config file in a list
    List<String[]> configData = r.readAll();
    r.close();
  
    allCatalogueIndexes = new HashMap<>();
    HashMap<String, Integer> catColIndex;
    String colName;
    int index;
    //config file column names stored at 0
    for (int i = 1; i < configData.get(0).length; i++) {
      //add catalogue names to global list for later use
      catalogueNames.add(configData.get(0)[i]);
      catColIndex = new HashMap<>();
      for (int j = 1; j < configData.size(); j++) {
        //check if column name associated with the label exists
        colName = configData.get(j)[i];
        if (colName.equals("")) {
          //store -1 if column associated with label doesn't exist in the database
          catColIndex.put(configData.get(j)[0], -1);
        } else {
          //find index of column in the catalogue
          if ((index = catColNames.get(i - 1).indexOf(DifferenceDetector.onlyAlphaNumeric(colName))) != -1) {
            catColIndex.put(configData.get(j)[0], index);
          } else {
            //Will help us know when there is a column name or something else missing in the catalogue
            throw new MissingColumnNameException(colName, configData.get(0)[i]);
          }
        }
      }
      allCatalogueIndexes.put(configData.get(0)[i], catColIndex);
    }
  }
  
  //TODO consider the case where the columns change and the application is still on, the indexes will be mapped wrong then
  public static HashMap<String, HashMap<String, Integer>> getIndexMappings() throws IOException, MissingColumnNameException {
    HashMap<String, HashMap<String, Integer>> indexCopy = new HashMap<>();
    if (allCatalogueIndexes == null) {
      //will only need to do the mapping the first time this method is called
      mapIndexes();
    }
    
    //Create a deep copy of the index mappings
    for (String key:allCatalogueIndexes.keySet()) {
      indexCopy.put(key, new HashMap<>(allCatalogueIndexes.get(key)));
    }
    return indexCopy;
  }
  
  /**
   *@return A list of catalogue names corresponding to the config file
   */
  public static ArrayList<String> getCatalogueNames() throws IOException, MissingColumnNameException {
    if (catalogueNames == null)
      mapIndexes();
    return new ArrayList<>(catalogueNames);
  }
  
  //TODO, case where some enters the wrong label ********
  public static HashMap<String, ArrayList<String>> mapPlanetToData(String cataloguePath, String catalogueLabel) throws IOException, MissingColumnNameException {
    CSVReader r = new CSVReader(new FileReader(cataloguePath));
    List<String[]> allData = r.readAll();
    HashMap<String, ArrayList<String>> planetToData = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> iMappings = getIndexMappings();
    for (String[] row : allData) {
      planetToData.put(row[iMappings.get(catalogueLabel).get("name")], new ArrayList<>(Arrays.asList(row)));
    }
    return planetToData;
  }
  
  public static class MissingColumnNameException extends Exception {
    public MissingColumnNameException(String colName, String catName) {
      super(colName + " in catalogue " + catName + " was not found. Check settings in config file");
    }
  }
  
  public static void main(String[] args) {
    try {
      System.out.println(getIndexMappings());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MissingColumnNameException e) {
      e.printStackTrace();
    }
  
  }
  
  /*
   * This method returns a hash map of key as planet name, and values as 
   * information about that planet. It takes in a string which is the name 
   * of the file. 
   */
//  public static HashMap<String, ArrayList<String>> readFile(String fileName)
//          throws IOException {
//
//    BufferedReader reader = null;
//
//    //stores data from csv file in a list format (separated by comma)
//    ArrayList<String> data;
//    String firstLine = "";
//    String line = "";
//    String splitBy = ",";
//    String planetName;
//
//    reader = new BufferedReader(new FileReader(fileName));
//    //read the first line to find the index of which column is planet name
//    //stored in and also to find number of columns there are in the file
//    firstLine = reader.readLine();
//    data = (ArrayList<String>) Arrays.asList(firstLine.split(splitBy));
//
//    //finds the total number of attributes in the csv file
//    int totalColumns = data.size();
//
//    //find the index of where the name of the planet is
//    int planetIndex = findIndexOfPlanetName(data);
//
//    //return exception if no planet col found
////    if(planetIndex == -1){
////      PlanetAttributeNotFoundException e = new
////          PlanetAttributeNotFoundException("Cannot find planet name column");
////    }
//
//    //create a hash map
//    HashMap<String, ArrayList<String>> hashMap = new HashMap<
//            String, ArrayList<String>>();
//
//    //traverse through file till the end
//    while ((line = reader.readLine()) != null) {
//
//      //split the current line by a comma
//      data = (ArrayList<String>) Arrays.asList(line.split(splitBy));
//
//      //key of hash map
//      planetName = data.get(planetIndex);
//
//      //stores values of hash map
//      ArrayList<String> planetInfo = new ArrayList<String>();
//
//      //traverse through the each row
//      for (int i = 0; i < totalColumns; i++) {
//        //add information about the planet into an array
//        //dont add the name of the planet into the list
//        if (i != planetIndex) {
//          planetInfo.add(data.get(i));
//        }
//      }
//
//      //add key, value to hash map
//      hashMap.put(planetName, planetInfo);
//    }
//    return hashMap;
//  }
//
  /*
   * This method finds the index of the column at which all planet names are 
   * stored in. It takes in an array list of strings and looks for a certain
   * name of the field and outputs an integer; the index."
   */
//  public int findIndexOfPlanetName(ArrayList<String> firstLine) {
//
//    //initialize index;
//    int index = -1;
//
//    //traverse through the list of data to find the index of the col which
//    //has name of all planet names
//    for (int i = 0; i <= firstLine.size(); i++) {
//      //match current item with constants
//      //found index if matched
//      if (firstLine.get(i).equals(matchName1) || firstLine.get(i).
//              equals(matchName2))
//        index = i;
//    }
//    return index;
//  }

//  public static int findIndexOfColumn(String columnName, String filePath) {
//    try {
//      CSVReader reader = new CSVReader();
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    } finally {
//      // Closes the Reader
//
//    }
//
//  }
}
