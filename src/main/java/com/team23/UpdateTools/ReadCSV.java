package com.team23.UpdateTools;

import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.SystemBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.team23.ModelStarSystems.Systems;

/**
 * Remember most of the methods in the class require mapIndexes() to be called once in the beginning
 */
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
   * Key to access EU data from allCatalogueIndexes hashmap
   */
  public static final String EU = "eu";
  
  /**
   * Key to access NASA data from allCatalogueIndexes hashmap
   */
  public static final String NASA = "nasa";
  
  /**
   * Store all labels from the config file
   */
  private static HashSet<String> allLabels;
  
  /**
   * Store system labels
   */
  private static HashSet<String> systemLabels;
  
  /**
   * Store star labels
   */
  private static HashSet<String> starLabels;
  
  /**
   * Store planet labels
   */
  private static HashSet<String> planetLabels;
  
  /**
   * Remove non alphanumeric characters from the strings in the given list
   *
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
   * will also populate the global list containing the catalogue names.
   */
  public static void mapIndexes()
          throws IOException, MissingColumnNameException {
    CSVReader r;
    ArrayList<ArrayList<String>> catColNames = new ArrayList<>();
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
   * the config file will be mapped to a hashmap of column names to indexes. Note, if mapIndexes
   * has not been run, the hashmap will be empty.
   *
   * @return index mappings of columns
   */
  public static HashMap<String, HashMap<String, Integer>> getIndexMappings() {
    HashMap<String, HashMap<String, Integer>> indexCopy = new HashMap<>();
      //Create a deep copy of the index mappings
      for (String key : allCatalogueIndexes.keySet()) {
        indexCopy.put(key, new HashMap<>(allCatalogueIndexes.get(key)));
      }
      return indexCopy;
  }

  public static HashMap<String, Integer> sigColWithIndexInCatalogue(String catalogueLabel){
    
    HashMap<String, Integer> sigColumns = new HashMap<>();
    
    //Scroll through list of labels in the config file and find the essential labels. Using EU
    //to retrieve the keys shouldn't cause any problems
    for (String label : allCatalogueIndexes.get(catalogueLabel).keySet()) {
      if (!(label.contains("error") || label.contains("discovery") || label.contains("update"))) {
        sigColumns.put(label, allCatalogueIndexes.get(catalogueLabel).get(label));
      }
    }
    return sigColumns;
  }
  
  
  
  //TODO, case where a column is removed between older and newer versions of the database
  
  /**
   * Map planet name to corresponding data. Remember, the inner hashmap will only contain
   * columns that actually exist in that database
   *
   * @param cataloguePath  Path to catalogue
   * @param catalogueLabel Catalogue name label as seen in the config file
   * @return Mapping of planet to data
   */
  public static HashMap<String, HashMap<String, String>> mapPlanetToData(String cataloguePath,
                                                                         String catalogueLabel) throws
          IOException{
    CSVReader r = new CSVReader(new FileReader(cataloguePath));
    List<String[]> allData = r.readAll();
    r.close();
    HashMap<String, HashMap<String, String>> planetToData = new HashMap<>();
    HashMap<String, Integer> sigColWithIndex = sigColWithIndexInCatalogue(catalogueLabel);
    HashMap<String, String> colWithVal;
    for (int i = 1; i < allData.size(); i++) {
      colWithVal = new HashMap<>();
      for (String col : sigColWithIndex.keySet()) {
        //dont need to consider columns that are not in the database column
        if (sigColWithIndex.get(col) != -1) {
          colWithVal.put(col, allData.get(i)[sigColWithIndex.get(col)]);
        }
      }
      //map by planet name
      planetToData.put(allData.get(i)[sigColWithIndex.get("pl_name")], colWithVal);
    }
    return planetToData;
  }
  
  /**
   * Retrieve all column labels from the config file
   * @return Set of column names
   */
  public static Set<String> getColumnLabels() {
    if (allLabels == null)
      allLabels = new HashSet<>(allCatalogueIndexes.get(EU).keySet());
    return new HashSet(allLabels);
  }
  
  /**
   * Helper method to retrieve a label with a certain substring
   * @param labelSubstring
   * @return
   */
  private static HashSet<String> getSpecificLabelType(String labelSubstring) {
    HashSet<String> specificLabel= new HashSet<>();
    for (String label:getColumnLabels()) {
      if (label.startsWith(labelSubstring)) {
        specificLabel.add(label);
      }
    }
    return specificLabel;
  }
  
  /**
   * Retrieve system labels
   * @return set of system labels
   */
  public static HashSet<String> getSystemLabels() {
    if (systemLabels == null)
      systemLabels = getSpecificLabelType("sy");
    return new HashSet<String>(systemLabels);
  
  }
  
  /**
   * Retrieve star labels
   * @return set of star labels
   */
  public static HashSet<String> getStarLabels() {
    if (starLabels == null)
      starLabels = getSpecificLabelType("st");
    return new HashSet<String>(starLabels);
  }
  
  /**
   * Retrieve star labels
   * @return set of planet labels
   */
  public static HashSet<String> getPlanetLabels() {
    if (planetLabels == null)
      planetLabels = getSpecificLabelType("pl");
    return new HashSet<String>(planetLabels);
  }
  
  /**
   * Thrown when a column name in the config file doesn't actually exist in the database
   */
  public static class MissingColumnNameException extends Exception {
    public MissingColumnNameException(String colName, String catName) {
      super(colName + " in catalogue " + catName + " was not found. Check settings in config file");
    }
  }
  
  
  public static void main(String[] args) {
    try {
      mapIndexes();
      //System.out.println(getIndexMappings());
      //System.out.println(sigColWithIndexInCatalogue());
//      System.out.println(mapPlanetToData(PullingTools.localNasaArchive, NASA).get("11 Com b"));
      CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
      List<String[]> allData = r.readAll();
      Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
//      HashMap<String, String> test = new HashMap<>();
//      test.put("pl_name", "ssslave");
//      test.put("sy_name", "master");
//      test.put("st_name", "slave");
//      test.put("pl_radius", "0231");
//      test.put("st_radius", "0932");
      //Systems s = SystemBuilder.buildSystemWithHashMap(test, "eu");
      System.out.println(s.getChild().getProperties());

    } catch (IOException e) {
      e.printStackTrace();
    } catch (MissingColumnNameException e) {
      e.printStackTrace();
    }
//    } catch (SystemBuilder.MissingCelestialObjectNameException e) {
//      e.printStackTrace();
//    }

  }
  
}