package TestSuite;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import ModelStarSystems.Systems;
import UpdateTools.DetectUpdates;
import UpdateTools.DifferenceDetector;
import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import UpdateTools.UpdateStorage;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by dhrumil on 13/11/16.
 * TestSuite for DetectUpdates class
 */
public class DetectUpdatesTest {
  /**
   * Test detecting new planet additions in between the newer and older versions of the EU catalogue
   * @throws IOException
   */
  @Test
  public void detectPlanetUpdatesEu() throws IOException, ReadCSV.MissingColumnNameException {
    //create a temporary copy of eu database in the current package
    String tmpFilePath = "tmp.csv";
    String tmpFilePathOld = "tmp2.csv";
    File tmp2 = new File(tmpFilePathOld);
    File tmp = new File(tmpFilePath);
    String fakePlanetNameSigChar = "mariol";
    String fakePlanet = "\nMario & L,19.4,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05," +
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
    
    //Create a temporary copy of the old version of the EU database
    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp2);
  
    //Now detect new planets
    //Gets columns dynamically so need to map indexes
    ReadCSV.mapIndexes();
  
    UpdateStorage us = new UpdateStorage();
    
    //Now create hashmaps of planets and their corresponding data
    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.EU),
            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.EU), us, ReadCSV.EU);
    
    boolean detected = false;
  
    for (Systems s : us.updates) {
      System.out.println(s.getChild().getChild().getName());
      if(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
              equals(fakePlanetNameSigChar)){
        detected = true;
        break;
      }
    }
    //Delete tmp files
    tmp.delete();
    tmp2.delete();
    assertTrue(detected);
  }

  
  /**
   * Test detecting new planet additions in between the newer and older versions of the NASA
   * catalogue
   * @throws IOException
   */
  @Test
  public void detectPlanetUpdatesNASA() throws IOException, ReadCSV.MissingColumnNameException {
    //create a temporary copy of eu database in the current package
    String tmpFilePath = "tmp.csv";
    String tmpFilePathOld = "tmp2.csv";
    File tmp2 = new File(tmpFilePathOld);
    File tmp = new File(tmpFilePath);
    String fakePlanetNameSigChar = "mariol";
    String fakePlanet = "\nMario & L,19.4,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05," +
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
    
    //Create a temporary copy of the old version of the EU database
    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp2);
    
    //Now detect new planets
    //Gets columns dynamically so need to map indexes
    ReadCSV.mapIndexes();
    
    UpdateStorage us = new UpdateStorage();
    
    //Now create hashmaps of planets and their corresponding data
    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.EU),
            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.EU), us, ReadCSV.EU);
    
    boolean detected = false;

    for (Systems s : us.updates) {
      System.out.println(s.getChild().getChild().getName());
      if(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
              equals(fakePlanetNameSigChar)){
        detected = true;
        break;
      }
    }
    //Delete tmp files
    tmp.delete();
    tmp2.delete();
    assertTrue(detected);
  }
}