import java.io.File;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CSV2XML {
	public static void exoeuPlanet(String csvLine, String header){
		/**
		 * Takes a single line of CSV and creates an XML String for planets
		 */
		String line = csvLine;
		String[] rowValues = line.split(",");
		
		String replacedString = header.replace("# ", "");
		String[] headerValues = replacedString.split(",");
		
		//Planet Info
		int name = Arrays.asList(headerValues).indexOf("name");
		int semimajoraxis = Arrays.asList(headerValues).indexOf("semi_major_axis");
		int semimajorminus = Arrays.asList(headerValues).indexOf("semi_major_axis_error_min");
		int semimajorplus = Arrays.asList(headerValues).indexOf("semi_major_axis_error_max");
		int periastron = Arrays.asList(headerValues).indexOf("omega");
		int periminus = Arrays.asList(headerValues).indexOf("omega_error_min");
		int periplus = Arrays.asList(headerValues).indexOf("omega_error_max");;
		int eccentricity = Arrays.asList(headerValues).indexOf("eccentricity");
		int eccenminus = Arrays.asList(headerValues).indexOf("eccentricity_error_min");
		int eccenplus = Arrays.asList(headerValues).indexOf("eccentricity_error_max");
		int period = Arrays.asList(headerValues).indexOf("orbital_period");;
		int periodminus = Arrays.asList(headerValues).indexOf("orbital_period_error_min");
		int periodplus = Arrays.asList(headerValues).indexOf("orbital_period_error_max");
		int mass = Arrays.asList(headerValues).indexOf("mass");
		int mass_error_min = Arrays.asList(headerValues).indexOf("mass_error_min");
		int mass_error_max = Arrays.asList(headerValues).indexOf("mass_error_max");
		int discover_method = Arrays.asList(headerValues).indexOf("detection_type");;
		int discovered = Arrays.asList(headerValues).indexOf("discovered");
		int updated = Arrays.asList(headerValues).indexOf("updated");
		
		/**Star Info
		int star_name = header.indexOf("star_name");
		int star_age = header.indexOf("star_age");
		int star_radius = header.indexOf("star_radius");
		int star_mass = header.indexOf("star_mass");
		int star_sp_type = header.indexOf("star_sp_type");
		int star_temp = header.indexOf("star_teff");
		int star_metallicity = header.indexOf("star_metallicity");
		**/
		
		//Making planet XML file
		try {
			 DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = df.newDocumentBuilder();

		     // planet and name
		     Document document = db.newDocument();
		     Element rootElement = document.createElement("planet");
		     document.appendChild(rootElement);
		     
		     Element planetname = document.createElement("name");
		     planetname.appendChild(document.createTextNode(rowValues[name]));
		     rootElement.appendChild(planetname);
		     
		     //semimajoraxis
		     Element semi_major_axis = document.createElement("semimajoraxis");
		     
		     Attr attr = document.createAttribute("errorminus");
		     attr.setValue(rowValues[semimajorminus]);
		     semi_major_axis.setAttributeNode(attr);
		     Attr attr2 = document.createAttribute("errorplus");
		     attr2.setValue(rowValues[semimajorplus]);
		     semi_major_axis.setAttributeNode(attr2);
		     
		     semi_major_axis.appendChild(document.createTextNode(rowValues[semimajoraxis]));
		     rootElement.appendChild(semi_major_axis);
		     
		     //periastron
		     Element peria = document.createElement("periastron");
		     
		     Attr attr3 = document.createAttribute("errorminus");
		     attr3.setValue(rowValues[periminus]);
		     peria.setAttributeNode(attr3);
		     Attr attr4 = document.createAttribute("errorplus");
		     attr4.setValue(rowValues[periplus]);
		     peria.setAttributeNode(attr4);
		     
		     peria.appendChild(document.createTextNode(rowValues[periastron]));
		     rootElement.appendChild(peria);
		     
		     //eccentricity
		     Element eccen = document.createElement("eccentricity");
		     
		     Attr attr5 = document.createAttribute("errorminus");
		     attr5.setValue(rowValues[eccenminus]);
		     eccen.setAttributeNode(attr5);
		     Attr attr6 = document.createAttribute("errorplus");
		     attr6.setValue(rowValues[eccenplus]);
		     eccen.setAttributeNode(attr6);
		     
		     eccen.appendChild(document.createTextNode(rowValues[eccentricity]));
		     rootElement.appendChild(eccen);
		     
		     //period
		     Element periodelem = document.createElement("period");
		     
		     Attr attr7 = document.createAttribute("errorminus");
		     attr7.setValue(rowValues[periodminus]);
		     periodelem.setAttributeNode(attr7);
		     Attr attr8 = document.createAttribute("errorplus");
		     attr8.setValue(rowValues[periodplus]);
		     periodelem.setAttributeNode(attr8);
		     
		     periodelem.appendChild(document.createTextNode(rowValues[period]));
		     rootElement.appendChild(periodelem);
		     
		     //mass
		     Element masselem = document.createElement("mass");
		     
		     Attr attr9 = document.createAttribute("errorminus");
		     attr9.setValue(rowValues[mass_error_min]);
		     masselem.setAttributeNode(attr9);
		     Attr attr10 = document.createAttribute("errorplus");
		     attr10.setValue(rowValues[mass_error_max]);
		     masselem.setAttributeNode(attr10);
		     
		     masselem.appendChild(document.createTextNode(rowValues[mass]));
		     rootElement.appendChild(masselem);
		     
		     //discoverymethod
		     Element discoverelem = document.createElement("discoverymethod");
		     discoverelem.appendChild(document.createTextNode(rowValues[discover_method]));
		     rootElement.appendChild(discoverelem);
		     
		     //discoveryear
		     Element discoveryearelem = document.createElement("discoveryyear");
		     discoveryearelem.appendChild(document.createTextNode(rowValues[discovered]));
		     rootElement.appendChild(discoveryearelem);
		     
		     //lastupdate
		     Element lastelem = document.createElement("lastupdate");
		     lastelem.appendChild(document.createTextNode(rowValues[updated]));
		     rootElement.appendChild(lastelem);
		     
		     //write to XML String
			 TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 Transformer transformer = transformerFactory.newTransformer();
		     DOMSource source = new DOMSource(document);
			 StreamResult result = new StreamResult(System.out);
			 //Convert to XML File instead
			 //StreamResult result = new StreamResult(new File("C:\\file.xml"));
			 transformer.transform(source, result);

		     
		} catch (Exception e){
			
		}
		
	}
	public static void exoeuStar(String csvLine, String header){
		/**
		 * Takes a single line of CSV and creates an XML String for stars
		 */
		String line = csvLine;
		String[] rowValues = line.split(",");
		
		String replacedString = header.replace("# ", "");
		String[] headerValues = replacedString.split(",");
		
		//star information
		int star_name = Arrays.asList(headerValues).indexOf("star_name");
		int star_age = Arrays.asList(headerValues).indexOf("star_age");
		int star_radius = Arrays.asList(headerValues).indexOf("star_radius");
		int star_mass = Arrays.asList(headerValues).indexOf("star_mass");
		int star_sp_type = Arrays.asList(headerValues).indexOf("star_sp_type");
		int star_temp = Arrays.asList(headerValues).indexOf("star_teff");
		int star_metallicity = Arrays.asList(headerValues).indexOf("star_metallicity");
		
		
		try {
			 DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = df.newDocumentBuilder();

		     // star and name
		     Document document = db.newDocument();
		     Element rootElement = document.createElement("star");
		     document.appendChild(rootElement);
		     
		     Element starname = document.createElement("name");
		     starname.appendChild(document.createTextNode(rowValues[star_name]));
		     rootElement.appendChild(starname);
		     
		     //age
		     Element starage = document.createElement("age");
		     starage.appendChild(document.createTextNode(rowValues[star_age]));
		     rootElement.appendChild(starage);
		     
		     //radius
		     Element starradius = document.createElement("radius");
		     starradius.appendChild(document.createTextNode(rowValues[star_radius]));
		     rootElement.appendChild(starradius);
		     
		     //mass
		     Element starmass = document.createElement("mass");
		     starmass.appendChild(document.createTextNode(rowValues[star_mass]));
		     rootElement.appendChild(starmass);
		     
		     //spectraltype
		     Element starsptype = document.createElement("spectraltype");
		     starsptype.appendChild(document.createTextNode(rowValues[star_sp_type]));
		     rootElement.appendChild(starsptype);
		     
		     //temperature
		     Element startemp = document.createElement("temperature");
		     startemp.appendChild(document.createTextNode(rowValues[star_temp]));
		     rootElement.appendChild(startemp);
		     
		     //metallicity
		     Element starmetallicity = document.createElement("metallicity");
		     starmetallicity.appendChild(document.createTextNode(rowValues[star_metallicity]));
		     rootElement.appendChild(starmetallicity);
		     
		     //write to XML String
			 TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 Transformer transformer = transformerFactory.newTransformer();
		     DOMSource source = new DOMSource(document);
			 StreamResult result = new StreamResult(System.out);
			 //Convert to XML File instead
			 //StreamResult result = new StreamResult(new File("C:\\file.xml"));
			 transformer.transform(source, result);
		     
		}catch (Exception e){
			
		}
		
	}
	
	/** Test
	public static void main(String argv[]){
		exoeuStar("11 Com b,19.4,1.5,1.5,19.4,1.5,1.5,,,,326.03,0.32,0.32,1.29,0.05,0.05,0.231,0.005,0.005,,,,0.011664,2008,2015-08-21,94.8,1.5,1.5,2452899.6,1.6,1.6,,,,,,,,,,,,,,,,,,,296.7,5.6,5.6,,,,,,,,Published in a refereed paper,Radial Velocity,,,,,11 Com,185.1791667,17.7927778,4.74,,,,,110.6,10.5,10.5,-0.35,0.09,0.09,2.7,0.3,0.3,19.0,2.0,2.0,G8 III,,,,4742.0,100.0,100.0,,,","# name,mass,mass_error_min,mass_error_max,mass_sini,mass_sini_error_min,mass_sini_error_max,radius,radius_error_min,radius_error_max,orbital_period,orbital_period_error_min,orbital_period_error_max,semi_major_axis,semi_major_axis_error_min,semi_major_axis_error_max,eccentricity,eccentricity_error_min,eccentricity_error_max,inclination,inclination_error_min,inclination_error_max,angular_distance,discovered,updated,omega,omega_error_min,omega_error_max,tperi,tperi_error_min,tperi_error_max,tconj,tconj_error_min,tconj_error_max,tzero_tr,tzero_tr_error_min,tzero_tr_error_max,tzero_tr_sec,tzero_tr_sec_error_min,tzero_tr_sec_error_max,lambda_angle,lambda_angle_error_min,lambda_angle_error_max,impact_parameter,impact_parameter_error_min,impact_parameter_error_max,tzero_vr,tzero_vr_error_min,tzero_vr_error_max,k,k_error_min,k_error_max,temp_calculated,temp_measured,hot_point_lon,geometric_albedo,geometric_albedo_error_min,geometric_albedo_error_max,log_g,publication_status,detection_type,mass_detection_type,radius_detection_type,alternate_names,molecules,star_name,ra,dec,mag_v,mag_i,mag_j,mag_h,mag_k,star_distance,star_distance_error_min,star_distance_error_max,star_metallicity,star_metallicity_error_min,star_metallicity_error_max,star_mass,star_mass_error_min,star_mass_error_max,star_radius,star_radius_error_min,star_radius_error_max,star_sp_type,star_age,star_age_error_min,star_age_error_max,star_teff,star_teff_error_min,star_teff_error_max,star_detected_disc,star_magnetic_field,star_alternate_names");
	}
	**/
}


