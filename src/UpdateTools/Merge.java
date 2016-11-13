package UpdateTools;

import ModelStarSystems.*;
import com.opencsv.CSVReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rishi on 2016-11-12.
 * Specific attribute merges yet to be added
 */
public class Merge {



    public static void newSystem(Systems system, String xmlSystem){
        try {

            //parsing xmlSystem string into a document
            DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlSystem));
            Document doc = documentbuild.parse(is);

            //outputting to a new system file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
            transformer.transform(source, result);

        } catch (Exception e){

        }
    }

    public static void newStar(Systems system, String xmlStar){

        try {

            //system file directory
            File dir = new File(PullingTools.oecData + system.getName() + ".xml");

            //parsing xmlStar string into a document
            DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlStar));
            Document stardoc = documentbuild.parse(is);

            //parsing system file into a document
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document sysdoc = db.parse(dir);

            //Merging star document into system document
            Node sysroot = sysdoc.getDocumentElement(); // system document root element
            Node starroot = stardoc.getDocumentElement(); // star document root element
            Node imported = sysdoc.importNode(starroot,true); // import star root into system document
            sysroot.appendChild(imported); // append imported root under system document root element

            //Writing result file (Overwrites old file)
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(sysdoc);
            StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
            transformer.transform(source, result);

        } catch (Exception e){

        }


    }

    public static void newPlanet(Systems system, String xmlPlanet){

        try {

            //system file directory
            File dir = new File(PullingTools.oecData + system.getName() + ".xml");

            //parsing xmlPlanet string into a document
            DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlPlanet));
            Document planetdoc = documentbuild.parse(is);

            //parsing system file into a document
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document sysdoc = db.parse(dir);

            //merging planet document into system document
            Element syselem = sysdoc.getDocumentElement();
            NodeList starlist = syselem.getElementsByTagName("star");

            //for each star
            for(int i = 0; i < starlist.getLength(); i++){
                //check if starname is the same as planet's star name
                String starname = starlist.item(i).getFirstChild().getTextContent();
                if (starname.equals(system.getChild().getName())){
                    Node planetroot = planetdoc.getDocumentElement();
                    Node imported = sysdoc.importNode(planetroot,true);
                    starlist.item(i).appendChild(imported);
                }

            }

            //Writing result file (Overwrites old file)
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(sysdoc);
            StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
            transformer.transform(source, result);

        } catch (Exception e){

        }
    }

    /**
     * Warning! Running tests in your oecData path without providing a new system will overwrite old system files
     * You can run tests in other directories by changing PullingTools.oecData to "C:\\" or preferred directory
     * If running tests in C:\\ or different directory remember to RUN AS ADMINISTRATOR
     **/
    public static void main(String argv[]){
        /** Tests
         * If running test in oecData path, be sure to give ai new system and remove s
        try{

            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allData = r.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
            newPlanet(s, generateXML.xmlPlanet(s));
        } catch (Exception e){

        }
         **/
    }
}
