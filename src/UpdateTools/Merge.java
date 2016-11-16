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
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static UpdateTools.DifferenceDetector.onlyAlphaNumeric;

/**
 * @author Rishi A class used to create new systems, but also merge new stars, new planets or new
 *         values to the old system files
 */
public class Merge {
  
  public static void newSystem(Systems system, String xmlSystem) {
    /**
     * Creates a new system XML file
     * @param system The system object corresponding to the new system
     * @param xmlSystem The system string which will be parsed into the new system file
     */
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
      
    } catch (Exception e) {
      
    }
  }
  
  public static void newStar(Systems system, String xmlStar) {
    /**
     * Merges new star into existing system file
     * @param system The system object corresponding to the new star
     * @param xmlSystem The star string which will be merged into the existing system file
     */
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
      Node imported = sysdoc.importNode(starroot, true); // import star root into system document
      sysroot.appendChild(imported); // append imported root under system document root element
      
      //Writing result file (Overwrites old file)
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      DOMSource source = new DOMSource(sysdoc);
      StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, result);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
  
//  public static void newPlanet(Systems system, String xmlPlanet) {
//    /**
//     * Merges new planet into existing system file
//     * @param system The system object corresponding to the new planet
//     * @param xmlPlanet The planet string which will be merged into the existing system file
//     */
//    try {
//
//      //system file directory
//      File dir = new File(PullingTools.oecData + system.getName() + ".xml");
//
//      //parsing xmlPlanet string into a document
//      DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//      InputSource is = new InputSource();
//      is.setCharacterStream(new StringReader(xmlPlanet));
//      Document planetdoc = documentbuild.parse(is);
//
//      //parsing system file into a document
//      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//      Document sysdoc = db.parse(dir);
//
//      //merging planet document into system document
//      Element syselem = sysdoc.getDocumentElement();
//      NodeList starlist = syselem.getElementsByTagName("star");
//
//      //for each star
//      for (int i = 0; i < starlist.getLength(); i++) {
//        //check if starname is the same as planet's star name
//        String starname = starlist.item(i).getFirstChild().getTextContent();
//        System.out.println(starname);
//        if (starname.equals(system.getChild().getName())) {
//          Node planetroot = planetdoc.getDocumentElement();
//          Node imported = sysdoc.importNode(planetroot, true);
//          starlist.item(i).appendChild(imported);
//        }
//
//      }
//
//      //Writing result file (Overwrites old file)
//      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//      Transformer transformer = transformerFactory.newTransformer();
//      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//      DOMSource source = new DOMSource(sysdoc);
//
//      ///////////////
//      StringWriter writer = new StringWriter();
//      transformer.transform(source, new StreamResult(writer));
//      System.out.println(writer.getBuffer().toString());
//      /////////////////
//      StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
//      transformer.transform(source, result);
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
public static void newPlanet(Systems system, String xmlPlanet) {
  /**
   * Merges new planet into existing system file
   * @param system The system object corresponding to the new planet
   * @param xmlPlanet The planet string which will be merged into the existing system file
   */
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
    Element stElem;
    NodeList nameNl;
    
    boolean found = false;
    //for each star
    for (int i = 0; i < starlist.getLength(); i++) {
      //check if starname is the same as planet's star name
      stElem = (Element)starlist.item(i);
      nameNl = stElem.getElementsByTagName("name");
      for (int j = 0; j < nameNl.getLength(); j++) {
        //System.out.println(nameNl.item(j).getTextContent());
        if (DifferenceDetector.onlyAlphaNumeric(nameNl.item(j).getTextContent()).
                equals(DifferenceDetector.onlyAlphaNumeric(system.getChild().getName()))) {
          Node planetroot = planetdoc.getDocumentElement();
          Node imported = sysdoc.importNode(planetroot, true);
          stElem.appendChild(imported);
          found = true;
          break;
        }
      }
      
      //exit the second loop as well if we found the parent star we were looking for
      if (found) {
        break;
      }
      
      
    }
    
    //Writing result file (Overwrites old file)
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    DOMSource source = new DOMSource(sysdoc);
    
    ///////////////
//    StringWriter writer = new StringWriter();
//    transformer.transform(source, new StreamResult(writer));
//    System.out.println(writer.getBuffer().toString());
    /////////////////
    StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
    transformer.transform(source, result);
    
  } catch (Exception e) {
    e.printStackTrace();
  }
}
  
  public static void newValues(Systems system) {
    /**
     * Modifies and merges specific values in system, star, or planet properties
     * @param system The system object whose properties contain ONLY the values needed to be updated
     */
    try {
      
      //system file directory
      File dir = new File(PullingTools.oecData + system.getName() + ".xml");
      
      //parsing system file into a document
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document sysdoc = db.parse(dir);
      
      //checking and setting system values
      Element syselem = sysdoc.getDocumentElement();
      
      for (String key : system.getProperties().keySet()) {
        if (system.getProperties().get(key) != null) {
          NodeList sysElemList = syselem.getElementsByTagName(key);
          if (sysElemList.item(0) == null) {
            Node imported = sysdoc.importNode(generateXML.xmlValue(key, system.getProperties().get(key)), true);
            syselem.insertBefore(imported, syselem.getElementsByTagName("star").item(0));
          } else {
            sysElemList.item(0).setTextContent(system.getProperties().get(key));
          }
          
        }
      }
      NodeList starlist = syselem.getElementsByTagName("star");
      
      //for each star
      for (int i = 0; i < starlist.getLength(); i++) {
        //Find the correct star to put value under
        String starname = starlist.item(i).getFirstChild().getTextContent();
        if (starname.equals(system.getChild().getName())) {
          //correct star found
          Element corrstar = (Element) starlist.item(i);
          
          //check and set star values
          HashMap<String, String> starprop = system.getChild().getProperties();
          for (String key : starprop.keySet()) {
            if (starprop.get(key) != null) {
              NodeList starElemList = corrstar.getElementsByTagName(key);
              if (starElemList.item(0) == null) {
                Node imported = sysdoc.importNode(generateXML.xmlValue(key, starprop.get(key)), true);
                corrstar.insertBefore(imported, corrstar.getElementsByTagName("planet").item(0));
              } else {
                starElemList.item(0).setTextContent(starprop.get(key));
              }
            }
          }
          
          
          NodeList planetlist = corrstar.getElementsByTagName("planet");
          //for each planet
          for (int p = 0; p < planetlist.getLength(); p++) {
            String planetname = planetlist.item(p).getFirstChild().getTextContent();
            if (planetname.equals(system.getChild().getChild().getName())) {
              //correct planet found
              Element corrplanet = (Element) planetlist.item(i);
              
              //check and set planet values
              HashMap<String, String> planetprop = system.getChild().getChild().getProperties();
              for (String key : planetprop.keySet()) {
                if (planetprop.get(key) != null) {
                  NodeList planetElemList = corrplanet.getElementsByTagName(key);
                  if (planetElemList.item(0) == null) {
                    Node imported = sysdoc.importNode(generateXML.xmlValue(key, planetprop.get(key)), true);
                    corrplanet.appendChild(imported);
                  } else {
                    planetElemList.item(0).setTextContent(planetprop.get(key));
                  }
                }
              }
            }
            break; //break planet loop once the correct planet's values have been updated
          }
        }
        break; //break the star loop once the correct star's values have been updated
      }
      //Writing result file (Overwrites old file)
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      DOMSource source = new DOMSource(sysdoc);
      StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, result);
      
    } catch (Exception e) {
      
    }
  }
  
  /**
   * Warning! Running tests in your oecData path without providing a new system will overwrite old
   * system files You can run tests in other directories by changing PullingTools.oecData to "C:\\"
   * or preferred directory If running tests in C:\\ or different directory remember to RUN AS
   * ADMINISTRATOR
   **/
  public static void main(String argv[]) {
    /** Tests
     * If running test in oecData path, be sure to give ai new system and remove s
     **/
    try {
      
      ReadCSV.mapIndexes();
      CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
      List<String[]> allData = r.readAll();
      Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
      //merge star
//      s.setName("16 Cygni");
//      s.getChild().setName("Leo");
//      newStar(s, generateXML.xmlStar(s));
      //merge planet
      s.getChild().setName("11 Com");
      s.getChild().getChild().setName("Ace");
      newPlanet(s, generateXML.xmlPlanet(s));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
