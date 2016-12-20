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


import static UpdateTools.UpdateStorage.findNewPlanetConflicts;
import static UpdateTools.UpdateStorage.findNewSystemConflicts;
import static org.junit.Assert.*;

/**
 * Created by Tirth Shah on 2016-11-14.
 */
public class UpdateStorageTest {


    private ArrayList<ArrayList<Systems>> planets;
    private ArrayList<ArrayList<Systems>> systems;


    @Before
    public void setUp() throws Exception {

        planets = new ArrayList<>();
        systems = new ArrayList<>();

    }

    @Test
    public void findNewPlanetConflictsTest() {
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

            System.out.println();
            System.out.print("Planet Conflicts: ");
            System.out.println(UpdateStorage.newPlanetConflicts.size());
            for (int i = 0; i < UpdateStorage.newPlanetConflicts.size(); i++) {
                System.out.println(UpdateStorage.newPlanetConflicts.get(i).get(0).getChild().getChild().getName() + "   ");
            }

        } catch (
                IOException e)

        {
            e.printStackTrace();
        } catch (
                ReadCSV.MissingColumnNameException e)

        {
            e.printStackTrace();
        } catch (
                ArrayIndexOutOfBoundsException e)

        {
            e.printStackTrace();
        }

    }


    @Test
    public void findNewSystemConflictsTest() {
        try {
            mapIndexes();
            CSVReader r1 = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allData1 = r1.readAll();
            Systems s1 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData1.get(678)), ReadCSV.EU);

            CSVReader r2 = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allData2 = r2.readAll();
            Systems s2 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(1)), ReadCSV.NASA);
            //System.out.println();
            //System.out.println(Arrays.asList((allData2.get(1))));
            s2.setName(s1.getName());
            Systems s3 = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData2.get(3)), ReadCSV.NASA);
            ArrayList<Systems> as = new ArrayList<>();
            as.add(s1);
            UpdateStorage.systems.add(as);
            as = new ArrayList<>();
            as.add(s2);
            UpdateStorage.systems.add(as);
            as = new ArrayList<>();
            as.add(s3);
            UpdateStorage.systems.add(as);

            System.out.print("Systems Added: ");
            findNewSystemConflicts();

            for (ArrayList<Systems> each : UpdateStorage.systems) {
                System.out.print(each.get(0).getName() + "   ");
            }

            System.out.println();
            System.out.print("System Conflicts: ");
            System.out.println(UpdateStorage.newSystemConflicts.size());
            for (int i = 0; i < UpdateStorage.newSystemConflicts.size(); i++) {
                System.out.println(UpdateStorage.newSystemConflicts.get(i).get(0).getName() + "   ");
            }

        } catch (
                IOException e)

        {
            e.printStackTrace();
        } catch (
                ReadCSV.MissingColumnNameException e)

        {
            e.printStackTrace();
        } catch (
                ArrayIndexOutOfBoundsException e)

        {
            e.printStackTrace();
        }

    }
}