package TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import UpdateTools.DifferenceDetector;
import UpdateTools.UpdateStorage;

import static junit.framework.Assert.*;

/**
 * Created by dhrumil on 13/11/16.
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
    Set<String> planetNames = DifferenceDetector.getPlanetNamesOEC();
    //Some of the real and alternate planet names that should exist in oec
    
  }
  
}