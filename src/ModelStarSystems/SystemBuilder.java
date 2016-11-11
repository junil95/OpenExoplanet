package ModelStarSystems;

import java.util.HashMap;
import java.util.List;

import UpdateTools.ReadCSV;

/**
 * Created by dhrumil on 06/11/16.
 */
public class SystemBuilder {
  public static Systems buildSystemWithCSVRow(List<String> csvRow, String databaseName){
    HashMap<String, Integer> columnIndexes = ReadCSV.getIndexMappings().get(databaseName);
    HashMap<String, String> systemProperties = new HashMap<>();
    HashMap<String, String> starProperties = new HashMap<>();
    HashMap<String, String> planetProperties = new HashMap<>();
    Systems system;
    Star star;
    Planet planet;
    String systemName = "", starName = "", planetName = "";
    for (String key : columnIndexes.keySet()) {
      if (columnIndexes.get(key) != -1) {
        if (key.startsWith("pl_")) {
          if (key.equals("pl_name"))
            planetName = csvRow.get(columnIndexes.get(key));
          else
            planetProperties.put(key, csvRow.get(columnIndexes.get(key)));
        } else if (key.startsWith("st_")) {
          if (key.equals("st_name"))
            starName = csvRow.get(columnIndexes.get(key));
          else
            starProperties.put(key, csvRow.get(columnIndexes.get(key)));
        } else if (key.startsWith("sy_")) {
          if (key.equals("sy_name"))
            systemName = csvRow.get(columnIndexes.get(key));
          else
            systemProperties.put(key, csvRow.get(columnIndexes.get(key)));
        }
      }
    }
    planet = new Planet(planetName, planetProperties, databaseName);
    star = new Star(starName, starProperties, planet, databaseName);
    system = new Systems(systemName, systemProperties, star, databaseName);
    
    return system;
  }
  
  public static Systems buildSystemWithHashMap(HashMap<String, String> sProperties, String database) throws MissingCelestialObjectNameException {
    HashMap<String, String> systemProperties = new HashMap<>();
    HashMap<String, String> starProperties = new HashMap<>();
    HashMap<String, String> planetProperties = new HashMap<>();
    String systemName ="", starName="", planetName="";
    Systems system;
    Star star;
    Planet planet;
    for (String key : sProperties.keySet()) {
      
        if (key.startsWith("pl_")) {
          if (key.equals("pl_name"))
            planetName = sProperties.get(key);
          else
            planetProperties.put(key, sProperties.get(key));
        } else if (key.startsWith("st_")) {
          if (key.equals("st_name"))
            starName = sProperties.get(key);
          else
            starProperties.put(key, sProperties.get(key));
        } else if (key.startsWith("sy_")) {
          if (key.equals("sy_name"))
            systemName = sProperties.get(key);
          else
            systemProperties.put(key, sProperties.get(key));
        }
      
    }
    if (planetName.equals("") || systemName.equals("") || starName.equals(""))
      throw new MissingCelestialObjectNameException();
    planet = new Planet(planetName, planetProperties, database);
    star = new Star(starName, starProperties, planet, database);
    system = new Systems(systemName, systemProperties, star, database);
    return system;
  }
  
  /**
   * Thrown when a column name in the config file doesn't actually exist in the database
   */
  public static class MissingCelestialObjectNameException extends Exception {
    public MissingCelestialObjectNameException() {
      super("System, Star or Planet Name has not been provided in the HashMap or is an empty string.");
    }
  }
  
}
