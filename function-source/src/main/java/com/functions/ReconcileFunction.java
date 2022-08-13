package com.functions;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.functions.ReconcileFunction.PubSubMessage;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;

public class ReconcileFunction implements BackgroundFunction<PubSubMessage> {

  // Config
  //-----------------------------------------------------------------
  private String stagingProject = "medtel-349114";
  private String stagingLocation = "us-east4";
  private String stagingDataset = "datastore";
  private String stagingFhirStore = "fhirstore";

  private String finalProject = "medtel-349114";
  private String finalLocation = "us-east4";
  private String finalDataset = "datastore";
  private String finalFhirStore = "finalFhirStore";
  //-----------------------------------------------------------------

  private final String URL_FORMAT = "https://healthcare.googleapis.com/v1/projects/%1$s/locations/%2$s/datasets/%3$s/fhirStores/%4$s/fhir/";
  public static String STAGING_URL;
  public static String FINAL_URL;

  @Override
  public void accept(PubSubMessage message, Context context) {
    //decode pubsub message
    String sourceFhirPath = message.data != null 
      ? new String(Base64.getDecoder().decode(message.data)) 
      : "null";

    run(sourceFhirPath);
  }

  public void run(String sourceFhirPath) {
    //parse fhir path from PubSub
    String[] args = sourceFhirPath.split("/");
    String resourceType = args[9];
    String resourceId = args[10];

    //generate URLs
    STAGING_URL = String.format(URL_FORMAT, stagingProject, stagingLocation, stagingDataset, stagingFhirStore);
    FINAL_URL = String.format(URL_FORMAT, finalProject, finalLocation, finalDataset, finalFhirStore);

    System.out.println(String.format("Attempting to move FHIR resource %1$s/%2$s from %3$s to %4$s...", resourceType, resourceId, stagingFhirStore, finalFhirStore));
    
    HTTPHandler http = new HTTPHandler();
    Resource resource;
    
    try {
      //get fhir resource from source fhir store
      JSONObject json = http.GET(STAGING_URL + resourceType + "/" + resourceId);

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
      resource.Search(http);

      //handle match
      if (resource.hasMatch()) {
        String matchId = resource.getMatchId();
        resource.SetId(matchId);
        System.out.println("Match found: updating FHIR resource " + resourceType + "/" + matchId + "...");   
        http.PUT(FINAL_URL + resourceType + "/" + matchId, resource.toJSON());
      } else {
        System.out.println("No match found: moving FHIR resource " + resourceType + "/" + resourceId + "...");   
        http.PUT(FINAL_URL + resourceType + "/" + resourceId, resource.toJSON());;
      }

    } catch (IOException ex){
      System.out.println("IOException: " + ex);
    }
  }

  public void setStagingConfig(String project, String location, String dataset, String fhirStore) {
    stagingProject = project;
    stagingLocation = location;
    stagingDataset = dataset;
    stagingFhirStore = fhirStore;
  }

  public void setFinalConfig(String project, String location, String dataset, String fhirStore) {
    finalProject = project;
    finalLocation = location;
    finalDataset = dataset;
    finalFhirStore = fhirStore;
  }

  public static class PubSubMessage {
    String data;
    Map<String, String> attributes;
    String messageId;
    String publishTime;
  }
}
