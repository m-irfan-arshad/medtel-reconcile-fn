package com.functions;

import com.functions.ReconcileFunction.PubSubMessage;
import static com.functions.HTTPHandler.*;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;

public class ReconcileFunction implements BackgroundFunction<PubSubMessage> {

  private String stagingProject, stagingLocation, stagingDataset, stagingFhirStore,
    finalProject, finalLocation, finalDataset, finalFhirStore;

  private final String URL_FORMAT = "https://healthcare.googleapis.com/v1/projects/%1$s/locations/%2$s/datasets/%3$s/fhirStores/%4$s/fhir/";
  
  public static String STAGING_URL, FINAL_URL;

  @Override
  public void accept(PubSubMessage message, Context context) {
    //decode pubsub message
    String sourceFhirPath = message.data != null 
      ? new String(Base64.getDecoder().decode(message.data)) 
      : "null";
    if (sourceFhirPath != "null") {
      reconcile(sourceFhirPath);
    }
  }

  public void reconcile (String sourceFhirPath) {
    //fetch env variables from config
    readConfig("config.properties");

    //parse fhir path from PubSub
    String[] args = sourceFhirPath.split("/");
    String resourceType = args[9];
    String resourceId = args[10];

    System.out.println(String.format("Attempting to move FHIR resource %1$s/%2$s from %3$s to %4$s...", resourceType, resourceId, stagingFhirStore, finalFhirStore));
    
    Resource resource;
    try {
      //get fhir resource from source fhir store
      JSONObject json = GET(STAGING_URL + resourceType + "/" + resourceId);

      //handle matchable resource types
      if (resourceType.equals("Patient")) {
        resource = new Patient(json);
      }
      else if (resourceType.equals("Encounter")) {
        resource = new Encounter(json);
      }
      else if (resourceType.equals("Appointment")) {
        resource = new Appointment(json);
      }
      else {
        resource = new Resource(json);
      }

      //search for matches in final fhir store
      System.out.println("Searching for matches...");
      resource.Search();

      //handle match
      if (resource.hasMatch()) {
        String matchId = resource.getMatchId();
        resource.SetId(matchId);
        System.out.println("Match found: updating FHIR resource " + resourceType + "/" + matchId + "...");   
        PUT(FINAL_URL + resourceType + "/" + matchId, resource.toJSON());
      } else {
        System.out.println("No match found: moving FHIR resource " + resourceType + "/" + resourceId + "...");   
        PUT(FINAL_URL + resourceType + "/" + resourceId, resource.toJSON());;
      }

    } catch (IOException ex){
      System.out.println("IOException: " + ex);
    }
  }

  public void readConfig(String filename) {
    File configFile = Paths.get("src/main/java/com/functions/" + filename).toAbsolutePath().toFile();
    try {
      FileReader reader = new FileReader(configFile);
      Properties props = new Properties();
      props.load(reader);

      stagingProject = props.getProperty("staging.project");
      stagingLocation = props.getProperty("staging.location");
      stagingDataset = props.getProperty("staging.dataset");
      stagingFhirStore = props.getProperty("staging.fhirstore");

      finalProject = props.getProperty("final.project");
      finalLocation = props.getProperty("final.location");
      finalDataset = props.getProperty("final.dataset");
      finalFhirStore = props.getProperty("final.fhirstore");

      STAGING_URL = String.format(URL_FORMAT, stagingProject, stagingLocation, stagingDataset, stagingFhirStore);
      FINAL_URL = String.format(URL_FORMAT, finalProject, finalLocation, finalDataset, finalFhirStore);

      reader.close();
    } catch (IOException ex) {
      System.out.println("IOException: " + ex);
    }
  }

  public static class PubSubMessage {
    String data;
    Map<String, String> attributes;
    String messageId;
    String publishTime;
  }
}
