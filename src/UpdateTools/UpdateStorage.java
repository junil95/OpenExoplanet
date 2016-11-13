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
import UpdateTools.Pair;
/*
 * Stores Systems objects in a HashSet (to avoid duplicates). The purpose of 
 * using sets is to avoid any duplicate systems, stars or planets. The 
 * sets can be accessed by any other classes since they're all public.  
 */
public class UpdateStorage {
  
    //created HashSets and array lists to store various system objects
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
    

    public ArrayList<ArrayList<Systems>> findPlanetConflicts(){
        ArrayList<Systems> nasaPlanets = new ArrayList<Systems>();
        ArrayList<Systems> euPlanets = new ArrayList<Systems>();
        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<
            Systems>>();
        
        //declare a variable of type Pair storing information about conflicts
        Pair dup;
        for(Systems eachPlanet : planets){
          //determines if theres a duplicate planet name in the list
          //adds it in the conflict list if it does
          //otherwise ignores it
          dup = findDuplicates(eachPlanet.getName(), planets);
          //found a duplicate
          //add planet in either nasa or eu list; respectively
          if(dup.duplicateOrNot){
            if(eachPlanet.getSource() == "nasa"){
              nasaPlanets.add(eachPlanet);
              euPlanets.add(getItemAtIndex(planets,dup.index));
            }
            else{
              euPlanets.add(eachPlanet);
              nasaPlanets.add(getItemAtIndex(planets,dup.index));
            }
          }
        }
        
        //add both lists into a bigger list and return it
        planetConflicts.add(nasaPlanets);
        planetConflicts.add(euPlanets);
        return planetConflicts;
    }
    
    public ArrayList<ArrayList<Systems>> findStarConflicts(){
      ArrayList<Systems> nasaStars = new ArrayList<Systems>();
      ArrayList<Systems> euStars = new ArrayList<Systems>();
      ArrayList<ArrayList<Systems>> starConflicts = new ArrayList<ArrayList<
          Systems>>();
      
      Pair dup;
      for(Systems eachStar : stars){
        //determines if theres a duplicate star name in the list
        //adds it in the conflict list if it does
        //otherwise ignores it
        dup = findDuplicates(eachStar.getName(), stars);
        //found a duplicate
        //add star in either nasa or eu list; respectively
        if(dup.duplicateOrNot){
          if(eachStar.getSource() == "nasa"){
            nasaStars.add(eachStar);
            euStars.add(getItemAtIndex(stars,dup.index));
          }
          else{
            euStars.add(eachStar);
            nasaStars.add(getItemAtIndex(stars,dup.index));
          }
        }
      }
      starConflicts.add(nasaStars);
      starConflicts.add(euStars);
      return starConflicts;
    }
    
    public ArrayList<ArrayList<Systems>> findSystemConflicts(){
      ArrayList<Systems> nasaSystems = new ArrayList<Systems>();
      ArrayList<Systems> euSystems = new ArrayList<Systems>();
      ArrayList<ArrayList<Systems>> systemConflicts = new ArrayList<ArrayList<
          Systems>>();
      
      Pair dup;
      for(Systems eachSystem : systems){
        //determines if theres a duplicate system name in the list
        //adds it in the conflict list if it does
        //otherwise ignores it
        dup = findDuplicates(eachSystem.getName(), systems);
        //found a duplicate
        //add star in either nasa or eu list; respectively
        if(dup.duplicateOrNot){
          if(eachSystem.getSource() == "nasa"){
            nasaSystems.add(eachSystem);
            euSystems.add(getItemAtIndex(systems,dup.index));
          }
          else{
            euSystems.add(eachSystem);
            nasaSystems.add(getItemAtIndex(systems,dup.index));
          }
        }
      }
      systemConflicts.add(nasaSystems);
      systemConflicts.add(euSystems);
      return systemConflicts;
    }
    
    /*
     * This method takes in a Systems set with an index where it returns a
     * Systems object at that index in the set. Need to create this  
     * functionality because a Set structure doesn't have a method like
     * getIndex(i) to get a specific value in the set at index i. 
     */
    private Systems getItemAtIndex(Set<Systems> type, int index){
      int i = 0;
      Systems ret = null;
      for (Systems each : type){
        if(i == index){
          ret = each;
        }
        i++;
      }
      return ret;
    }
    
    /*
     * This method takes in a string name and a set of systems to find out
     * if a systems obj with the given name is duplicate in the given set from
     * another catalog. 
     */
    private Pair findDuplicates(String name, Set<Systems> type){
      //not a duplicate then first value of list is -1
      //otherwise first value is 1 (indicating true; it is a  duplicate) and
      //second value is the index of that duplicate
      int index=0,count=0;
      Pair ret = null;      
      
      for (Systems each: type){
        if(name == each.getName()){
          count++;
          //duplicate occurs at count = 2 because count = 1 will always occur
          //since the current object will compare with its self once
          if(count == 2){
            //found a duplicate ie.set it to true
            ret.duplicateOrNot = true;
            ret.index = index;
          }
        }
        index++;
      }
      return ret;
    }
}