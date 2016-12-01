package com.team23.UpdateTools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.GZIPInputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import org.apache.commons.io.FileUtils;

/**
 * Created by dhrumil on 10/10/16.
 */

public class PullingTools {
  /**
   * Url for getting oec as seperate files per system
   */
  public static final String oecSeperateFiles = "https://github.com/OpenExoplanetCatalogue/open_exoplanet_catalogue/archive/master.zip";
  
  /**
   * Url for getting oec as one file with all of the systems in it
   */
  public static final String oecOneFile = "https://github.com/OpenExoplanetCatalogue/oec_gzip/raw/master/systems.xml.gz";
  
  /**
   * Url for getting the exoplanet Eu catalogue
   */
  public static final String exoplanetEu = "http://exoplanet.eu/catalog/csv";
  
  /**
   * Url for getting the nasa Archive catalogue
   */
  public static final String nasaArchive = "http://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=exoplanets&format=csv&select=*";
  
  /**
   * Url for getting oec as single file per system
   */
  public static final String localOecFile = "Data/oec/oec.xml";
  
  /**
   * Path to the oec folder containing catalogue data
   */
  public static final String oecData = "Data/oec/open_exoplanet_catalogue-master/systems/";
  
  /**
   * Url for getting the exoplanet Eu catalogue
   */
  public static final String localExoplanetEu = "Data/exoplanetEu/exoplanetEu.csv";
  
  /**
   * Url for getting the nasa Archive catalogue
   */
  public static final String localNasaArchive = "Data/nasaArchive/nasaArchive.csv";
  
  /**
   * Paths to store old version of the catalogue Eu
   */
  public static final String localExoplanetEuOld = "Data/exoplanetEu/exoplanetEuOld.csv";
  
  /**
   * Path to store the old version of the catalogue NASA
   */
  public static final String localNasaArchiveOld = "Data/nasaArchive/nasaArchiveOld.csv";
  
  /**
   * Method used to fetch a file from the url and save it
   *
   * @param fileName The path to save the file
   * @param fileUrl  The url to fetch the file from
   * @throws IOException When fetching or saving the file fails
   */
  private static void saveFileFromUrl(String fileName, String fileUrl) throws IOException {
    //save file from the url, 60 s timeout
    FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName), 60000, 60000);
  }
  
  /**
   * Fetch and save the Exoplanet Eu catalogue
   *
   * @throws IOException When fetching or saving the file fails
   */
  public static void pullExoplanetEu() throws IOException {
    saveFileFromUrl(localExoplanetEu, exoplanetEu);
  }
  
  /**
   * Fetch and save the NASA archive catalogue
   *
   * @throws IOException When fetching or saving the file fails
   */
  public static void pullNasaArchive() throws IOException {
    saveFileFromUrl(localNasaArchive, nasaArchive);
  }
  
  /**
   * Fetch, save and decompress the oec catalogue contained in one file
   *
   * @throws IOException When fetching or saving the file fails
   */
  public static void pullOecOneFile() throws IOException {
    String destFileName = "Data/oec/oec.xml.gz";
    
    saveFileFromUrl(destFileName, oecOneFile);
    
    //decompress gunzip
    byte[] buffer = new byte[1024];
    
    //decompress the gz file
    GZIPInputStream gzis =
            new GZIPInputStream(new FileInputStream(destFileName));
    
    FileOutputStream out =
            new FileOutputStream(localOecFile);
    
    int len;
    while ((len = gzis.read(buffer)) > 0) {
      out.write(buffer, 0, len);
    }
    gzis.close();
    out.close();
    
  }
  
  /**
   * Fetch, save and unzip the oec catalogue contained in seperate files by system
   */
  public static void pullOecSeperateFiles() throws IOException, ZipException {
    String zipDest = "Data/oec/oecSeperateFiles.zip";
    saveFileFromUrl(zipDest, oecSeperateFiles);
    
    //Delete the old version of the catalogue if it already exists
    String p = "Data/oec/open_exoplanet_catalogue-master";
    if (Files.isDirectory(Paths.get(p))) {
      FileUtils.deleteDirectory(new File(p));
    }
    
    ZipFile zipFile = new ZipFile(zipDest);
    List<FileHeader> fileHeaders = zipFile.getFileHeaders();
    //unzip only the systems folder since it is the only thing we require
    for (FileHeader fh : fileHeaders) {
      if (fh.getFileName().contains("open_exoplanet_catalogue-master/systems/")) {
        zipFile.extractFile(fh.getFileName(), "Data/oec");
      }
    }
  }
  
  private static void renameOldCatalogueVersion(String oldPath, String newPath) throws IOException {
    //rename the old catalogues
    File oldName = new File(oldPath);
    File newName = new File(newPath);
    //remove exoplanetEuOld if it exists
    if (newName.exists())
      newName.delete();
    //Now rename
    if (!oldName.renameTo(newName)) {
      throw new IOException("Unable to rename " + oldPath + " to " + newPath);
    }
  }
  
  public static void createLatestCatalogueCopy() throws IOException {
    renameOldCatalogueVersion(localExoplanetEu, localExoplanetEuOld);
    PullingTools.pullExoplanetEu();
    renameOldCatalogueVersion(localNasaArchive, localNasaArchiveOld);
    PullingTools.pullNasaArchive();
  }
  
  //Temp for testing purposes, remove after
  public static void main(String[] args) {
    try {
      //pullExoplanetEu();
//      pullNasaArchive();
//      pullOecSeperateFiles();
      pullOecOneFile();
      System.out.println("Done");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


