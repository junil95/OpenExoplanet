package UpdateTools;
import java.util.*;

/*
 * Created custom data type to help design and read implementation of 
 * updatedStorage class. The data type works very similar to tuple. It has
 * has parts where the first part stores a boolean where true indicates 
 * a duplicate given System object occurs in a given set and false otherwise.
 * The second component is an integer where if the duplicate occurs, it shows 
 * the index of the 2nd duplicate object.
 */
public class Pair{
  boolean duplicateOrNot;
  int index;
  
  //Constructor 
  Pair(boolean dup, int i){
    this.duplicateOrNot = dup;
    this.index = i;
  }
}