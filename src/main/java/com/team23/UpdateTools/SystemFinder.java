/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team23.UpdateTools;
import com.team23.ModelStarSystems.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author junil
 */
public class SystemFinder {
        public static String getSystem(CelestialObjects celesObj) throws MissingCelestialObjectException {
          //get name of planet or star
          String name = DifferenceDetector.onlyAlphaNumeric(celesObj.getName());
          String searchelm = "";
          String sysname = "";
          //identify the proper search element
          if(celesObj instanceof Planet){
              searchelm = "star/planet";
          }
          else if(celesObj instanceof Star){
              searchelm = "star";
          }
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder;
          try {
              builder = factory.newDocumentBuilder();
              Document doc = builder.parse(PullingTools.localOecFile);
              // Create XPathFactory object
              XPathFactory xpathFactory = XPathFactory.newInstance();
              // Create XPath object
              XPath xpath = xpathFactory.newXPath();
              //lowercase criteria that gets rid of spaces and special characters
              String lowerCase = "name[translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ+-~!@#$%^&*()_=,./:;[]* ','abcdefghijklmnopqrstuvwxyz')";
              XPathExpression expr = xpath.compile("//system["+searchelm+"/"+lowerCase+"='"+name+"']]/name[1]");
              //evaluate expression result on XML document
              NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
              for (int i = 0; i < nodes.getLength(); i++){
                  sysname = nodes.item(i).getTextContent();
              }
              //case: binary system
              if((DifferenceDetector.onlyAlphaNumeric(sysname)).equalsIgnoreCase("")){
                  expr = xpath.compile("//system[binary//"+searchelm+"/"+lowerCase+"='"+name+"']]/name[1]");
                  //evaluate expression result on XML document
                  nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                  for (int i = 0; i < nodes.getLength(); i++){
                      sysname = nodes.item(i).getTextContent();
                  }
              }

          } catch (ParserConfigurationException | IOException e) {
          }   catch (SAXException | XPathExpressionException ex) {
                  Logger.getLogger(SystemFinder.class.getName()).log(Level.SEVERE, null, ex);
              }
          //case: an unidentifiable planet or star
          if((DifferenceDetector.onlyAlphaNumeric(sysname)).equalsIgnoreCase("")){
              throw new SystemFinder.MissingCelestialObjectException(celesObj.getName());
              //return "Does Not Exist";
          }
          else{
              return sysname;
          }
        }

        public static boolean systemCheck(Systems sys){
            String name = DifferenceDetector.onlyAlphaNumeric(sys.getName());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            boolean exist = false;

            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(PullingTools.localOecFile);
                // Create XPathFactory object
                XPathFactory xpathFactory = XPathFactory.newInstance();
                // Create XPath object
                XPath xpath = xpathFactory.newXPath();
                String lowerCase = "text()[translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ+-~!@#$%^&*()_=,./:;[]* ','abcdefghijklmnopqrstuvwxyz')";
                XPathExpression expr = xpath.compile("//system/name["+lowerCase+"='" + name+"']]");
                //evaluate expression result on XML document
                NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                for (int i = 0; i < nodes.getLength(); i++){
                    //check if the system names match
                    if(sys.getName().equalsIgnoreCase(nodes.item(i).getTextContent())){
                       exist = true;
                    }
            }
            } catch (ParserConfigurationException | IOException e) {
        }   catch (SAXException | XPathExpressionException ex) {
                Logger.getLogger(SystemFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
            return exist;
        }
        /**
         * Thrown when a CelestialObject is not found in OEC
         */
        public static class MissingCelestialObjectException extends Exception {
          public MissingCelestialObjectException(String objName) {
            super(objName + "not in OEC database");
          }
        }

    public static void main(String[] args) throws SAXException, XPathExpressionException {

        try {
            ReadCSV.mapIndexes();
        } catch (IOException | ReadCSV.MissingColumnNameException ex) {
            Logger.getLogger(SystemFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            HashMap<String, String> updates = new HashMap<>();
            //normal planet and stars test
            //Planet p1 = new Planet("YBP1194 b",updates,ReadCSV.EU);
            HashMap<String, String> update2 = new HashMap<>();
            //Star s1 = new Star("nGC 2682 YBP 1514",update2,p1,ReadCSV.EU);
            //double binary planet and stars
            //Planet p1 = new Planet("HD 186427 B b",updates,ReadCSV.EU);
            //Star s1 = new Star("WDS J19418+5032 Aa",update2,p1,ReadCSV.EU);
            //single binary
            Planet p1 = new Planet("XO-2N c", updates, ReadCSV.EU);
            Star s1 = new Star("XO-2B", update2, p1, ReadCSV.EU);
            //planet and stars that dont exist
            //Planet p1 = new Planet("Abcdsfa",updates,ReadCSV.EU);
            //Star s1 = new Star("kasfi",update2,p1,ReadCSV.EU);
            HashMap<String, String> update3 = new HashMap<>();
            Systems sys = new Systems("16 cYgni", update3, s1, ReadCSV.EU);
            System.out.println(com.team23.UpdateTools.SystemFinder.getSystem(s1));
            //System.out.println(UpdateTools.SystemFinder.systemCheck(sys));
        } catch (SystemFinder.MissingCelestialObjectException e) {
            e.printStackTrace();
        }
    }
}
