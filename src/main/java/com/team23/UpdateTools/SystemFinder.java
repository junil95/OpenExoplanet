/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team23.UpdateTools;
import java.io.IOException;
import java.util.HashMap;
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

import com.team23.ModelStarSystems.CelestialObjects;
import com.team23.ModelStarSystems.Planet;
import com.team23.ModelStarSystems.Star;
import com.team23.ModelStarSystems.Systems;

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

}
