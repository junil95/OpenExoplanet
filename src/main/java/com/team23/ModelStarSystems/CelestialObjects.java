package com.team23.ModelStarSystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.team23.UpdateTools.ReadCSV;

//TODO store converted units
/**
 * Created by dhrumil on 06/11/16.
 */
public abstract class CelestialObjects {
  /**
   * Store properties of the celestial object
   */
  private HashMap<String, String> properties;
  
  /**
   * Store the name
   */
  private String name;
  
  /**
   * Store the child
   */
  private CelestialObjects child;
  
  /**
   * Store the parent
   */
  private CelestialObjects parent;
  
  /**
   * Store the database source
   */
  private String source;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getSource() {
    return source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }
  
  public CelestialObjects getChild() {
    return child;
    
  }
  
  public void setChild(CelestialObjects child) {
    this.child = child;
  }
  
  public CelestialObjects getParent() {
    return parent;
  }
  
  public void setParent(CelestialObjects parent) {
    this.parent = parent;
  }
  
  protected void setProperties(HashSet<String> objectLabels, String labelPrefix, HashMap<String, String> properties) {
    this.properties = new HashMap<>();
    
    for (String label : objectLabels) {
      //don't need to add the system name to the system properties
      if (!label.contains("_name")){
        //Remove the first 3 digits of the label
        this.properties.put(label.substring(3), null);
      }
    }
    
    for (String property : properties.keySet()) {
      if (property.startsWith(labelPrefix) && (this.properties.containsKey(property.substring(3))) && (!properties.get(property).equals(""))){
    	this.properties.put(property.substring(3), properties.get(property));
      }
    }
  }
  
  public HashMap<String, String> getModifiableProperties() {
    return properties;
  }
  
  public HashMap<String, String> getProperties() {
    return new HashMap<>(properties);
  }
}
