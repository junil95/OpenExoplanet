package TestSuite;

import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import UpdateTools.UpdateStorage;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Before;
import org.junit.Test;
import com.opencsv.CSVReader;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import ModelStarSystems.Planet;
import ModelStarSystems.Star;
import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;

import com.opencsv.CSVReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static UpdateTools.ReadCSV.NASA;
import static UpdateTools.ReadCSV.mapIndexes;


import static org.junit.Assert.*;

/**
 * Created by Tirth Shah on 2016-11-14.
 */
public class UpdateStorageTest {

    /*
    private Set<Systems> updates;
    private ArrayList<ArrayList<Systems>> stars;
    private ArrayList<ArrayList<Systems>> systems;
    private ArrayList<ArrayList<Systems>> starUpdates;
    private ArrayList<ArrayList<Systems>> systemUpdates;
    private ArrayList<ArrayList<Systems>> planetUpdates;
    private ArrayList<ArrayList<Systems>> newPlanetConflicts;
    private ArrayList<ArrayList<Systems>> newStarConflicts;
    private ArrayList<ArrayList<Systems>> newSystemConflicts;
    private ArrayList<ArrayList<Systems>> syPropConflicts;
    private ArrayList<ArrayList<Systems>> stPropConflicts;
    private ArrayList<ArrayList<Systems>> plPropConflicts;*/

    private ArrayList<ArrayList<Systems>> planets;

    @Before
    public void setUp() throws Exception {
        /*
        updates = new HashSet<>();
        stars = new ArrayList<>();
        systems = new ArrayList<>();
        starUpdates = new ArrayList<>();
        systemUpdates = new ArrayList<>();
        planetUpdates = new ArrayList<>();
        newPlanetConflicts = new ArrayList<>();
        newStarConflicts = new ArrayList<>();
        newSystemConflicts = new ArrayList<>();
        syPropConflicts = new ArrayList<>();
        stPropConflicts = new ArrayList<>();
        plPropConflicts = new ArrayList<>(); */

        planets = new ArrayList<>();
        
    }

    /*
    @Test
    public void findConflictsCommon(){

    }*/

    @Test
    public void findNewPlanetConflicts() {
        try {
            mapIndexes();
            CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allData1 = r1.readAll();
            Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
            //System.out.println(Arrays.asList((allData1.get(678))));
//
            CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allData2 = r2.readAll();
            Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
            //System.out.println();
//      //System.out.println(Arrays.asList((allData2.get(1))));
            s2.getChild().getChild().setName(s1.getChild().getChild().getName());
            Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
            ArrayList<Systems> as = new ArrayList<>();
            as.add(s1);
            UpdateStorage.planets.add(as);
            as = new ArrayList<>();
            as.add(s2);
            UpdateStorage.planets.add(as);
            as = new ArrayList<>();
            as.add(s3);
            UpdateStorage.planets.add(as);

            System.out.print("Planets Added: ");
            findNewPlanetConflicts();
            for (ArrayList<Systems> each : UpdateStorage.planets) {
                System.out.print(each.get(0).getChild().getChild().getName() + "   ");
            }

//      System.out.println();
//      System.out.print("Planet Conflicts: ");
//      System.out.println(UpdateStorage.newPlanetConflicts.size());
//      for (int i = 0; i < UpdateStorage.newPlanetConflicts.size(); i++) {
//        System.out.println(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");
//
//      }
            //System.out.println(us.findPlanetConflicts());


        } catch (
                IOException e)

        {
            //e.printStackTrace();
        } catch (
                ReadCSV.MissingColumnNameException e)

        {
            //e.printStackTrace();
        } catch (
                ArrayIndexOutOfBoundsException e)

        {
            //e.printStackTrace();
        }

    }

    /*
    @Test
    public void findNewSystemConflicts(){

    }*/

    /*
    @Test
    public void findSystemPropertyConflicts(){

    }

    @Test
    public void findStarPropertyConflicts(){

    }

    @Test
    public void findPlanetPropertyConflicts(){

    }*/

//    @Test
//    public void findPlanetConflictsTest() throws IOException, ReadCSV.MissingColumnNameException {
//        mapIndexes();
//        CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//        List<String[]> allData1 = r1.readAll();
//        Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
//        //System.out.println(Arrays.asList((allData1.get(678))));
//
//        CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//        List<String[]> allData2 = r2.readAll();
//        Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//        //System.out.println();
//        //System.out.println(Arrays.asList((allData2.get(1))));
//
//        Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
//
//        UpdateStorage u = new UpdateStorage();
//        u.planets.add(s1);
//        u.planets.add(s2);
//        u.planets.add(s3);
//
//        /*System.out.print("Planets Added: ");
//        for (Systems each : u.planets) {
//            System.out.print(each.getChild().getChild().getName() + "   ");
//        }*/
//
//        //System.out.println();
//        //System.out.print("Planet Conflicts: ");
//        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
//        planetConflicts = u.findPlanetConflicts();
//
//        for(int i = 0; i < planetConflicts.size(); i++){
//            for (int j = 0; j < planetConflicts.get(i).size(); j++){
//                assertEquals("HD 4308 b", planetConflicts.get(i).get(j).getChild().getChild().getName());
//            }
//        }
//    }
//
//    @Test
//    public void findStarConflictsTest() throws IOException, ReadCSV.MissingColumnNameException {
//        mapIndexes();
//        CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//        List<String[]> allData1 = r1.readAll();
//        Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
//        //System.out.println(Arrays.asList((allData1.get(678))));
//
//        CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//        List<String[]> allData2 = r2.readAll();
//        Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//        //System.out.println();
//        //System.out.println(Arrays.asList((allData2.get(1))));
//
//        Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
//
//        UpdateStorage u = new UpdateStorage();
//        u.planets.add(s1);
//        u.planets.add(s2);
//        u.planets.add(s3);
//
//        /*System.out.print("Planets Added: ");
//        for (Systems each : u.planets) {
//            System.out.print(each.getChild().getChild().getName() + "   ");
//        }*/
//
//        //System.out.println();
//        //System.out.print("Planet Conflicts: ");
//        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
//        planetConflicts = u.findPlanetConflicts();
//
//        for(int i = 0; i < planetConflicts.size(); i++){
//            for (int j = 0; j < planetConflicts.get(i).size(); j++){
//                assertEquals("HD 4308", planetConflicts.get(i).get(j).getChild().getName());
//            }
//        }
//    }
//
//    @Test
//    public void findSystemConflictsTest() throws IOException, ReadCSV.MissingColumnNameException {
//        mapIndexes();
//        CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//        List<String[]> allData1 = r1.readAll();
//        Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
//        //System.out.println(Arrays.asList((allData1.get(678))));
//
//        CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//        List<String[]> allData2 = r2.readAll();
//        Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//        //System.out.println();
//        //System.out.println(Arrays.asList((allData2.get(1))));
//
//        Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
//
//        UpdateStorage u = new UpdateStorage();
//        u.planets.add(s1);
//        u.planets.add(s2);
//        u.planets.add(s3);
//
//        /*System.out.print("Planets Added: ");
//        for (Systems each : u.planets) {
//            System.out.print(each.getChild().getChild().getName() + "   ");
//        }*/
//
//        //System.out.println();
//        //System.out.print("Planet Conflicts: ");
//        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
//        planetConflicts = u.findPlanetConflicts();
//
//        for(int i = 0; i < planetConflicts.size(); i++){
//            for (int j = 0; j < planetConflicts.get(i).size(); j++){
//                assertEquals("HD 4308", planetConflicts.get(i).get(j).getName());
//            }
//        }
//    }
//
//    @Test
//    public void NoConflictsTest() throws IOException, ReadCSV.MissingColumnNameException {
//        mapIndexes();
//        CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//        List<String[]> allData1 = r1.readAll();
//        Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(33)), ReadCSV.EU);
//        //System.out.println(Arrays.asList((allData1.get(678))));
//
//        CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//        List<String[]> allData2 = r2.readAll();
//        Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//        //System.out.println();
//        //System.out.println(Arrays.asList((allData2.get(1))));
//
//        Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
//
//        UpdateStorage u = new UpdateStorage();
//        u.planets.add(s1);
//        u.planets.add(s2);
//        u.planets.add(s3);
//
//        /*System.out.print("Planets Added: ");
//        for (Systems each : u.planets) {
//            System.out.print(each.getChild().getChild().getName() + "   ");
//        }*/
//
//        //System.out.println();
//        //System.out.print("Planet Conflicts: ");
//        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
//        planetConflicts = u.findPlanetConflicts();
//
//        int size;
//        for(int i = 0; i < planetConflicts.size(); i++){
//            size = planetConflicts.get(i).size();
//            System.out.println(size);
//            assertEquals(0, size,0);
//        }
//    }
//
//    @Test
//    public void findMultipleConflictsTest() throws IOException, ReadCSV.MissingColumnNameException {
//        mapIndexes();
//        CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
//        List<String[]> allData1 = r1.readAll();
//        Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);
//        //System.out.println(Arrays.asList((allData1.get(678))));
//
//        Systems s4 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(691)), ReadCSV.EU);
//
//
//        CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
//        List<String[]> allData2 = r2.readAll();
//        Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
//        //System.out.println();
//        //System.out.println(Arrays.asList((allData2.get(1))));
//
//        Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(2)), ReadCSV.NASA);
//
//        UpdateStorage u = new UpdateStorage();
//        u.planets.add(s1);
//        u.planets.add(s2);
//        u.planets.add(s3);
//        u.planets.add(s4);
//
//        /*System.out.print("Planets Added: ");
//        for (Systems each : u.planets) {
//            System.out.print(each.getChild().getChild().getName() + "   ");
//        }*/
//
//        //System.out.println();
//        //System.out.print("Planet Conflicts: ");
//        ArrayList<ArrayList<Systems>> planetConflicts = new ArrayList<ArrayList<Systems>>();
//        planetConflicts = u.findPlanetConflicts();
//
//        //for(int i = 0; i < planetConflicts.size(); i++){
//          //  assertEquals(planetConflicts.get(i).get(i).getName(), );
//        //}
//
//        assertEquals(planetConflicts.get(0).get(0).getName(), planetConflicts.get(1).get(0).getName());
//        assertEquals(planetConflicts.get(0).get(1).getName(), planetConflicts.get(1).get(1).getName());
//    }
}