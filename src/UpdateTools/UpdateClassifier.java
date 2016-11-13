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
    public static UpdateStorage classify(UpdateStorage updatesFound) throws SystemFinder.MissingCelestialObjectException{
        for(Systems s: updatesFound.updates){
            //get planet and star objects along with their names for each system
            Star star = (Star) s.getChild();
            Planet planet = (Planet) star.getChild();
            String check1;
            String check2;
            if(!(SystemFinder.systemCheck(s))){
                //need to add entire system
                updatesFound.systems.add(s);
            }
            try{
                check1 = SystemFinder.getSystem(star);
            }catch (SystemFinder.MissingCelestialObjectException e){
                //if no name is retrieved from getSystem then we need to add star
                updatesFound.stars.add(s);
            }
            try{
                check2 = SystemFinder.getSystem(planet);
            }catch (SystemFinder.MissingCelestialObjectException e1){
                //if no name is retrieved from getSystem then we need to add star
                updatesFound.planets.add(s);
            }

        }
        //clear updates set so we know we already completed classifying these systems
        updatesFound.updates.clear();
        return updatesFound;
    }
    
}
