/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;
import ModelStarSystems.*;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


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
            boolean added = false;
            System.out.println(SystemFinder.systemCheck(s));
            if(!(SystemFinder.systemCheck(s))){
                //need to add entire system
                updatesFound.systems.add(s);
                added = true;
            }
            if(!(added)){
                try{
                    check1 = SystemFinder.getSystem(star);
                }catch (SystemFinder.MissingCelestialObjectException e){
                    //if no name is retrieved from getSystem then we need to add star
                    updatesFound.stars.add(s);
                    added = true;
                }
            }
            if(!(added)){
                try{
                    check2 = SystemFinder.getSystem(planet);
                }catch (SystemFinder.MissingCelestialObjectException e1){
                    //if no name is retrieved from getSystem then we need to add star
                    updatesFound.planets.add(s);
                    //added = true;
                }
            }

            added = false;
        }
        //clear updates set so we know we already completed classifying these systems
        updatesFound.updates.clear();
        return updatesFound;
    }


    public static void main(String[] args) throws SAXException, XPathExpressionException {

        try {
            ReadCSV.mapIndexes();
        }catch (IOException | ReadCSV.MissingColumnNameException ex) {
            Logger.getLogger(SystemFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            HashMap <String,String> updates = new HashMap <>();
            //normal planet and stars test
            Planet p2 = new Planet("YBP1194 b",updates,ReadCSV.EU);
            HashMap <String,String> update2 = new HashMap <>();
            Star s2 = new Star("nGC 2682 YBP 1514",update2,p2,ReadCSV.EU);
            Systems sys2 = new Systems("YBP1514",update2,s2,ReadCSV.EU);
            //double binary planet and stars
            Planet p3 = new Planet("HD 186427 B b",updates,ReadCSV.EU);
            Star s3 = new Star("WDS J19418+5032 Aa",update2,p3,ReadCSV.EU);
            Systems sys3 = new Systems("16 Cygni",update2,s3,ReadCSV.EU);
            //single binary
            Planet p1 = new Planet("XO-2N c",updates,ReadCSV.EU);
            Star s1 = new Star("XO-2ac",update2,p1,ReadCSV.EU);
            Systems sys1 = new Systems("XO-2",update2,s1,ReadCSV.EU);
            //planet and stars that dont exist
            Planet p4 = new Planet("Abcdsfa",updates,ReadCSV.EU);
            Star s4 = new Star("kasfi",update2,p4,ReadCSV.EU);
            HashMap <String,String> update3 = new HashMap <>();
            Systems sys4 = new Systems("16 gni",update3,s4,ReadCSV.EU);
            //System.out.println(SystemFinder.getSystem(s1));
            //System.out.println(systemCheck(sys));
            UpdateStorage new_updates = new UpdateStorage();
            new_updates.updates.add(sys1);
            new_updates.updates.add(sys2);
            new_updates.updates.add(sys3);
            new_updates.updates.add(sys4);
            System.out.println("updates");
            for(Systems s : new_updates.updates){
                System.out.println(s.getName());

            }
            classify(new_updates);
            System.out.println("updates systems");
            for(Systems s : new_updates.systems){
                System.out.println(s.getName());

            }
            System.out.println("updates stars");
            for(Systems s : new_updates.stars){
                System.out.println(s.getChild().getName());

            }
            System.out.println("updates planets");
            for(Systems s : new_updates.planets){
                System.out.println(s.getChild().getChild().getName());

            }
            //System.out.println("planets"+ new_updates.planets);
            //System.out.println("stars" + new_updates.stars);
            //System.out.println("systems" + new_updates.systems);
            //System.out.println("updates" + new_updates.updates);

        }catch(SystemFinder.MissingCelestialObjectException e){
            e.printStackTrace();
        }

    }


}
