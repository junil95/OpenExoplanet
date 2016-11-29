package com.team23.TestSuite;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;

import com.opencsv.CSVReader;
import com.team23.ModelStarSystems.SystemBuilder;
import com.team23.ModelStarSystems.Systems;
import com.team23.UpdateTools.PullingTools;
import com.team23.UpdateTools.ReadCSV;

/**
 * Created by Rishi on 2016-11-13.
 * TestSuite for Merge class
 */
public class MergeTest {

    /**
     * Check if this method produces the correct XML system file
     */
    @Test
    public void testnewSystem(){

        try{
            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allData = r.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allData.get(1)), ReadCSV.EU);
            s.setName("Test");
            File created = new File(PullingTools.oecData + s.getName() + ".xml");
            File testFile = new File("C:\\Users\\Rishi\\IdeaProjects\\team23-Project\\Data\\" + s.getName() + ".xml");

            //Parse test file
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document testdoc = db.parse(testFile);

            //hopefully creates correct system file in oecData directory
            //newSystem(s, generateXML.xmlSystem(s));

            //Parse created file
            DocumentBuilder db2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document createddoc = db2.parse(created);

            //turn both documents into strings
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource testsource = new DOMSource(testdoc);

            TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
            Transformer transformer2 = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource createdsource = new DOMSource(createddoc);

            StringWriter writer = new StringWriter();
            transformer.transform(testsource, new StreamResult(writer));
            String testString = writer.getBuffer().toString();

            StringWriter writer2 = new StringWriter();
            transformer.transform(createdsource, new StreamResult(writer2));
            String expectedString = writer2.getBuffer().toString();

            assertEquals(expectedString, testString);


        } catch (Exception e){

        }

    }
}
