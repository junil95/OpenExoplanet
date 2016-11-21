package com.team23.ModelStarSystems;

import java.util.HashMap;

import UpdateTools.ReadCSV;

/**
 * Created by dhrumil on 23/10/16.
 */
public class Planet extends CelestialObjects {
  /**
   * Create a planet
   *
   * @param properties Planet labels. Note, the planet labels need to start with pl
   */
  public Planet(String name, HashMap<String, String> properties, String source) {
    setSource(source);
    setName(name);
    setProperties(ReadCSV.getPlanetLabels(), "pl_", properties);
  }
}
