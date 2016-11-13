package UpdateTools;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ModelStarSystems.Systems;
import ModelStarSystems.CelestialObjects;
import ModelStarSystems.Planet;
import ModelStarSystems.Star;
import ModelStarSystems.SystemBuilder;

/*
 * Stores Systems objects in a HashSet (to avoid duplicates). The purpose of 
 * using sets is to avoid any duplicate systems, stars or planets. The 
 * sets can be accessed by any other classes since they're all public.  
 */
public class UpdateStorage {
  public Set<Systems> updates;
  public Set<Systems> stars;
  public Set<Systems> systems;
  public Set<Systems> planets;
  public ArrayList<Systems> oldAttributes;
  public ArrayList<Systems> newAttributes;
  
  public UpdateStorage() {
    updates = new HashSet<>();
    stars = new HashSet<>();
    systems = new HashSet<>();
    planets = new HashSet<>();
    oldAttributes = new ArrayList<>();
    newAttributes = new ArrayList<>();
  }
}
