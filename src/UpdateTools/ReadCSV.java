package UpdateTools;

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
  
  /**
   * Hashmap will store column indexes for different catalogues. So catalogue name as it appears in
   * the config file will be mapped to a hashmap of column names to indexes.
   */
  private static HashMap<String, HashMap<String, Integer>> allCatalogueIndexes;
  
  /**
   * Names of the catalogues as in the config file
   */
  private static ArrayList<String> catalogueNames;
  
  /**
   * Remove non alphanumeric characters from the strings in the given list
   * @param stringList
   * @return List of strings containing only alphanumerica characters
   */
  public static ArrayList<String> onlyAlphanumericList(ArrayList<String> stringList) {
    String word;
    for (int i = 0; i < stringList.size(); i++) {
      word = stringList.get(i);
      stringList.remove(i);
      stringList.add(i, DifferenceDetector.onlyAlphaNumeric(word));
    }
    return stringList;
  }
  
  /**
   * Method used to map column names in the catalogues with their associated indexes. This method
   * will also populate the global list containing the catalogue names
   * @throws IOException
   * @throws MissingColumnNameException
   */
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
  
  /**
   * Retrieve the index mappings of the columns in the catalogue. So catalogue name as it appears in
   * the config file will be mapped to a hashmap of column names to indexes
   * @return index mappings of columns
   * @throws IOException
   * @throws MissingColumnNameException
   */
  public static HashMap<String, HashMap<String, Integer>> getIndexMappings() throws IOException,
          MissingColumnNameException {
    HashMap<String, HashMap<String, Integer>> indexCopy = new HashMap<>();
    if (allCatalogueIndexes == null) {
      //will only need to do the mapping the first time this method is called
      mapIndexes();
    }
    
    //Create a deep copy of the index mappings
    for (String key : allCatalogueIndexes.keySet()) {
      indexCopy.put(key, new HashMap<>(allCatalogueIndexes.get(key)));
    }
    return indexCopy;
  }
  
  /**
   * @return A list of catalogue names corresponding to the config file
   */
  public static ArrayList<String> getCatalogueNames() throws IOException, MissingColumnNameException {
    if (catalogueNames == null)
      mapIndexes();
    return new ArrayList<>(catalogueNames);
  }
  
  public static HashMap<String, Integer> sigColWithIndexInCatalogue(String catalogueLabel)
          throws IOException, MissingColumnNameException {
    HashMap<String, HashMap<String, Integer>> iMappings = getIndexMappings();
    HashMap<String, Integer> sigColumns = new HashMap<>();
    
    //Scroll through list of labels in the config file and find the essential labels
    for (String label : iMappings.get(catalogueLabel).keySet()) {
      if (!(label.contains("error") || label.contains("discovery") || label.contains("update"))){
        sigColumns.put(label, iMappings.get(catalogueLabel).get(label));
      }
    }
    return sigColumns;
  }
    
  //TODO, case where a column is removed between older and newer versions of the database
  /**
   * Map planet name to corresponding data
   * @param cataloguePath Path to catalogue
   * @param catalogueLabel Catalogue name label as seen in the config file
   * @return Mapping of planet to data
   * @throws IOException
   * @throws MissingColumnNameException
   */
  public static HashMap<String, HashMap<String, String>> mapPlanetToData(String cataloguePath,
                                                                   String catalogueLabel) throws
          IOException, MissingColumnNameException {
    CSVReader r = new CSVReader(new FileReader(cataloguePath));
    List<String[]> allData = r.readAll();
    r.close();
    HashMap<String, HashMap<String, String>> planetToData = new HashMap<>();
    HashMap<String, Integer> sigColWithIndex = sigColWithIndexInCatalogue(catalogueLabel);
    HashMap<String, String> colWithVal;
    for (int i=1; i < allData.size(); i++) {
      colWithVal = new HashMap<>();
      for (String col:sigColWithIndex.keySet()) {
        //dont need to consider columns that are not in the database or the pl_name column
        if (sigColWithIndex.get(col) != -1 && !(col.equals("pl_name"))) {
          colWithVal.put(col, allData.get(i)[sigColWithIndex.get(col)]);
        }
      }
      //map by planet name
      planetToData.put(allData.get(i)[sigColWithIndex.get("pl_name")], colWithVal);
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
      System.out.println(mapPlanetToData(PullingTools.localNasaArchive, "nasa").get("11 Com b"));
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MissingColumnNameException e) {
      e.printStackTrace();
    }
    
  }
  
}