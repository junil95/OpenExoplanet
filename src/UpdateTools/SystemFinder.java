/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateTools;
import ModelStarSystems.*;
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

/**
 *
 * @author junil
 */
public class SystemFinder {
        public static String getSystem(CelestialObjects celesObj) throws SAXException, XPathExpressionException{
        //get name of planet or star
        String name = celesObj.getName();
        String searchelm = "";
        String sysname = "";
        //identify the proper search element
        if(celesObj instanceof Planet){
            searchelm = "star/planet";
            
        }
        else if(celesObj instanceof Star){
            searchelm = "star";
        }
        //System.out.println(searchelm);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        //Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PullingTools.localOecFile);

            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("//system["+searchelm+"/name='"+name+"']/name[1]");
            
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            //sysname = nodes.item(0).getTextContent();
            for (int i = 0; i < nodes.getLength(); i++){
                //System.out.println(nodes.item(i).getTextContent());
                sysname = nodes.item(i).getTextContent();
            }
            //case: binary system
            if((DifferenceDetector.onlyAlphaNumeric(sysname)).equalsIgnoreCase("")){
                expr = xpath.compile("//system[binary//"+searchelm+"/name='"+name+"']/name[1]");
                //System.out.println("//system[binary//"+searchelm+"/name='"+name+"']/name[1]");
                //evaluate expression result on XML document
                nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                //sysname = nodes.item(0).getTextContent();
                for (int i = 0; i < nodes.getLength(); i++){
                //System.out.println(nodes.item(i).getTextContent());
                    sysname = nodes.item(i).getTextContent();
                }
            }
            
        } catch (ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        //case: an unidentifiable planet or star
        if((DifferenceDetector.onlyAlphaNumeric(sysname)).equalsIgnoreCase("")){
            return "Does Not Exist";
        }
        else{
            return sysname;
        }
    }
      
}

