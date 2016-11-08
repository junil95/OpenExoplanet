package ModelStarSystems;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import UpdateTools.ReadCSV;


/**
 * Created by dhrumil on 23/10/16.
 */
public class Systems extends CelestialObjects{
  /**
   * Store system properties in a hashmap
   */
  private HashMap<String, String> properties;
  
  /**
   * Store system name
   */
  private String name;
  
  /**
   * Store the child of the system
   */
  private Star child;
  
  /**
   * Create a system with a child star
   * @param properties System labels. Note, the system labels need to start with sy
   * @param star
   */
  public Systems(String name, HashMap<String, String> properties, Star star){
    this.name = name;
    star.setParent(this);
    this.child = star;
    
    //Populate hashmap with system labels
    for (String label : ReadCSV.getSystemLabels()) {
      //don't need to add the system name to the system properties
      if (!label.contains("name")){
        //Remove the first 3 digits of the label
        this.properties.put(label.substring(3), null);
      }
    }
    for (String property : properties.keySet()) {
      if (property.startsWith("sy_") && (this.properties.containsKey(property.substring(3)))){
        this.properties.replace(property.substring(3), properties.get(property));
      }
    }
  }
  
  
}
