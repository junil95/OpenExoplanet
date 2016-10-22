import java.io.BufferedReader;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ReadCSV {
  
  //constants to find planet names
  public static final String matchName1 = "# Name";
  public static final String matchName2 = "pl_name";
  private static HashMap<String, Integer> columnIndexEu;
  private static HashMap<String, Integer> columnIndexNasa;
  private static final String[] columnNamesEu = new String[]{"name", "semi_major_axis", "semi_major_axis_error_min",
          "semi_major_axis_error_max", "omega", "omega_error_min", "omega_error_max", "eccentricity",
          "eccentricity_error_min", "eccentricity_error_max", "orbital_period", "orbital_period_err_min", "orbital_period_err_max",
          "lambda_angle_error_min", "lambda_angle_error_max", "lambda_angle", "inclination_error_min", "inclination_error_max", "inclination",
          "mass", "mass_error_min", "mass_error_max", "radius_error_min", "radius_error_max", "radius", "temp_measured", "detection_type", "discovered", "updated", "star_name", "star_age", "star_radius",
          "star_mass", "star_sp_type", "star_teff", "star_metallicity", "star_distance", "ra", "dec", "discovered", "updated", "detection_type", "alternate_names", "star_alternate_names"};
  
  private static final String[] columnNamesNasa = new String[]{"pl_hostname", "ra_str", "dec_str", "st_raderr2", "st_raderr1", "st_rad", "st_vjerr2", "st_vjerr1", "st_vj",
  "st_icerr2", "st_icerr1", "st_ic", "st_jerr2", "st_jerr1", "st_j", "st_herr2", "stherr1", "st_h", "st_kerr2", "st_kerr1", "st_k", "st_masserr2", "st_masserr1", "st_mass," +
          "st_tefferr2", "st_tefferr1", "st_teff", "pl_name", "pl_orbsmaxerr2", "pl_orbsmaxerr1", "pl_orbsmax", "pl_orbeccenerr2", "pl_orbeccenerr1", "pl_orbeccen", "pl_orblpererr2",
  "pl_orblpererr1", "pl_orblper", "pl_orbinclerr2", "pl_orbinclerr1", "pl_orbincl", "pl_orbpererr2", "pl_orbpererr1", "pl_orbper", "pl_massj", "pl_msinijerr2", "pl_msinijerr1", "pl_msinij",
  "pl_massjerr2", "pl_massjerr1", "pl_massj", "pl_radjerr2", "pl_radjerr1", "pl_radj", "pl_eqterr2", "pl_eqterr1", "pl_eqt", "pl_discmethod", "pl_disc", "rowupdate", "pl_tranmiderr2", "pl_tranmiderr1", "pl_tranmid"};
  
  private static final String[] columnLabelsAll = new String[]{"name", "semi_major_axis", "semi_major_axis_error_min",
          "semi_major_axis_error_max", "omega", "omega_error_min", "omega_error_max", "eccentricity",
          "eccentricity_error_min", "eccentricity_error_max", "orbital_period", "orbital_period_err_min", "orbital_period_err_max",
          "lambda_angle_error_min", "lambda_angle_error_max", "lambda_angle", "inclination_error_min", "inclination_error_max", "inclination",
          "mass", "mass_error_min", "mass_error_max", "radius_error_min", "radius_error_max", "radius", "temp_measured", "detection_type", "discovered", "updated", "star_name", "star_age", "star_radius",
          "star_mass", "star_sp_type", "star_teff", "star_metallicity", "star_distance", "ra", "dec", "discovered", "updated", "detection_type", "alternate_names", "star_alternate_names", "st_raderr_min",
          "st_raderr_max", "st_magv_min", "st_magv_max", "st_"};
  
  public static void populateIndexes() {
//
//    if (columnIndexEu == null || columnIndexNasa == null) {
//      columnIndexEu = new HashMap<>();
//      columnIndexNasa = new HashMap<>();
//
//    }
    System.out.println(columnNamesEu.length);
    System.out.println(columnNamesNasa.length);
  }
  
  public static void main(String[] args) {
    populateIndexes();
    
    
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
  public int findIndexOfPlanetName(ArrayList<String> firstLine) {
    
    //initialize index;
    int index = -1;
    
    //traverse through the list of data to find the index of the col which 
    //has name of all planet names
    for (int i = 0; i <= firstLine.size(); i++) {
      //match current item with constants
      //found index if matched
      if (firstLine.get(i).equals(matchName1) || firstLine.get(i).
              equals(matchName2))
        index = i;
    }
    return index;
  }
  
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
