package ModelStarSystems;

import java.util.HashMap;

import UpdateTools.ReadCSV;


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

    //EU
    if (getSource().equals(ReadCSV.EU)) {
      getModifiableProperties().
              put("right_ascension", convertEuRaToOecFormat(getProperties().get("right_ascension")));
      getModifiableProperties().
              put("declination", convertEuDecToOecFormat(getProperties().get("declination")));
    }
    //NASA
    else{
      getModifiableProperties().put("ra_str", convertNASAraStartoOecFormat(getProperties().get("ra_str")));
      getModifiableProperties().put("dec_str", convertNASAdecStrtoOecFormat(getProperties().get("dec_str")));
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

  public static String convertNASAraStartoOecFormat(String ra_str) {
    String tempra = ra_str;

    if (ra_str != null) {
      try {
        String splitHourInfo[] = ra_str.split("h");
        double hours = Double.parseDouble(splitHourInfo[0]);
        String splitMinInfo[] = splitHourInfo[1].split("m");
        double minutes = Double.parseDouble(splitMinInfo[0]);
        String splitSecInfo[] = splitMinInfo[1].split("s");
        double seconds = Double.parseDouble(splitSecInfo[0]);

        tempra = String.format("%.0f %.0f %.0f", hours, minutes, seconds);

      } catch (NumberFormatException e) {
      }
    }
    return tempra;
  }

  public static String convertNASAdecStrtoOecFormat(String dec_str) {
    String tempra = dec_str;

    if (dec_str != null) {
      try {
        String splitHourInfo[] = dec_str.split("d");
        double hours = Double.parseDouble(splitHourInfo[0]);
        String splitMinInfo[] = splitHourInfo[1].split("m");
        double minutes = Double.parseDouble(splitMinInfo[0]);
        String splitSecInfo[] = splitMinInfo[1].split("s");
        double seconds = Double.parseDouble(splitSecInfo[0]);

        tempra = String.format("%.0f %.0f %.0f", hours, minutes, seconds);

      } catch (NullPointerException e) {
      }
    }
    return tempra;
  }




  public static void main(String[] args) {

    System.out.println(convertEuDecToOecFormat("17.7927778"));
    System.out.println(convertEuRaToOecFormat("1.248493"));
    System.out.println(convertNASAdecStrtoOecFormat("-65d38m58.3s"));
    System.out.println(convertNASAraStartoOecFormat("00h44m39.27s"));
  }

}