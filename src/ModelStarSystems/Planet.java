package ModelStarSystems;

import java.util.HashMap;
import java.util.HashSet;

import UpdateTools.ReadCSV;

/**
 * Created by dhrumil on 23/10/16.
 */
public class Planet extends CelestialObjects {
  /**
   * Create a planet
   *
   * @param properties Planet labels. Note, the planet labels need to start with pl
   */
  public Planet(String name, HashMap<String, String> properties, String source) {
    setSource(source);
    setName(name);
    setProperties(ReadCSV.getPlanetLabels(), "pl_", properties);

    /*if (getSource().equals(ReadCSV.NASA)) {
      getModifiableProperties().
              put("right_ascension", changePropertyName(getProperties().get("right_ascension")));
      getModifiableProperties().
              put("declination", convertEuRaToOecFormat(getProperties().get("declination")));
    }*/
  }

  public static void changePropertyName(HashMap<String, String> properties){
    String key = "detection_type";
    String val = properties.get(key);

    if(val != null) {
      try {//change value of that key
        if (val.equals("radial")) {
          properties.put(key, "RV");
        }
      } catch (NullPointerException e) {
      }
    }

    val = properties.get("updated").substring(2);
    char c;
    for(int i = 0; i < val.length(); i++){
      c = val.charAt(i);
      if(c == '-'){
        //change character at index i from '-' to '/'
        //use helper function
        val = charInPosition(i,'/',val);
      }
    }
    //add the changed value into the hashmap
    properties.put("updated", val);
  }

  /*
  Helper function to change character at a given index i in the string
  str. Returns the changed string.
   */
  public static String charInPosition(int position, char ch, String str){
    char[]  charArray = str.toCharArray();
    charArray[position] = ch;
    return new String(charArray);
  }



}