package ModelStarSystems;

import java.util.HashMap;
import UpdateTools.ReadCSV;


/**
 * Created by dhrumil on 23/10/16.
 */
public class Systems extends CelestialObjects{
  public Systems(String name, HashMap<String, String> properties, Star star){
    setName(name);
    star.setParent(this);
    setChild(star);
    setProperties(ReadCSV.getSystemLabels(), "sy_", properties);
  }
  
}
