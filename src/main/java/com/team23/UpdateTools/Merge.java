package com.team23.UpdateTools;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.SystemBuilder;
import com.team23.ModelStarSystems.Systems;

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
      //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);
      StringWriter writer = new StringWriter();
      //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, new StreamResult(writer));
      String output = writer.getBuffer().toString();
      String prettyOutput = generateXML.toPrettyString(output, 4);
      try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
        out.println( prettyOutput );
      }
      
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
      Element sysroot = sysdoc.getDocumentElement(); // system document root element

      NodeList childList = sysroot.getChildNodes();
      boolean binary = false;

      Node starroot = stardoc.getDocumentElement(); // star document root element
      Node imported = sysdoc.importNode(starroot, true); // import star root into system document
      //sysroot.appendChild(imported); // append imported root under system document root element

      for(int i = 0; i < childList.getLength(); i++){
        if (childList.item(i).getNodeName().equals("binary")){
          binary = true;
          sysroot.insertBefore(imported, childList.item(i));
          break;
        }
      }

      if (binary == false){
        for(int i = 0; i < childList.getLength(); i++){
          if(childList.item(i).getNodeName().equals("star")){
            sysroot.insertBefore(imported, childList.item(i));
          }
        }
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(sysdoc);
      StringWriter writer = new StringWriter();
      //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, new StreamResult(writer));
      String output = writer.getBuffer().toString();
      String prettyOutput = generateXML.toPrettyString(output, 4);
      try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
        out.println( prettyOutput );
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }

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
    //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    DOMSource source = new DOMSource(sysdoc);
    StringWriter writer = new StringWriter();
    //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
    transformer.transform(source, new StreamResult(writer));
    String output = writer.getBuffer().toString();
    String prettyOutput = generateXML.toPrettyString(output, 4);
    try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
      out.println( prettyOutput );
    }
    
  } catch (Exception e) {
    e.printStackTrace();
  }
}

  public static void newSystemVals(Systems system){
    /**
     * Modifies and merges specific values in system
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
          NodeList sysElemList = syselem.getElementsByTagName(key.replaceAll("_", ""));
          if (sysElemList.getLength() < 1) {
            Node imported = sysdoc.importNode(generateXML.xmlValue(key.replaceAll("_", ""), system.getProperties().get(key)), true);
            for(int node = 0; node < syselem.getChildNodes().getLength(); node++){
              if(!syselem.getChildNodes().item(node).getNodeName().equals("name")){
                syselem.insertBefore(imported, syselem.getChildNodes().item(node));
                break;
              }
            }
            syselem.insertBefore(imported, syselem.getElementsByTagName("name").item(0).getNextSibling());
            //inserted after name instead of before "rightascension"
          } else {
            sysElemList.item(0).setTextContent(system.getProperties().get(key));
          }

        }
      }

      //Writing result file (Overwrites old file)
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(sysdoc);
      StringWriter writer = new StringWriter();
      //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, new StreamResult(writer));
      String output = writer.getBuffer().toString();
      String prettyOutput = generateXML.toPrettyString(output, 4);
      try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
        out.println( prettyOutput );
      }


    } catch(Exception e){

    }
  }

  public static void newStarVals(Systems system){
    try {
      //System file directory
      File dir = new File(PullingTools.oecData + system.getName() + ".xml");

      //parsing system file into document
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document sysdoc = db.parse(dir);

      //get root system element from document
      Element syselem = sysdoc.getDocumentElement();

      //get starname of the star to change properties of
      String starName = system.getChild().getName();

      //get a list of stars from document
      NodeList starList = syselem.getElementsByTagName("star");

      //for each star in star list
      for(int i= 0; i < starList.getLength(); i++){
        Element star = (Element) starList.item(i); //current star
        NodeList starChildren = star.getChildNodes();
        //check if it is the star that requires changes in attributes

        int nameNodePlace = 0;
        for(int nameNode = 0; nameNode < starChildren.getLength(); nameNode++) {
          if(starChildren.item(nameNode).getNodeName().equals("name")){
            nameNodePlace = nameNode;
            break;
          }
        }
        System.out.println(starChildren.item(nameNodePlace).getTextContent());
        if(starChildren.item(nameNodePlace).getTextContent().equals(starName)){
          HashMap<String, String> starProps = system.getChild().getProperties();
          //if it is loop through properties and check which ones need to be changed (which ones are not null)

          for(String key: starProps.keySet()){
            if(starProps.get(key)!= null) {
              Node valueNode = null;
              for(int node = 0; node < starChildren.getLength(); node++){
                if (starChildren.item(node).getNodeName().equals(key.replaceAll("_", ""))){
                  valueNode = starChildren.item(node);
                  break;
                }
              }
              if (valueNode == null) {
                //if the attribute does not exist, add it in a new node
                Node imported = sysdoc.importNode(generateXML.xmlValue(key.replaceAll("_", ""), starProps.get(key)), true);

                for(int node = 0; node < starChildren.getLength(); node++){
                  if(!starChildren.item(node).getNodeName().equals("name")){
                    star.insertBefore(imported, starChildren.item(node));
                    break;
                  }
                }
              }
              else{
                //if it does exist change the text content to the new value
                valueNode.setTextContent(starProps.get(key));
              }
            }
          }
        }
      }

      //Writing result file (Overwrites old file)
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(sysdoc);
      StringWriter writer = new StringWriter();
      //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, new StreamResult(writer));
      String output = writer.getBuffer().toString();
      String prettyOutput = generateXML.toPrettyString(output, 4);
      try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
        out.println( prettyOutput );
      }

    } catch(Exception e){

    }
  }

  public static void newPlanetVals(Systems system){
    try {
      //System file directory
      File dir = new File(PullingTools.oecData + system.getName() + ".xml");

      //parsing system file into document
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document sysdoc = db.parse(dir);

      //get root system element from document
      Element syselem = sysdoc.getDocumentElement();

      //get planet properties and planet name
      String planetName = system.getChild().getChild().getName();
      HashMap<String,String> planetProps = system.getChild().getChild().getProperties();

      NodeList planetList = syselem.getElementsByTagName("planet");

      for(int i = 0; i < planetList.getLength(); i++){
        Element planet = (Element) planetList.item(i);
        if(planet.getElementsByTagName("name").item(0).getTextContent().equals(planetName)){
          for(String key : planetProps.keySet()){
            if(planetProps.get(key) != null){
              NodeList planetKeyList = planet.getElementsByTagName(key);
              if(planetKeyList.getLength() < 1){
                Node imported = sysdoc.importNode(generateXML.xmlValue(key, planetProps.get(key)), true);
                planet.appendChild(imported);
              }else{
                //if it does exist change the text content to the new value
                planetKeyList.item(0).setTextContent(planetProps.get(key));
              }
            }
          }
        }
      }

      //Writing result file (Overwrites old file)
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(sysdoc);
      StringWriter writer = new StringWriter();
      //StreamResult result = new StreamResult(new File(PullingTools.oecData + system.getName() + ".xml"));
      transformer.transform(source, new StreamResult(writer));
      String output = writer.getBuffer().toString();
      String prettyOutput = generateXML.toPrettyString(output, 4);
      try(  PrintWriter out = new PrintWriter(PullingTools.oecData + system.getName() + ".xml")  ){
        out.println( prettyOutput );
      }


    } catch(Exception e){

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
      s.setName("16 Cygni");
      s.getChild().setName("Leo");
      newStar(s, generateXML.xmlStar(s));
      //merge planet
      //s.getChild().setName("11 Com");
      //s.getChild().getChild().setName("Ace");
      //newPlanet(s, generateXML.xmlPlanet(s));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
