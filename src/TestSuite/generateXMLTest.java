package TestSuite;

import ModelStarSystems.SystemBuilder;
import ModelStarSystems.Systems;
import UpdateTools.PullingTools;
import UpdateTools.ReadCSV;
import UpdateTools.generateXML;
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
        String expectedEU = toformatString("<planet>\n" +
                "    <name>11 Com b</name>\n" +
                "    <discoverymethod>Radial Velocity</discoverymethod>\n" +
                "    <discoveryyear>2008</discoveryyear>\n" +
                "    <eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity>\n" +
                "    <lastupdate>2015-08-21</lastupdate>\n" +
                "    <mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass>\n" +
                "    <massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother>\n" +
                "    <periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron>\n" +
                "    <period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period>\n" +
                "    <semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis>\n" +
                "</planet>", 4);
        String expectedNASA = toformatString("<planet>\n" +
                "    <name>Kepler-139 c</name>\n" +
                "    <discoverymethod>Transit</discoverymethod>\n" +
                "    <discoveryyear>2014</discoveryyear>\n" +
                "    <impactparameter errorminus=\"-0.3000\" errorplus=\"0.3000\">0.8300</impactparameter>\n" +
                "    <lastupdate>2014-05-14</lastupdate>\n" +
                "    <period errorminus=\"-0.00172000\" errorplus=\"0.00172000\">157.07287800</period>\n" +
                "    <radius>0.302</radius>\n" +
                "    <semimajoraxis>0.586000</semimajoraxis>\n" +
                "</planet>", 4);
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
        String expectedEU = toformatString("<star>\n" +
                "    <name>11 Com</name>\n" +
                "    <magV>4.74</magV>\n" +
                "    <mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass>\n" +
                "    <metallicity>-0.35</metallicity>\n" +
                "    <radius errorminus=\"2.0\" errorplus=\"2.0\">19.0</radius>\n" +
                "    <spectraltype>G8 III</spectraltype>\n" +
                "    <temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature>\n" +
                "    <planet>\n" +
                "        <name>11 Com b</name>\n" +
                "        <discoverymethod>Radial Velocity</discoverymethod>\n" +
                "        <discoveryyear>2008</discoveryyear>\n" +
                "        <eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity>\n" +
                "        <lastupdate>2015-08-21</lastupdate>\n" +
                "        <mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass>\n" +
                "        <massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother>\n" +
                "        <periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron>\n" +
                "        <period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period>\n" +
                "        <semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis>\n" +
                "    </planet>" +
                "</star>\n", 4);
        String expectedNASA = toformatString("<star>\n" +
                "    <name>Kepler-139</name>\n" +
                "    <magH>11.222</magH>\n" +
                "    <magHmax>0.018</magHmax>\n" +
                "    <magHmin>0.018</magHmin>\n" +
                "    <magJ>11.530</magJ>\n" +
                "    <magJmax>0.021</magJmax>\n" +
                "    <magJmin>0.021</magJmin>\n" +
                "    <magK>11.167</magK>\n" +
                "    <magKmax>0.015</magKmax>\n" +
                "    <magKmin>0.015</magKmin>\n" +
                "    <mass>1.08</mass>\n" +
                "    <metallicity>[Fe/H]</metallicity>\n" +
                "    <radius errorminus=\"-0.25\" errorplus=\"0.25\">1.30</radius>\n" +
                "    <temperature errorminus=\"-100.00\" errorplus=\"100.00\">5594.00</temperature>\n" +
                "    <planet>\n" +
                "        <name>Kepler-139 c</name>\n" +
                "        <discoverymethod>Transit</discoverymethod>\n" +
                "        <discoveryyear>2014</discoveryyear>\n" +
                "        <impactparameter errorminus=\"-0.3000\" errorplus=\"0.3000\">0.8300</impactparameter>\n" +
                "        <lastupdate>2014-05-14</lastupdate>\n" +
                "        <period errorminus=\"-0.00172000\" errorplus=\"0.00172000\">157.07287800</period>\n" +
                "        <radius>0.302</radius>\n" +
                "        <semimajoraxis>0.586000</semimajoraxis>\n" +
                "    </planet>\n" +
                "</star>", 4);
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
        String expectedEU = toformatString("<system>\n" +
                "    <name>11 Com</name>\n" +
                "    <declination>+18 48 34</declination>\n" +
                "    <distance errorminus=\"10.5\" errorplus=\"10.5\">110.6</distance>\n" +
                "    <rightascension>2 3 27</rightascension>\n" +
                "    <star>\n" +
                "        <name>11 Com</name>\n" +
                "        <magV>4.74</magV>\n" +
                "        <mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass>\n" +
                "        <metallicity>-0.35</metallicity>\n" +
                "        <radius errorminus=\"2.0\" errorplus=\"2.0\">19.0</radius>\n" +
                "        <spectraltype>G8 III</spectraltype>\n" +
                "        <temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature>\n" +
                "        <planet>\n" +
                "            <name>11 Com b</name>\n" +
                "            <discoverymethod>Radial Velocity</discoverymethod>\n" +
                "            <discoveryyear>2008</discoveryyear>\n" +
                "            <eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity>\n" +
                "            <lastupdate>2015-08-21</lastupdate>\n" +
                "            <mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass>\n" +
                "            <massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother>\n" +
                "            <periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron>\n" +
                "            <period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period>\n" +
                "            <semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis>\n" +
                "        </planet>\n" +
                "    </star>\n" +
                "</system>", 4);
        String expectedNASA = toformatString("<system>\n" +
                "    <name>Kepler-139</name>\n" +
                "    <declination>+43d53m21.7s</declination>\n" +
                "    <rightascension>18h49m34.07s</rightascension>\n" +
                "    <star>\n" +
                "        <name>Kepler-139</name>\n" +
                "        <magH>11.222</magH>\n" +
                "        <magHmax>0.018</magHmax>\n" +
                "        <magHmin>0.018</magHmin>\n" +
                "        <magJ>11.530</magJ>\n" +
                "        <magJmax>0.021</magJmax>\n" +
                "        <magJmin>0.021</magJmin>\n" +
                "        <magK>11.167</magK>\n" +
                "        <magKmax>0.015</magKmax>\n" +
                "        <magKmin>0.015</magKmin>\n" +
                "        <mass>1.08</mass>\n" +
                "        <metallicity>[Fe/H]</metallicity>\n" +
                "        <radius errorminus=\"-0.25\" errorplus=\"0.25\">1.30</radius>\n" +
                "        <temperature errorminus=\"-100.00\" errorplus=\"100.00\">5594.00</temperature>\n" +
                "        <planet>\n" +
                "            <name>Kepler-139 c</name>\n" +
                "            <discoverymethod>Transit</discoverymethod>\n" +
                "            <discoveryyear>2014</discoveryyear>\n" +
                "            <impactparameter errorminus=\"-0.3000\" errorplus=\"0.3000\">0.8300</impactparameter>\n" +
                "            <lastupdate>2014-05-14</lastupdate>\n" +
                "            <period errorminus=\"-0.00172000\" errorplus=\"0.00172000\">157.07287800</period>\n" +
                "            <radius>0.302</radius>\n" +
                "            <semimajoraxis>0.586000</semimajoraxis>\n" +
                "        </planet>\n" +
                "    </star>\n" +
                "</system>", 4);
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

    /**
     * Check if XML string is formatted properly
     */
    @Test
    public void testtoformatString(){
        String expected = "<system>\n" +
                "    <name>11 Com</name>\n" +
                "    <declination>+18 48 34</declination>\n" +
                "    <distance errorminus=\"10.5\" errorplus=\"10.5\">110.6</distance>\n" +
                "    <rightascension>2 3 27</rightascension>\n" +
                "    <star>\n" +
                "        <name>11 Com</name>\n" +
                "        <magV>4.74</magV>\n" +
                "        <mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass>\n" +
                "        <metallicity>-0.35</metallicity>\n" +
                "        <radius errorminus=\"2.0\" errorplus=\"2.0\">19.0</radius>\n" +
                "        <spectraltype>G8 III</spectraltype>\n" +
                "        <temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature>\n" +
                "        <planet>\n" +
                "            <name>11 Com b</name>\n" +
                "            <discoverymethod>Radial Velocity</discoverymethod>\n" +
                "            <discoveryyear>2008</discoveryyear>\n" +
                "            <eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity>\n" +
                "            <lastupdate>2015-08-21</lastupdate>\n" +
                "            <mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass>\n" +
                "            <massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother>\n" +
                "            <periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron>\n" +
                "            <period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period>\n" +
                "            <semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis>\n" +
                "        </planet>\n" +
                "    </star>\n" +
                "</system>";

        String input = "<system><name>11 Com</name><declination>+18 48 34</declination><distance errorminus=\"10.5\" errorplus=\"10.5\">110.6</distance><rightascension>2 3 27</rightascension><star>\n" +
                "    <name>11 Com</name>\n" +
                "    <magV>4.74</magV>\n" +
                "    <mass errorminus=\"0.3\" errorplus=\"0.3\">2.7</mass>\n" +
                "    <metallicity>-0.35</metallicity>\n" +
                "    <radius errorminus=\"2.0\" errorplus=\"2.0\">19.0</radius>\n" +
                "    <spectraltype>G8 III</spectraltype>\n" +
                "    <temperature errorminus=\"100.0\" errorplus=\"100.0\">4742.0</temperature>\n" +
                "    <planet>\n" +
                "        <name>11 Com b</name>\n" +
                "        <discoverymethod>Radial Velocity</discoverymethod>\n" +
                "        <discoveryyear>2008</discoveryyear>\n" +
                "        <eccentricity errorminus=\"0.005\" errorplus=\"0.005\">0.231</eccentricity>\n" +
                "        <lastupdate>2015-08-21</lastupdate>\n" +
                "        <mass errorminus=\"1.5\" errorplus=\"1.5\">19.4</mass>\n" +
                "        <massother errorminus=\"1.5\" errorplus=\"1.5\">19.4</massother>\n" +
                "        <periastron errorminus=\"1.5\" errorplus=\"1.5\">94.8</periastron>\n" +
                "        <period errorminus=\"0.32\" errorplus=\"0.32\">326.03</period>\n" +
                "        <semimajoraxis errorminus=\"0.05\" errorplus=\"0.05\">1.29</semimajoraxis>\n" +
                "    </planet>\n" +
                "</star></system>";
        String actual = generateXML.toformatString(input, 4);
    }

}
