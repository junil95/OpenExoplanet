package UpdateTools;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;
import com.opencsv.CSVReader;

import static UpdateTools.ReadCSV.NASA;
import static UpdateTools.ReadCSV.mapIndexes;
import static UpdateTools.ReadCSV.mapPlanetToData;

/**
 * Created by Tirth Shah on 2016-11-14.
 */

public class UpdateStorage {

    //created HashSets and array lists to store various system objects
    public Set<Systems> updates;
    /**
     * Store new stars
     */
    public Set<Systems> stars;
    /**
     * Store new systems
     */
    public Set<Systems> systems;
    /**
     * Store new planets
     */
    public Set<Systems> planets;
    
    /**
     * Stores attribute changes in star. Index 0 is before, index 1 is after, index 2 is data
     * in OEC
     */
    public ArrayList<ArrayList<Systems>> starUpdates;
    
    /**
     * Stores attribute changes in system. Index 0 is before, index 1 is after, index 2 is data
     * in OEC
     */
    public ArrayList<ArrayList<Systems>> systemUpdates;
    
    /**
     * Stores attribute changes in planet. Index 0 is before, index 1 is after, index 2 is data
     * in OEC
     */
    public ArrayList<ArrayList<Systems>> planetUpdates;

    public UpdateStorage() {
        updates = new HashSet<>();
        stars = new HashSet<>();
        systems = new HashSet<>();
        planets = new HashSet<>();
        systemUpdates = new ArrayList<>();
        starUpdates = new ArrayList<>();
        planetUpdates = new ArrayList<>();
    }
    
    /**
     * Reinitialize all storage, so it can be reused for the next iteration
     */
    public void clearAll() {
        updates = new HashSet<>();
        stars = new HashSet<>();
        systems = new HashSet<>();
        planets = new HashSet<>();
        systemUpdates = new ArrayList<>();
        starUpdates = new ArrayList<>();
        planetUpdates = new ArrayList<>();
    }

    //TODO: Tell tirth to store everything as tuple instead, so basically append arraylists of size 2
    //to the big arraylist everytime. The arraylist of size 2 would contain the corresponding eu and
    //nasa data. This will break tests so will have to fix them
    /*
    Method to find store and find conflicts in a given set of planets. After classifying repeated planets, the method
    will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
    with the two different source lists.
     */
    public ArrayList<ArrayList<Systems>> findPlanetConflicts() {
        ArrayList<Systems> nasaPlanets = new ArrayList<Systems>();
        ArrayList<Systems> euPlanets = new ArrayList<Systems>();
        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<
                Systems>>();

        //convert set to arrayList so its easy to traverse and will also help find the number of objects there
        //are in the set
        ArrayList<Systems> planetList = new ArrayList(planets);

        //nested for loop to traverse through each element in the list
        //compare each element with the remaining elements in the list and find duplicates
        //will take O(n) since length of list decreases by 1 every iteration
        for(int i = 0; i < planetList.size(); i++){
            for(int j = i+1; j < planetList.size(); j++){
                //found duplicate
                //each elemement is of type Systems, so need to get the child of child (ie.planet of the system) to
                //compare
                if(planetList.get(i).getChild().getChild().getName().equals(planetList.get(j).getChild().getChild().
                        getName())){
                    //find the source of both duplicates and place them into their respective array lists
                    if(planetList.get(i).getSource().equals(NASA)){
                        nasaPlanets.add(planetList.get(i));
                        euPlanets.add(planetList.get(j));
                    }
                    else{
                        euPlanets.add(planetList.get(i));
                        nasaPlanets.add(planetList.get(j));
                    }

                }
            }
        }
        planetConflicts.add(nasaPlanets);
        planetConflicts.add(euPlanets);

        return planetConflicts;
    }


    /*
    Method to find store and find conflicts in a given set of stars. After classifying repeated stars, the method
    will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
    with the two different source lists.
     */
    public ArrayList<ArrayList<Systems>> findStarConflicts() {
        ArrayList<Systems> nasaStars = new ArrayList<Systems>();
        ArrayList<Systems> euStars = new ArrayList<Systems>();
        ArrayList<ArrayList<Systems>> starConflicts = new ArrayList<ArrayList<
                Systems>>();

        //convert set to arrayList so its easy to traverse and will also help find the number of objects there
        //are in the set
        ArrayList<Systems> starList = new ArrayList(stars);

        //nested for loop to traverse through each element in the list
        //compare each element with the remaining elements in the list and find duplicates
        //will take O(n) since length of list decreases by 1 every iteration
        for(int i = 0; i < starList.size(); i++){
            for(int j = i+1; j < starList.size(); j++){
                //found duplicate
                //each element is of type Systems, so need to get the child(ie.star of the system) to
                //compare
                if(starList.get(i).getChild().getName().equals(starList.get(j).getChild().
                        getName())){
                    //find the source of both duplicates and place them into their respective array lists
                    if(starList.get(i).getSource().equals(NASA)){
                        nasaStars.add(starList.get(i));
                        euStars.add(starList.get(j));
                    }
                    else{
                        euStars.add(starList.get(i));
                        nasaStars.add(starList.get(j));
                    }

                }
            }
        }
        starConflicts.add(nasaStars);
        starConflicts.add(euStars);

        return starConflicts;
    }

    /*
   Method to find store and find conflicts in a given set of systems. After classifying repeated systems, the method
   will check its respective source (ie. nasa or eu) and will place them in an arrayList of arrayList
   with the two different source lists.
    */
    public ArrayList<ArrayList<Systems>> findSystemConflicts() {
        ArrayList<Systems> nasaSystems = new ArrayList<Systems>();
        ArrayList<Systems> euSystems = new ArrayList<Systems>();
        ArrayList<ArrayList<Systems>> systemConflicts = new ArrayList<ArrayList<
                Systems>>();

        //convert set to arrayList so its easy to traverse and will also help find the number of objects there
        //are in the set
        ArrayList<Systems> starList = new ArrayList(stars);

        //nested for loop to traverse through each element in the list
        //compare each element with the remaining elements in the list and find duplicates
        //will take O(n) since length of list decreases by 1 every iteration
        for(int i = 0; i < starList.size(); i++){
            for(int j = i+1; j < starList.size(); j++){
                //found duplicate
                //each element is of type Systems
                //use __equals__ to compare
                if(starList.get(i).getName().equals(starList.get(j).
                        getName())){
                    //find the source of both duplicates and place them into their respective array lists
                    if(starList.get(i).getSource().equals(NASA)){
                        nasaSystems.add(starList.get(i));
                        euSystems.add(starList.get(j));
                    }
                    else{
                        euSystems.add(starList.get(i));
                        nasaSystems.add(starList.get(j));
                    }

                }
            }
        }
        systemConflicts.add(nasaSystems);
        systemConflicts.add(euSystems);

        return systemConflicts;
    }

    public static void main(String[] args) {
        try {
            mapIndexes();
            CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allData1 = r1.readAll();
            Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
            //System.out.println(Arrays.asList((allData1.get(678))));

            CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allData2 = r2.readAll();
            Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
            //System.out.println();
            //System.out.println(Arrays.asList((allData2.get(1))));

            Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);

            UpdateStorage u = new UpdateStorage();
            u.planets.add(s1);
            u.planets.add(s2);
            u.planets.add(s3);

            System.out.print("Planets Added: ");
            for(Systems each : u.planets){
                System.out.print(each.getChild().getChild().getName() + "   ");
            }

            System.out.println();
            System.out.print("Planet Conflicts: ");
            ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
            planetConflicts = u.findPlanetConflicts();
            for(int i = 0; i < planetConflicts.size(); i++){
                for (int j = 0; j < planetConflicts.get(i).size(); j++){
                    System.out.print(planetConflicts.get(i).get(j).getChild().getChild().getName() + "   ");
                }
            }
            //System.out.println(u.findPlanetConflicts());


            //Systems s = SystemBuilder.buildSystemWithHashMap(test, "eu");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReadCSV.MissingColumnNameException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
//    } catch (SystemBuilder.MissingCelestialObjectNameException e) {
//      e.printStackTrace();
//    }

    }
}
