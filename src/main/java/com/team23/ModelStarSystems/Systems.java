package com.team23.ModelStarSystems;

import java.util.HashMap;
import com.team23.UpdateTools.ReadCSV;


/**
 * Created by dhrumil on 23/10/16.
 */
public class Systems extends CelestialObjects{
  public Systems(String name, HashMap<String, String> properties, Star star, String source){
    setSource(source);
    setName(name);
    star.setParent(this);
    setChild(star);
    setProperties(ReadCSV.getSystemLabels(), "sy_", properties);
  }
  
}
