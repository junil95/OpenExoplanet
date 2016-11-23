package com.team23.TestSuite;

import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.SystemBuilder;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.team23.ModelStarSystems.Systems;
import com.team23.UpdateTools.PullingTools;
import com.team23.UpdateTools.ReadCSV;
import static org.junit.Assert.*;

/**
 * Created by dhrumil on 14/11/16.
 * Test Bulding systems
 */

public class SystemBuilderTest {
  /**
   * Test building system using Eu database csv row
   */
  @Test
  public void testBuildingSystemCsvRowEu() throws IOException, ReadCSV.MissingColumnNameException {
    //Create index mappings
    ReadCSV.mapIndexes();
    CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
    List<String[]> allData = r.readAll();
    Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
    assertEquals(s.getName(), "11 Com");
    assertEquals(s.getChild().getName(), "11 Com");
    assertEquals(s.getChild().getChild().getName(), "11 Com b");
  }
  
  /**
   * Test building system using NASA database csv row
   */
  @Test
  public void testBuildingSystemCsvRowNASA() throws IOException, ReadCSV.MissingColumnNameException {
    //Create index mappings
    ReadCSV.mapIndexes();
    CSVReader r = new CSVReader(new FileReader(PullingTools.localNasaArchive));
    List<String[]> allData = r.readAll();
    Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.NASA);
    assertEquals(s.getName(), "HD 4308");
    assertEquals(s.getChild().getName(), "HD 4308");
    assertEquals(s.getChild().getChild().getName(), "HD 4308 b");
  }
  
  /**
   * Test building system using Hashmap
   */
  @Test
  public void testBuildingSystemUsingHashMap() throws IOException,
          ReadCSV.MissingColumnNameException, SystemBuilder.MissingCelestialObjectNameException {
    //Create index mappings
    ReadCSV.mapIndexes();
    ReadCSV.mapPlanetToData(PullingTools.localExoplanetEu, ReadCSV.EU).get("11 Com b");
    Systems s = SystemBuilder.buildSystemWithHashMap(ReadCSV.mapPlanetToData(
            PullingTools.localExoplanetEu, ReadCSV.EU).get("11 Com b"), ReadCSV.EU);
    assertEquals(s.getName(), "11 Com");
    assertEquals(s.getChild().getName(), "11 Com");
    assertEquals(s.getChild().getChild().getName(), "11 Com b");
  }
}