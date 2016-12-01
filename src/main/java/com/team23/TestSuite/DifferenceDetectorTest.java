package com.team23.TestSuite;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.team23.ModelStarSystems.Systems;
import com.team23.UpdateTools.DifferenceDetector;
import com.team23.UpdateTools.PullingTools;
import com.team23.UpdateTools.ReadCSV;
import com.team23.UpdateTools.UpdateStorage;

/**
 * Created by dhrumil on 13/11/16.
 * TestSuite for the DifferenceDetector class
 */
public class DifferenceDetectorTest {
  
  /**
   * Check if this function removes spaces
   */
  @Test
  public void testRemovingSpacesOnlyAlphanumeric(){
    String input = "tes t string";
    String expected = "teststring";
    assertEquals(expected, DifferenceDetector.onlyAlphaNumeric(input));
  }
  
  /**
   * Check if this function removes trash characters
   */
  @Test
  public void testRemovingRandomCharacters(){
    String input = "tes@ t# string";
    String expected = "teststring";
    assertEquals(expected, DifferenceDetector.onlyAlphaNumeric(input));
  }
  
  /**
   * Check if this function turns everything to lowercase
   */
  @Test
  public void testLowerCaseAndAlphanumeric(){
    String input = "tes@ t# Str@Ing";
    String expected = "teststring";
    assertEquals(expected, DifferenceDetector.onlyAlphaNumeric(input));
  }
  
  /**
   * Test retrieving a list of the existing planet names in oec. Note the retrieved names
   * will be alphanumeric and lowercase
   * Note this will require oec.xml file to be inside the oec folder in data
   */
  @Test
  public void testRetrievingPlanetNamesOec(){
    Set<String> planetNames = DifferenceDetector.getNamesOEC();
    //Some of the real and alternate planet names that should exist in oec
    Set<String> existingPlanetNames = new HashSet<>();
    existingPlanetNames.add("11comb");
    existingPlanetNames.add("11umib");
    //alternate name
    existingPlanetNames.add("11ursaeminorisb");
    existingPlanetNames.add("hd221345b");
    existingPlanetNames.add("gj614c");
    for (String s : existingPlanetNames) {
      assertTrue(planetNames.contains(s));
    }
    //also make sure that a planet that doesn't exist is not detected
    assertFalse(planetNames.contains("yeayeayea"));
  }
  
  /**
   * Test detecting new planets that don't exist during the initial merge. Requires
   * exoplanetEu and oec.xml to be in the data folder
   */
  @Test
  public void testDetectingNewPlanetsDuringInitialMergeEU() throws IOException, ReadCSV.MissingColumnNameException {
    //create a temperory copy of eu database in the current package
    String tmpFilePath = "tmp.csv";
    File tmp = new File(tmpFilePath);
    String fakePlanetNameSigChar = "mariol";
    String fakePlanet = "Mario & L,19.4,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05," +
            "0.231,0.005,0.005,,,,0.011664,2008,2015-08-21,94.8,1.5,1.5,2452899.6,1.6,1.6,,,,,,," +
            ",,,,,,,,,,,,296.7,5.6,5.6,,,,,,,,Published in a refereed paper,Radial Velocity," +
            ",,,,11 Com,185.1791667,17.7927778,4.74,,,,,110.6,10.5,10.5,-0.35,0.09,0.09,2.7,0.3," +
            "0.3,19.0,2.0,2.0,G8 III,,,,4742.0,100.0,100.0,,,";
    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp);
    //Now add a fake planet to the end of the file
    Writer output;
    output = new BufferedWriter(new FileWriter(tmpFilePath, true));  //clears file every time
    output.append(fakePlanet);
    output.close();
    
    //Now detect new planets
    //Gets columns dynamically so need to map indexes
    ReadCSV.mapIndexes();
    DifferenceDetector.getNewPlanetIDs(tmpFilePath, ReadCSV.EU);
    boolean found = false;
    //Check if the fake planet was detected as a new planet
    for (Systems s : UpdateStorage.updates) {
      if (DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
              equals(fakePlanetNameSigChar)) {
        found = true;
        break;
      }
    }
    //Clean up, delete the tmp file
    tmp.delete();
    assertTrue(found);
  }
  
  /**
   * Test detecting new planets that don't exist during the initial merge. Requires
   * NASA archives and oec.xml to be in the data folder
   */
  @Test
  public void testDetectingNewPlanetsDuringInitialMergeNASA() throws IOException, ReadCSV.MissingColumnNameException {
    //create a temperory copy of eu database in the current package
    String tmpFilePath = "tmp.csv";
    File tmp = new File(tmpFilePath);
    String fakePlanetNameSigChar = "mariol";
    String fakePlanet = "269.496333,0.000000,17.96642222,0.00000000,17h57m59.12s,-30.715175," +
            "0.000000,-30d42m54.6s,359.835949,0.000000,-3.213775,0.000000,269.563475,0.000000," +
            "-7.276647,0.000000,1,,,,,0,0,810.00,100.00,-100.00,0,1,,,,,,,,,,,0,,,,,,0,,,,,," +
            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," +
            ",,,,,,,,,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,0,,,,,0,,,,,0,0,,,,,0,0,,,," +
            ",0,0,,,,,,0,0,,,,,0,0,0.11,0.01,-0.01,0,0,1,,,,,0,0,,,,0,,0,,,,,,0,,,,,0,,,,,0,," +
            ",,,0,0,0,0,0,0,3,0,,,,Mario &&& L,MOA 2010-BLG-328L,b,1,0,3,Microlensing," +
            "2013,Furusawa et al. 2013,2013-12,MOA,1.8 m MOA Telescope,MOA CCD Array,Ground," +
            "Furusawa et al. 2013,0,0,0,0,0,0,0,0,0,0," +
            "http://exoplanet.eu/catalog/moa-2010-blg-328l_b/,,,,,,0,0.920000,0.160000," +
            "-0.160000,0,1,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,,,,,0,0.02895,0.00692," +
            "-0.00692,0,9.20000,2.20000,-2.20000,0,1,0.02895,0.00692,-0.00692,0,9.20000," +
            "2.20000,-2.20000,0,1,Mass,,,,,,,,,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,," +
            ",0,,,,,,0,,,,,0,,,,,0,,,,,0,3,0,2015-06-04";
    FileUtils.copyFile(new File(PullingTools.localNasaArchive), tmp);
    //Now add a fake planet to the end of the file
    Writer output;
    output = new BufferedWriter(new FileWriter(tmpFilePath, true));  //clears file every time
    output.append(fakePlanet);
    output.close();
    
    //Now detect new planets
    //Gets columns dynamically so need to map indexes
    ReadCSV.mapIndexes();
    DifferenceDetector.getNewPlanetIDs(tmpFilePath, ReadCSV.NASA);
    boolean found = false;
    //Check if the fake planet was detected as a new planet
    for (Systems s : UpdateStorage.updates) {
      if (DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
              equals(fakePlanetNameSigChar)) {
        found = true;
        break;
      }
    }
    //Clean up, delete the tmp file
    tmp.delete();
    assertTrue(found);
  }
}
