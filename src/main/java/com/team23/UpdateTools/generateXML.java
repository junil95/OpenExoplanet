package com.team23.UpdateTools;


import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.CelestialObjects;
import com.team23.ModelStarSystems.SystemBuilder;
import com.team23.ModelStarSystems.Systems;
import com.team23.UpdateTools.ReadCSV.MissingColumnNameException;

/**
 * @author Rishi A class used to generate XML strings for planets, stars or systems.
 * 				 This class also generates XML elements for specific system/star/planet properties.
 */

public class generateXML {
	public static String xmlPlanet(Systems system){
		/**
		 * Takes a system object and returns an XML String for planet
		 *
		 * @param system Is a system object for which the planet has to be converted to an XML string
		 * @return The XML String for planet
		 */
		CelestialObjects planet = system.getChild().getChild(); //planet
		HashMap<String, String> prop = planet.getProperties(); //planet properties
		
		List<String> keyList = new ArrayList<String>(prop.keySet());
		java.util.Collections.sort(keyList);

		//Making planet XML file
		try {
			 DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = df.newDocumentBuilder();

		     // planet and name
		     Document document = db.newDocument();
		     Element rootElement = document.createElement("planet");
		     document.appendChild(rootElement);
		     
		     Element name = document.createElement("name");
		     name.appendChild(document.createTextNode(planet.getName()));
		     rootElement.appendChild(name);

		     for (String key : keyList) {
                 if ((prop.get(key) != null) && !key.contains("error")) {
                     Element elem = document.createElement(key.replaceAll("_", ""));

                     if (prop.containsKey(key + "_error_min") && prop.containsKey(key + "_error_max")) {
						 if(prop.get(key+"_error_min")!= null && prop.get(key+"_error_max")!=null) {
							 Attr attr = document.createAttribute("errorminus");
							 attr.setValue(prop.get(key + "_error_min"));
							 elem.setAttributeNode(attr);
							 Attr attr2 = document.createAttribute("errorplus");
							 attr2.setValue(prop.get(key + "_error_max"));
							 elem.setAttributeNode(attr2);
						 }
                     }
                     elem.appendChild(document.createTextNode(prop.get(key)));
                     rootElement.appendChild(elem);
                 }
             }

		     //write to XML String
			 TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 Transformer transformer = transformerFactory.newTransformer();
             transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			 //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			 //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");
		     DOMSource source = new DOMSource(document);
		     StringWriter writer = new StringWriter();
		     transformer.transform(source, new StreamResult(writer));
		     String output = writer.getBuffer().toString();
			String prettyOutput = toPrettyString(output, 4);
			return prettyOutput;

			} catch (Exception e){

			}
		return null;

	}
	public static String xmlStar(Systems system){
		/**
		 * Takes a system object and returns an XML String for star
		 * @param system Is a system object for which the star and planet have to be converted to XML string
		 * @return The XML String for star including the planet under it
		 */
		CelestialObjects star = system.getChild();
		HashMap<String, String> prop = star.getProperties();
		
		List<String> keyList = new ArrayList<String>(prop.keySet());
		java.util.Collections.sort(keyList);

		try {
			 DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = df.newDocumentBuilder();

		     // star and name
		     Document document = db.newDocument();
		     Element rootElement = document.createElement("star");
		     document.appendChild(rootElement);
		     
		     Element name = document.createElement("name");
		     name.appendChild(document.createTextNode(star.getName()));
		     rootElement.appendChild(name);


		     for (String key : keyList) {
                 if ((prop.get(key) != null) && !key.contains("error")) {
                     Element elem = document.createElement(key.replaceAll("_", ""));

                     if (prop.containsKey(key + "_error_min") && prop.containsKey(key + "_error_max")) {
						 if(prop.get(key+"_error_min")!= null && prop.get(key+"_error_max")!=null) {
							 Attr attr = document.createAttribute("errorminus");
							 attr.setValue(prop.get(key + "_error_min"));
							 elem.setAttributeNode(attr);
							 Attr attr2 = document.createAttribute("errorplus");
							 attr2.setValue(prop.get(key + "_error_max"));
							 elem.setAttributeNode(attr2);
						 }
                     }
                     elem.appendChild(document.createTextNode(prop.get(key)));
                     rootElement.appendChild(elem);
                 }
             }

            DocumentBuilder subdoc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlPlanet(system)));

            Document doc = subdoc.parse(is);

            Node sub = doc.getFirstChild();

            Node imported = document.importNode(sub,true);
            rootElement.appendChild(imported);


			//write to XML String
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		   	DOMSource source = new DOMSource(document);
		   	StringWriter writer = new StringWriter();
		   	transformer.transform(source, new StreamResult(writer));
		   	String output = writer.getBuffer().toString();
			String prettyOutput = toPrettyString(output, 4);
			return prettyOutput;

			}catch (Exception e){

			}
		return null;


	}
	public static String xmlSystem(Systems system){
		/**
		 * Takes a system object and returns an XML String for system
		 * @param system Is a system object for which the system, star and planet have to be converted to XML string
		 * @return The XML String for system including the star under it and the planet under the star
		 */
		CelestialObjects sys = system;
		HashMap<String, String> prop = sys.getProperties();
		
		List<String> keyList = new ArrayList<String>(prop.keySet());
		java.util.Collections.sort(keyList);
		
		try {
			 DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = df.newDocumentBuilder();

		     //xml document
		     Document document = db.newDocument();
		     Element rootElement = document.createElement("system");
		     document.appendChild(rootElement);
		     
		     Element name = document.createElement("name");
		     name.appendChild(document.createTextNode(sys.getName()));
		     rootElement.appendChild(name);

		     for (String key : keyList) {
                 if ((prop.get(key) != null) && !key.contains("error")) {
                     Element elem = document.createElement(key.replaceAll("_", ""));

                     if (prop.containsKey(key + "_error_min") && prop.containsKey(key + "_error_max")) {
						 if(prop.get(key+"_error_min")!= null && prop.get(key+"_error_max")!=null) {
							 Attr attr = document.createAttribute("errorminus");
							 attr.setValue(prop.get(key + "_error_min"));
							 elem.setAttributeNode(attr);
							 Attr attr2 = document.createAttribute("errorplus");
							 attr2.setValue(prop.get(key + "_error_max"));
							 elem.setAttributeNode(attr2);
						 }
                     }
                     elem.appendChild(document.createTextNode(prop.get(key)));
                     rootElement.appendChild(elem);
                 }
             }

             DocumentBuilder subdoc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
             InputSource is = new InputSource();
             is.setCharacterStream(new StringReader(xmlStar(system)));

             Document doc = subdoc.parse(is);

            Node sub = doc.getFirstChild();

            Node imported = document.importNode(sub,true);
            rootElement.appendChild(imported);


			//xml document
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);
			StringWriter writer = new StringWriter();
			transformer.transform(source, new StreamResult(writer));
			String output = writer.getBuffer().toString();
			String prettyOutput = toPrettyString(output, 4);
			return prettyOutput;

		}catch (Exception e){

		}
		return null;

	}
	public static Element xmlValue(String tag, String value){
		/**
		 * Takes two strings and returns a node with first string tag and second string text content
		 * @param tag Is a string which will be the tag of the returned node
		 * @param value Is a string which will be the text content of the returned node
		 * @return The Node needed to replace the old Node and update the old OEC database value
		 */
		try {
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = df.newDocumentBuilder();

			//xml document
			Document document = db.newDocument();
			Element rootElement = document.createElement(tag);
			rootElement.appendChild(document.createTextNode(value));
			return rootElement;
		} catch (Exception e){

		}
		return null;
	}

	public static String toPrettyString(String xml, int indent) {
		try {
			// Turn xml string into a document
			DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document document = documentbuild.parse(is);

			// Remove whitespaces outside tags
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
					document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				node.getParentNode().removeChild(node);
			}

			// Setup pretty print options
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Return pretty print xml string
			StringWriter stringWriter = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			return stringWriter.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static void main(String argv[]){
		try {
			ReadCSV.mapIndexes();
			CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
			CSVReader q = new CSVReader(new FileReader(PullingTools.localNasaArchive));
			List<String[]> allEUData = r.readAll();
			List<String[]> allNASAData = q.readAll();
			Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allEUData.get(1)), ReadCSV.EU);
			Systems n = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allNASAData.get(1)), ReadCSV.NASA);
            //System.out.println(xmlPlanet(n));
			System.out.println(xmlSystem(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingColumnNameException e){
			e.printStackTrace();
		}


	}
}
