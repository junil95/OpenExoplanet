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

 
  public static void storeSystems(){
    
    Set<Systems> systems = new HashSet<Systems>();
  }
  
  public static void storeStars(){
    Set<Systems> stars = new HashSet<Systems>();
  }
  
  public static void storePlanets(){
    Set<Systems> planets = new HashSet<Systems>();

  }
  
  public static void storeAttributes(){
    Set<Systems> attributes = new HashSet<Systems>();

  }
  
  public static void storeBeforeUpdates(){
    Set<Systems> stars = new HashSet<Systems>();
  }

    
}
