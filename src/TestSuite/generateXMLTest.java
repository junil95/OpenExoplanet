package TestSuite;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;
import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import com.opencsv.CSVReader;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static UpdateTools.generateXML.*;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Rishi on 2016-11-13.
 * TestSuite for generateXML class
 */
public class generateXMLTest {

    /**
     * Check if this method produces the correct XML String output
     * Test for both exoplanetEU and NASA databases
     */
    @Test
    public void testxmlPlanet(){
        String expectedEU = "<planet><name>11 Com b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2008</discoveryyear><eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity><lastupdate>2015-08-21</lastupdate><mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass><massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother><periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron><period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period><semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis></planet>";
        String expectedNASA = "<planet><name>HD 4308 b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2005</discoveryyear><eccentricity errorminus=\"-0.010000\" errorplus=\"0.010000\">0.000000</eccentricity><lastupdate>2014-12-03</lastupdate><massother errorminus=\"\" errorplus=\"\">0.04420</massother><periastron errorminus=\"-47.0000\" errorplus=\"47.0000\">359.0000</periastron><period errorminus=\"-0.02000000\" errorplus=\"0.02000000\">15.56000000</period><semimajoraxis errorminus=\"\" errorplus=\"\">0.115000</semimajoraxis></planet>";
        try {
            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            CSVReader q = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allEUData = r.readAll();
            List<String[]> allNASAData = q.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allEUData.get(1)), ReadCSV.EU);
            Systems n = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allNASAData.get(1)), ReadCSV.NASA);

            //exoplanetEU test
            assertEquals(expectedEU, xmlPlanet(s));

            //NASAArchive test
            assertEquals(expectedNASA, xmlPlanet(n));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadCSV.MissingColumnNameException e){
            e.printStackTrace();
        }

    }

    /**
     * Check if this method produces the correct XML String output with planets under stars
     * Test for both exoplanetEU and NASA databases
     */
    @Test
    public void testxmlStar(){
        String expectedEU = "<star><name>11 Com</name><magV>4.74</magV><mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass><metallicity>-0.35</metallicity><radius>19.0</radius><radiusmax>2.0</radiusmax><radiusmin>2.0</radiusmin><spectraltype>G8 III</spectraltype><temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature><planet><name>11 Com b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2008</discoveryyear><eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity><lastupdate>2015-08-21</lastupdate><mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass><massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother><periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron><period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period><semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis></planet></star>";
        String expectedNASA = "<star><name>HD 4308</name><age errorminus=\"\" errorplus=\"\">4.300</age><magH>5.101</magH><magHmax>0.016</magHmax><magHmin>0.016</magHmin><magI>5.835</magI><magJ>5.366</magJ><magJmax>0.024</magJmax><magJmin>0.024</magJmin><magK>4.945</magK><magKmax>0.020</magKmax><magKmin>0.020</magKmin><magV>6.560</magV><mass errorminus=\"-0.01\" errorplus=\"0.01\">0.83</mass><metallicity>[Fe/H]</metallicity><temperature errorminus=\"-13.00\" errorplus=\"13.00\">5685.00</temperature><planet><name>HD 4308 b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2005</discoveryyear><eccentricity errorminus=\"-0.010000\" errorplus=\"0.010000\">0.000000</eccentricity><lastupdate>2014-12-03</lastupdate><massother errorminus=\"\" errorplus=\"\">0.04420</massother><periastron errorminus=\"-47.0000\" errorplus=\"47.0000\">359.0000</periastron><period errorminus=\"-0.02000000\" errorplus=\"0.02000000\">15.56000000</period><semimajoraxis errorminus=\"\" errorplus=\"\">0.115000</semimajoraxis></planet></star>";
        try {
            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            CSVReader q = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allEUData = r.readAll();
            List<String[]> allNASAData = q.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allEUData.get(1)), ReadCSV.EU);
            Systems n = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allNASAData.get(1)), ReadCSV.NASA);

            //exoplanetEU test
            assertEquals(expectedEU, xmlStar(s));

            //NASAArchive test
            assertEquals(expectedNASA, xmlStar(n));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadCSV.MissingColumnNameException e){
            e.printStackTrace();
        }

    }

    /**
     * Check if this method produces the correct XML String output with planets under stars under system
     * Test for both exoplanetEU and NASA databases
     */
    @Test
    public void testxmlSystem(){
        String expectedEU = "<system><name>11 Com</name><declination>17.7927778</declination><distance errorminus=\"10.5\" errorplus=\"10.5\">110.6</distance><rightascension>185.1791667</rightascension><star><name>11 Com</name><magV>4.74</magV><mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass><metallicity>-0.35</metallicity><radius>19.0</radius><radiusmax>2.0</radiusmax><radiusmin>2.0</radiusmin><spectraltype>G8 III</spectraltype><temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature><planet><name>11 Com b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2008</discoveryyear><eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity><lastupdate>2015-08-21</lastupdate><mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass><massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother><periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron><period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period><semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis></planet></star></system>";
        String expectedNASA = "<system><name>HD 4308</name><declination>-65d38m58.3s</declination><distance errorminus=\"-0.27\" errorplus=\"0.26\">21.85</distance><rightascension>00h44m39.27s</rightascension><star><name>HD 4308</name><age errorminus=\"\" errorplus=\"\">4.300</age><magH>5.101</magH><magHmax>0.016</magHmax><magHmin>0.016</magHmin><magI>5.835</magI><magJ>5.366</magJ><magJmax>0.024</magJmax><magJmin>0.024</magJmin><magK>4.945</magK><magKmax>0.020</magKmax><magKmin>0.020</magKmin><magV>6.560</magV><mass errorminus=\"-0.01\" errorplus=\"0.01\">0.83</mass><metallicity>[Fe/H]</metallicity><temperature errorminus=\"-13.00\" errorplus=\"13.00\">5685.00</temperature><planet><name>HD 4308 b</name><discoverymethod>Radial Velocity</discoverymethod><discoveryyear>2005</discoveryyear><eccentricity errorminus=\"-0.010000\" errorplus=\"0.010000\">0.000000</eccentricity><lastupdate>2014-12-03</lastupdate><massother errorminus=\"\" errorplus=\"\">0.04420</massother><periastron errorminus=\"-47.0000\" errorplus=\"47.0000\">359.0000</periastron><period errorminus=\"-0.02000000\" errorplus=\"0.02000000\">15.56000000</period><semimajoraxis errorminus=\"\" errorplus=\"\">0.115000</semimajoraxis></planet></star></system>";
        try {
            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            CSVReader q = new CSVReader(new FileReader(PullingTools.localNasaArchive));
            List<String[]> allEUData = r.readAll();
            List<String[]> allNASAData = q.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allEUData.get(1)), ReadCSV.EU);
            Systems n = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allNASAData.get(1)), ReadCSV.NASA);

            //exoplanetEU test
            assertEquals(expectedEU, xmlSystem(s));

            //NASAArchive test
            assertEquals(expectedNASA, xmlSystem(n));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadCSV.MissingColumnNameException e){
            e.printStackTrace();
        }

    }

    /**
     * Check if this method produces the correct XML node for the specific value
     */
    @Test
    public void testxmlValue(){
        try {
            //creating new system object
            ReadCSV.mapIndexes();
            CSVReader r = new CSVReader(new FileReader(PullingTools.localExoplanetEu));
            List<String[]> allEUData = r.readAll();
            Systems s = SystemBuilder.buildSystemWithCSVRow(Arrays.asList(allEUData.get(1)), ReadCSV.EU);

            //turn system object into string
            String sysstring = xmlSystem(s);

            //create a document out of XML string
            DocumentBuilder documentbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(sysstring));
            Document sysdoc = documentbuild.parse(is);
            Element rootElement = sysdoc.getDocumentElement();

            //extract expected node from XML document
            Node expectedNode = rootElement.getElementsByTagName("distance").item(0);

            //arguments for xmlValue
            String tag = "distance";
            String value = s.getProperties().get(tag);

            //test if expected node and actual node have the same name and text content/value
            assertEquals(expectedNode.getLocalName(), xmlValue(tag, value).getLocalName());
            System.out.println(value);
            assertEquals(expectedNode.getTextContent(), xmlValue(tag, value).getTextContent());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadCSV.MissingColumnNameException e){
            e.printStackTrace();
        } catch (javax.xml.parsers.ParserConfigurationException e){
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e){
            e.printStackTrace();
        }
    }

}
