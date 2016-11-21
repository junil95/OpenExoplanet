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
//  @Test
//  public void detectPlanetUpdatesEu() throws IOException, ReadCSV.MissingColumnNameException {
//    //create a temporary copy of eu database in the current package
//    String tmpFilePath = "tmp.csv";
//    String tmpFilePathOld = "tmp2.csv";
//    File tmp2 = new File(tmpFilePathOld);
//    File tmp = new File(tmpFilePath);
//    String fakePlanetNameSigChar = "mariol";
//    String fakePlanet = "Mario & L,19.4,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05," +
//            "0.231,0.005,0.005,,,,0.011664,2008,2015-08-21,94.8,1.5,1.5,2452899.6,1.6,1.6,,,,,,," +
//            ",,,,,,,,,,,,296.7,5.6,5.6,,,,,,,,Published in a refereed paper,Radial Velocity," +
//            ",,,,11 Com,185.1791667,17.7927778,4.74,,,,,110.6,10.5,10.5,-0.35,0.09,0.09,2.7,0.3," +
//            "0.3,19.0,2.0,2.0,G8 III,,,,4742.0,100.0,100.0,,,";
//    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp);
//    //Now add a fake planet to the end of the file
//    Writer output;
//    output = new BufferedWriter(new FileWriter(tmpFilePath, true));  //clears file every time
//    output.append(fakePlanet);
//    output.close();
//
//    //Create a temporary copy of the old version of the EU database
//    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp2);
//
//    //Now detect new planets
//    //Gets columns dynamically so need to map indexes
//    ReadCSV.mapIndexes();
//
//    UpdateStorage us = new UpdateStorage();
//
//    //Now create hashmaps of planets and their corresponding data
//    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.EU),
//            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.EU), us, ReadCSV.EU);
//
//    boolean detected = false;
//
//    for (Systems s : us.updates) {
//      if(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
//              equals(fakePlanetNameSigChar)){
//        detected = true;
//        break;
//      }
//    }
//    //Delete tmp files
//    tmp.delete();
//    tmp2.delete();
//    assertTrue(detected);
//  }
//
//
//  /**
//   * Test detecting new planet additions in between the newer and older versions of the NASA
//   * catalogue
//   * @throws IOException
//   */
//  @Test
//  public void detectPlanetUpdatesNASA() throws IOException, ReadCSV.MissingColumnNameException {
//    //create a temporary copy of eu database in the current package
//    String tmpFilePath = "tmp.csv";
//    String tmpFilePathOld = "tmp2.csv";
//    File tmp2 = new File(tmpFilePathOld);
//    File tmp = new File(tmpFilePath);
//    String fakePlanetNameSigChar = "mariol";
//    String fakePlanet = "269.496333,0.000000,17.96642222,0.00000000,17h57m59.12s,-30.715175," +
//            "0.000000,-30d42m54.6s,359.835949,0.000000,-3.213775,0.000000,269.563475,0.000000," +
//            "-7.276647,0.000000,1,,,,,0,0,810.00,100.00,-100.00,0,1,,,,,,,,,,,0,,,,,,0,,,,,," +
//            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," +
//            ",,,,,,,,,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,0,,,,,0,,,,,0,0,,,,,0,0,,,," +
//            ",0,0,,,,,,0,0,,,,,0,0,0.11,0.01,-0.01,0,0,1,,,,,0,0,,,,0,,0,,,,,,0,,,,,0,,,,,0,," +
//            ",,,0,0,0,0,0,0,3,0,,,,Mario &&& L,MOA 2010-BLG-328L,b,1,0,3,Microlensing," +
//            "2013,Furusawa et al. 2013,2013-12,MOA,1.8 m MOA Telescope,MOA CCD Array,Ground," +
//            "Furusawa et al. 2013,0,0,0,0,0,0,0,0,0,0," +
//            "http://exoplanet.eu/catalog/moa-2010-blg-328l_b/,,,,,,0,0.920000,0.160000," +
//            "-0.160000,0,1,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,,,,,0,0.02895,0.00692," +
//            "-0.00692,0,9.20000,2.20000,-2.20000,0,1,0.02895,0.00692,-0.00692,0,9.20000," +
//            "2.20000,-2.20000,0,1,Mass,,,,,,,,,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,," +
//            ",0,,,,,,0,,,,,0,,,,,0,,,,,0,3,0,2015-06-04";
//    FileUtils.copyFile(new File(PullingTools.localNasaArchive), tmp);
//    //Now add a fake planet to the end of the file
//    Writer output;
//    output = new BufferedWriter(new FileWriter(tmpFilePath, true));  //clears file every time
//    output.append(fakePlanet);
//    output.close();
//
//    //Create a temporary copy of the old version of the EU database
//    FileUtils.copyFile(new File(PullingTools.localNasaArchive), tmp2);
//
//    //Now detect new planets
//    //Gets columns dynamically so need to map indexes
//    ReadCSV.mapIndexes();
//
//    UpdateStorage us = new UpdateStorage();
//
//    //Now create hashmaps of planets and their corresponding data
//    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.NASA),
//            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.NASA), us, ReadCSV.NASA);
//
//    boolean detected = false;
//
//    for (Systems s : us.updates) {
//      if(DifferenceDetector.onlyAlphaNumeric(s.getChild().getChild().getName()).
//              equals(fakePlanetNameSigChar)){
//        detected = true;
//        break;
//      }
//    }
//    //Delete tmp files
//    tmp.delete();
//    tmp2.delete();
//    assertTrue(detected);
//  }
//
//  /**
//   * Detect updates to individual columns in the eu catalogue
//   * @throws IOException
//   * @throws ReadCSV.MissingColumnNameException
//   */
//
//  @Test
//  public void detectAttributeUpdatesEu() throws IOException, ReadCSV.MissingColumnNameException {
//    //create a temporary copy of eu database in the current package
//    String tmpFilePath = "tmp.csv";
//    String tmpFilePathOld = "tmp2.csv";
//    File tmp2 = new File(tmpFilePathOld);
//    File tmp = new File(tmpFilePath);
//    String fakePlanetNameSigChar = "mariol";
//    String fakePlanet = "Mario & L,19.4,1.5,1.5,19.4,1.5,1.5,777777,7777,,326.03,0.32,0.32,1.29,0.05,0.05," +
//            "0.231,0.005,0.005,,,,0.011664,2008,2015-08-21,94.8,1.5,1.5,2452899.6,1.6,1.6,,,,,,," +
//            ",,,,,,,,,,,,296.7,5.6,5.6,,,,,,,,Published in a refereed paper,Radial Velocity," +
//            ",,,,11 Com,185.1791667,17.7927778,4.74,,,,,110.6,10.5,10.5,-0.35,0.09,0.09,2.7,0.3," +
//            "0.3,19.0,2.0,2.0,G8 III,,,,4742.0,100.0,100.0,,,";
//    String fakePlanetOld = "Mario & L,9999,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05," +
//            "0.231,0.005,0.005,,,,0.011664,2008,2015-08-21,94.8,1.5,1.5,2452899.6,1.6,1.6,,,,,,," +
//            ",,,,,,,,,,,,296.7,5.6,5.6,,,,,,,,Published in a refereed paper,Radial Velocity," +
//            ",,,,11 Com,185.1791667,17.7927778,4.74,,,,,110.6,10.5,10.5,-0.35,0.09,0.09,2.7,0.3," +
//            "0.3,19.0,2.0,2.0,G8 III,,,,4742.0,100.0,100.0,,,";
//    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp);
//    //Now add a fake planet to the end of the file
//    Writer output;
//    output = new BufferedWriter(new FileWriter(tmpFilePath, true));
//    output.append(fakePlanet);
//    output.close();
//
//    //Create a temporary copy of the old version of the EU database
//    FileUtils.copyFile(new File(PullingTools.localExoplanetEu), tmp2);
//    output = new BufferedWriter(new FileWriter(tmpFilePathOld, true));
//    //Change the value in this file as well
//    output.append(fakePlanetOld);
//    output.close();
//
//    //Now detect changes in attributes
//    //Gets columns dynamically so need to map indexes
//    ReadCSV.mapIndexes();
//
//    UpdateStorage us = new UpdateStorage();
//
//    //Now create hashmaps of planets and their corresponding data
//    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.EU),
//            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.EU), us, ReadCSV.EU);
//
//    //Delete tmp files
//    tmp.delete();
//    tmp2.delete();
//
//    boolean detected = false;
//    //Test if the new value of the attribute is detected and stored
//    for (Systems s : us.newAttributes) {
//      System.out.println(s.getChild().getChild().getProperties());
//      if(s.getChild().getChild().getProperties().get("mass").equals("19.4")&&(s.getChild().getChild().getProperties().get("radius").
//              equals("777777"))){
//        detected = true;
//        break;
//      }
//    }
//    assertTrue(detected);
//
//    //Test if the old value of the attribute is stored
//    detected=false;
//    for (Systems s : us.oldAttributes) {
//      System.out.println(s.getChild().getChild().getProperties());
//      if(s.getChild().getChild().getProperties().get("mass").equals("19.4")&&(s.getChild().getChild().getProperties().get("radius").
//              equals(null))){
//        detected = true;
//        break;
//      }
//    }
//    assertTrue(detected);
//
//  }
//
//  /**
//   * Detect updates to individual columns in the nasa catalogue
//   * @throws IOException
//   * @throws ReadCSV.MissingColumnNameException
//   */
//
//  @Test
//  public void detectAttributeUpdatesNASA() throws IOException, ReadCSV.MissingColumnNameException {
//    //create a temporary copy of eu database in the current package
//    String tmpFilePath = "tmp.csv";
//    String tmpFilePathOld = "tmp2.csv";
//    File tmp2 = new File(tmpFilePathOld);
//    File tmp = new File(tmpFilePath);
//    String fakePlanetNameSigChar = "mariol";
//    String fakePlanet = "269.496333,0.000000,17.96642222,0.00000000,99999,-30.715175," +
//            "0.000000,-30d42m54.6s,359.835949,0.000000,-3.213775,0.000000,269.563475,0.000000," +
//            "-7.276647,0.000000,1,,,,,0,0,810.00,100.00,-100.00,0,1,,,,,,,,,,,0,,,,,,0,,,,,," +
//            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," +
//            ",,,,,,,,,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,0,,,,,0,,,,,0,0,,,,,0,0,,,," +
//            ",0,0,,,,,,0,0,,,,,0,0,0.11,0.01,-0.01,0,0,1,,,,,0,0,,,,0,,0,,,,,,0,,,,,0,,,,,0,," +
//            ",,,0,0,0,0,0,0,3,0,,,,Mario &&& L,MOA 2010-BLG-328L,b,1,0,3,Microlensing," +
//            "2013,Furusawa et al. 2013,2013-12,MOA,1.8 m MOA Telescope,MOA CCD Array,Ground," +
//            "Furusawa et al. 2013,0,0,0,0,0,0,0,0,0,0," +
//            "http://exoplanet.eu/catalog/moa-2010-blg-328l_b/,,,,,,0,0.920000,0.160000," +
//            "-0.160000,0,1,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,,,,,0,0.02895,0.00692," +
//            "-0.00692,0,9.20000,2.20000,-2.20000,0,1,0.02895,0.00692,-0.00692,0,9.20000," +
//            "2.20000,-2.20000,0,1,Mass,,,,,,,,,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,," +
//            ",0,,,,,,0,,,,,0,,,,,0,,,,,0,3,0,2015-06-04";
//    String fakePlanetOld = "269.496333,0.000000,17.96642222,0.00000000,17h57m59.12s,-30.715175," +
//            "0.000000,-30d42m54.6s,359.835949,0.000000,-3.213775,0.000000,269.563475,0.000000," +
//            "-7.276647,0.000000,1,,,,,0,0,810.00,100.00,-100.00,0,1,,,,,,,,,,,0,,,,,,0,,,,,," +
//            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," +
//            ",,,,,,,,,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,0,,,,,0,,,,,0,0,,,,,0,0,,,," +
//            ",0,0,,,,,,0,0,,,,,0,0,0.11,0.01,-0.01,0,0,1,,,,,0,0,,,,0,,0,,,,,,0,,,,,0,,,,,0,," +
//            ",,,0,0,0,0,0,0,3,0,,,,Mario &&& L,MOA 2010-BLG-328L,b,1,0,3,Microlensing," +
//            "2013,Furusawa et al. 2013,2013-12,MOA,1.8 m MOA Telescope,MOA CCD Array,Ground," +
//            "Furusawa et al. 2013,0,0,0,0,0,0,0,0,0,0," +
//            "http://exoplanet.eu/catalog/moa-2010-blg-328l_b/,,,,,,0,0.920000,0.160000," +
//            "-0.160000,0,1,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,,,,,0,0.02895,0.00692," +
//            "-0.00692,0,9.20000,2.20000,-2.20000,0,1,0.02895,0.00692,-0.00692,0,9.20000," +
//            "2.20000,-2.20000,0,1,Mass,,,,,,,,,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,,,0,,,," +
//            ",0,,,,,,0,,,,,0,,,,,0,,,,,0,3,0,2015-06-04";
//    FileUtils.copyFile(new File(PullingTools.localNasaArchive), tmp);
//    //Now add a fake planet to the end of the file
//    Writer output;
//    output = new BufferedWriter(new FileWriter(tmpFilePath, true));
//    output.append(fakePlanet);
//    output.close();
//
//    //Create a temporary copy of the old version of the EU database
//    FileUtils.copyFile(new File(PullingTools.localNasaArchive), tmp2);
//    output = new BufferedWriter(new FileWriter(tmpFilePathOld, true));
//    //Change the value in this file as well
//    output.append(fakePlanetOld);
//    output.close();
//
//    //Now detect changes in attributes
//    //Gets columns dynamically so need to map indexes
//    ReadCSV.mapIndexes();
//
//    UpdateStorage us = new UpdateStorage();
//
//    //Now create hashmaps of planets and their corresponding data
//    us = DetectUpdates.detectUpdates(ReadCSV.mapPlanetToData(tmpFilePathOld, ReadCSV.NASA),
//            ReadCSV.mapPlanetToData(tmpFilePath, ReadCSV.NASA), us, ReadCSV.NASA);
//
//    //Delete tmp files
//    tmp.delete();
//    tmp2.delete();
//
//    boolean detected = false;
//    //Test if the new value of the attribute is detected and stored
//    for (Systems s : us.newAttributes) {
//      System.out.println(s.getChild().getChild().getProperties());
//      if(s.getProperties().get("right_ascension").equals("99999")){
//        detected = true;
//        break;
//      }
//    }
//    assertTrue(detected);
//
//    //Test if the old value of the attribute is stored
//    detected=false;
//    for (Systems s : us.oldAttributes) {
//      System.out.println(s.getChild().getChild().getProperties());
//      if(s.getProperties().get("right_ascension").equals("17h57m59.12s")){
//        detected = true;
//        break;
//      }
//    }
//    assertTrue(detected);
//
//  }
}