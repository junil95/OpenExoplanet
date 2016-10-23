/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_dom_parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author junil
 */
public class Java_dom_parser {

    /**
     * @param planet
     * @return 
     */
    public static String getPlanetParentNode (String Planet){
        String pln_name = "";
        String sys_name = "";
        DocumentBuilderFactory factory;
        factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc;
            doc = builder.parse("systems.xml");
            NodeList systemlist;
            //store each system in a list
            systemlist = doc.getElementsByTagName("system");
            for(int i=0; i<systemlist.getLength();i++){
                Node system ;
                system = systemlist.item(i);
                String sys;
                sys = (String) system.getTextContent();
                //System.out.println("planet name" + p.getNodeType());
                if(system.getNodeType()== Node.ELEMENT_NODE){
                    Element elm;
                    elm = (Element) system;
                    NodeList planetlist;
                    //store each planet in a list for the systems
                    planetlist = elm.getElementsByTagName("planet");
                    for(int j=0; j<planetlist.getLength();j++){
                        Node pln;
                        pln = planetlist.item(j);
                        //String planet;
                        //planet = (String) pln.getParentNode().getTextContent();
                        //System.out.println("n" + name);
                        Element elm1;
                        elm1 = (Element) pln;
                        NodeList namelist;
                        //store each name of the planets in a list
                        namelist = elm1.getElementsByTagName("name");
                        for(int k=0; k<namelist.getLength();k++){
                            Node name;
                            name = namelist.item(k);
                            String p_name;
                            p_name = (String) name.getTextContent();
                            //System.out.println("name2 :" + pln);
                            //loop through the list until name matches the passed in planet name
                            if (p_name.equalsIgnoreCase(Planet)){
                                String[] lines = sys.split(System.getProperty("line.separator"));
                                sys_name = "System Name: "+ lines[1].trim();
                                pln_name = "Planet Name: "+ p_name.trim();  
                            }
                        }
                    }
                }
                
            }
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Java_dom_parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (sys_name +"\n" + pln_name + "\n");
    }
    
    public static String getStarParentNode (String star){
        String star_name = "";
        String sys_name = "";
        DocumentBuilderFactory factory;
        factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc;
            doc = builder.parse("systems.xml");
            NodeList systemlist;
            //store each system in a list
            systemlist = doc.getElementsByTagName("system");
            for(int i=0; i<systemlist.getLength();i++){
                Node system ;
                system = systemlist.item(i);
                String sys;
                sys = (String) system.getTextContent();
                //System.out.println("planet name" + p.getNodeType());
                if(system.getNodeType()== Node.ELEMENT_NODE){
                    Element elm;
                    elm = (Element) system;
                    NodeList starlist;
                    //store each star in a list for the systems
                    starlist = elm.getElementsByTagName("star");
                    for(int j=0; j<starlist.getLength();j++){
                        Node pln;
                        pln = starlist.item(j);
                        //String planet;
                        //planet = (String) pln.getParentNode().getTextContent();
                        //System.out.println("n" + name);
                        Element elm1;
                        elm1 = (Element) pln;
                        NodeList namelist;
                        //store each name of the stars in a list
                        namelist = elm1.getElementsByTagName("name");
                        for(int k=0; k<namelist.getLength();k++){
                            Node name;
                            name = namelist.item(k);
                            String s_name;
                            s_name = (String) name.getTextContent();
                            //System.out.println("name2 :" + pln);
                            //loop through the list until name matches the passed in star name
                            if (s_name.equalsIgnoreCase(star)){
                                String[] lines = sys.split(System.getProperty("line.separator"));
                                sys_name = "System Name: "+ lines[1].trim();
                                star_name = "Star Name: "+ s_name.trim();  
                            }
                        }
                    }
                }
                
            }
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Java_dom_parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (sys_name +"\n" + star_name + "\n");
    }

    public static void main(String[] args) {
        System.out.println(getPlanetParentNode("2MASS J07465196+3905404 b"));
        System.out.println(getStarParentNode("NGC 2682 YBP 1514"));
    }
    
}
