package com.team23.ModelStarSystems;

import java.util.HashMap;
import com.team23.UpdateTools.ReadCSV;


/**
 * Created by dhrumil on 23/10/16.
 */
public class Systems extends CelestialObjects {
  public Systems(String name, HashMap<String, String> properties, Star star, String source) {
    setSource(source);
    setName(name);
    star.setParent(this);
    setChild(star);
    setProperties(ReadCSV.getSystemLabels(), "sy_", properties);
    if (getSource().equals(ReadCSV.EU)) {
      getModifiableProperties().
              put("right_ascension", convertEuRaToOecFormat(getProperties().get("right_ascension")));
      getModifiableProperties().
              put("declination", convertEuRaToOecFormat(getProperties().get("declination")));
    }
  }
  
  /**
   * Convert ascension to hours minutes and seconds
   * @param asc
   * @return
   */
  public static String convertEuRaToOecFormat(String asc) {
    String tempra = asc;
    if (asc != null) {
      try {
        double ra = Double.parseDouble(asc);
        double hours = (ra / 360) * 4;
        double minutes = hours % 1 * 60;
        double seconds = minutes % 1 * 60;
        tempra = String.format("%.0f %.0f %.0f", hours, minutes, seconds);
      } catch (NumberFormatException e) {
      }
    }
    return tempra;
  }
  
  /**
   * Convert declination to deg mm ss
   * @param sdec
   */
  public static String convertEuDecToOecFormat(String sdec) {
    /**
     *tempdec = ""
     dec = float(p['dec'])
     tempdec += "%+.2i" %(dec)
     minutes = dec % 1 * 60
     tempdec += " %.2i" % (minutes)
     seconds = round(minutes % 1 * 60)
     tempdec+= " %.2i" % (seconds)
     */
    String tempdec = sdec;
    if (sdec != null) {
      try {
        double dec = Double.parseDouble(sdec);
        double minutes = Math.abs(dec % 1 * 60);
        double seconds = Math.abs(minutes % 1 * 60);
        tempdec = String.format("%+.0f %.0f %.0f", dec, minutes, seconds);
      } catch (NumberFormatException e) {
      }
    }
    return tempdec;
  }
  
  
  public static void main(String[] args) {
    System.out.println(convertEuDecToOecFormat("17.7927778"));
  }
  
}
