package ModelStarSystems;

import java.util.HashMap;

import UpdateTools.ReadCSV;

/**
 * Created by dhrumil on 23/10/16.
 */
public class Star extends CelestialObjects {
  
  /**
   * Create a star with a child planet
   *
   * @param properties Star labels. Note, the star labels need to start with st
   */
  public Star(String name, HashMap<String, String> properties, Planet planet, String source) {
    setSource(source);
    setName(name);
    planet.setParent(this);
    setChild(planet);
    setProperties(ReadCSV.getStarLabels(), "st_", properties);
  }
}
