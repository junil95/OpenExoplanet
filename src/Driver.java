import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import ModelStarSystems.Systems;

/**
 * Created by dhrumil on 06/11/16.
 *
 * This class will provide api methods for the front end
 */
public class Driver {
  public static String getNewPlanets() {
    String json = "";
    Gson gson = new Gson();
    
    return json;
  }
  
  private static HashMap convertToMap(Systems s) {
    HashMap<String, String> map = s.getProperties();
    Gson gson = new Gson();
    map.put("sy_name", s.getName());
    map.put("st_name", s.getChild().getName());
    map.put("pl_name", s.getChild().getChild().getName());
    map.put("src", s.getSource());
    map.putAll(s.getChild().getProperties());
    map.putAll(s.getChild().getChild().getProperties());
    return map;
  }
  
  public static void main(String[] args) {
    HashMap<String, String> hash = new HashMap<>();
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ArrayList<ArrayList<HashMap<String, String>>> big = new ArrayList<>();
    hash.put("Dhrumil", "hello");
    hash.put("Akash", "bye bye");
    list.add(hash);
    hash = new HashMap<>();
    hash.put("Yes", "hello");
    hash.put("PLZ", "bye bye");
    list.add(hash);
    big.add(list);
    String json = "";
    Gson gson = new Gson();
    System.out.println(gson.toJson(big));
  }
}
