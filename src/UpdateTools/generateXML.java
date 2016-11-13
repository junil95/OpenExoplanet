package UpdateTools;


import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import com.opencsv.CSVReader;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import ModelStarSystems.*;
import UpdateTools.ReadCSV.MissingColumnNameException;

public class generateXML {
	public static String xmlPlanet(Systems system){
		/**
		 * Takes a system object and returns an XML String for planet
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
                         Attr attr = document.createAttribute("errorminus");
                         attr.setValue(prop.get(key + "_error_min"));
                         elem.setAttributeNode(attr);
                         Attr attr2 = document.createAttribute("errorplus");
                         attr2.setValue(prop.get(key + "_error_max"));
                         elem.setAttributeNode(attr2);
                     }
                     elem.appendChild(document.createTextNode(prop.get(key)));
                     rootElement.appendChild(elem);
                 }
             }

		     //write to XML String
			 TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 Transformer transformer = transformerFactory.newTransformer();
             transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		     DOMSource source = new DOMSource(document);
		     StringWriter writer = new StringWriter();
		     transformer.transform(source, new StreamResult(writer));
		     String output = writer.getBuffer().toString();

		     return output;

			} catch (Exception e){

			}
		return null;

	}
	public static String xmlStar(Systems system){
		/**
		 * Takes a system object and returns an XML String for star
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
                         Attr attr = document.createAttribute("errorminus");
                         attr.setValue(prop.get(key + "_error_min"));
                         elem.setAttributeNode(attr);
                         Attr attr2 = document.createAttribute("errorplus");
                         attr2.setValue(prop.get(key + "_error_max"));
                         elem.setAttributeNode(attr2);
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
		   	DOMSource source = new DOMSource(document);
		   	StringWriter writer = new StringWriter();
		   	transformer.transform(source, new StreamResult(writer));
		   	String output = writer.getBuffer().toString();
		   	return output;

			}catch (Exception e){

			}
		return null;


	}

	public static String xmlSystem(Systems system){
		/**
		 * Takes a system object and returns an XML String for system
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
                         Attr attr = document.createAttribute("errorminus");
                         attr.setValue(prop.get(key + "_error_min"));
                         elem.setAttributeNode(attr);
                         Attr attr2 = document.createAttribute("errorplus");
                         attr2.setValue(prop.get(key + "_error_max"));
                         elem.setAttributeNode(attr2);
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
				 DOMSource source = new DOMSource(document);
                 //StreamResult result = new StreamResult(new File("C:\\file.xml"));
                 //transformer.transform(source, result);

                 StringWriter writer = new StringWriter();
				 transformer.transform(source, new StreamResult(writer));
				 String output = writer.getBuffer().toString();

				 return output;

		}catch (Exception e){

		}
		return null;

	}

	public static Element xmlValue(String tag, String value){
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


	public static void main(String argv[]){
		try {
			ReadCSV.mapIndexes();
			CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
			List<String[]> allData = r.readAll();
			Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
            System.out.println(xmlStar(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingColumnNameException e){
			e.printStackTrace();
		}



	}
}
