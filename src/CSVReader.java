import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CSVReader {
  
  
  //constants to find planet names
  final String matchName1 = "# Name";
  final String matchName2 = "pl_name";
  
  public static void main (String [] args){
    
    CSVReader obv = new CSVReader();
    //obv.readFile(fileName);
    
    
  }
  
  /*
   * This method returns a hash map of key as planet name, and values as 
   * information about that planet. It takes in a string which is the name 
   * of the file. 
   */
  @SuppressWarnings("resource")
  public HashMap<String, ArrayList<String>> readFile(String fileName) 
      throws FileNotFoundException, IOException{
    
    BufferedReader reader = null;
    
    //stores data from csv file in a list format (separated by comma)
    ArrayList<String> data = new ArrayList<String>();
    String firstLine = "";
    String line = "";
    String splitBy = ",";
    String planetName;
    
    reader = new BufferedReader(new FileReader(fileName));
    //read the first line to find the index of which column is planet name
    //stored in and also to find number of columns there are in the file
    firstLine = reader.readLine();
    data = (ArrayList<String>) Arrays.asList(firstLine.split(splitBy));
    
    //finds the total number of attributes in the csv file
    int totalColumns = data.size();
    
    //find the index of where the name of the planet is
    int planetIndex = findIndexOfPlanetName(data);
      
    //return exception if no planet col found
    if(planetIndex == -1){
      PlanetAttributeNotFoundException e = new 
          PlanetAttributeNotFoundException("Cannot find planet name column");
    }
      
    //create a hash map
    HashMap<String, ArrayList<String>> hashMap = new HashMap<
        String, ArrayList<String>>();
      
    //traverse through file till the end 
    while((line = reader.readLine()) != null){
        
    //split the current line by a comma
    data = (ArrayList<String>) Arrays.asList(line.split(splitBy));
        
    //key of hash map
    planetName = data.get(planetIndex);
        
    //stores values of hash map
    ArrayList<String> planetInfo = new ArrayList<String>();
        
    //traverse through the each row
    for(int i = 0; i < totalColumns; i++){
      //add information about the planet into an array
      //dont add the name of the planet into the list
      if(i != planetIndex){
        planetInfo.add(data.get(i));
      }
    }
        
    //add key, value to hash map
    hashMap.put(planetName, planetInfo);
  }
  return hashMap;
}
 
  /*
   * This method finds the index of the column at which all planet names are 
   * stored in. It takes in an array list of strings and looks for a certain
   * name of the field and outputs an integer; the index."
   */
  public int findIndexOfPlanetName(ArrayList<String> firstLine){
    
    //initialize index;
    int index = -1;
    
    //traverse through the list of data to find the index of the col which 
    //has name of all planet names
    for (int i = 0; i <= firstLine.size(); i++){
      //match current item with constants
      //found index if matched
      if(firstLine.get(i).equals(matchName1) || firstLine.get(i).
          equals(matchName2))
        index = i;
    }
    return index;
  }
  
  public int getNumOfCol(ArrayList<String> line){
    return line.size(); }
}
