/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;
import ModelStarSystems.*;


/**
 *
 * @author junil
 */
public class UpdateClassifier {
    public static void classify() throws SystemFinder.MissingCelestialObjectException{
        for(Systems s: UpdateStorage.updates){
            //get planet and star objects along with their names for each system
            String systemName = s.getName();
            Star star = (Star) s.getChild();
            String starName = star.getName();
            Planet planet = (Planet) star.getChild();
            String planetName = planet.getName();
            if(!(SystemFinder.systemCheck(s))){
                //need to add entire system
                UpdateStorage.systems.add(s);
            }
            else if(SystemFinder.getSystem(star).equalsIgnoreCase("Does Not Exist")){
                //need to add star
                UpdateStorage.stars.add(s);
            }
            else if(SystemFinder.getSystem(planet).equalsIgnoreCase("Does Not Exist")){
                //add planets
                UpdateStorage.planets.add(s);
            }

        }
    }

}
