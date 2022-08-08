package com.functions;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.functions.ReconcileFunction.PubSubMessage;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;

public class ReconcileFunction implements BackgroundFunction<PubSubMessage> {

  private static final String FINAL_FHIR_STORE = "finalFhirStore";
  private static final String ENDPOINT_TEMPLATE = "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/%1$s/fhir/%2$s/%3$s";

  @Override
  public void accept(PubSubMessage message, Context context) {
    //decode pubsub message
    String sourceFhirPath = message.data != null 
      ? new String(Base64.getDecoder().decode(message.data)) 
      : "null";

    //parse fhir path from PubSub
    String[] args = sourceFhirPath.split("/");
    String sourceFhirStore = args[7];
    String resourceType = args[9];
    String resourceId = args[10];

    System.out.println(String.format("Attempting to move FHIR resource %1$s/%2$s from %3$s to %4$s...", resourceType, resourceId, sourceFhirStore, FINAL_FHIR_STORE));
    
    HTTPHandler http = new HTTPHandler();
    Resource resource;
    
    try {
      //get fhir resource from source fhir store
      JSONObject json = http.GET(String.format(ENDPOINT_TEMPLATE, sourceFhirStore, resourceType, resourceId));

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

      //search for matches in destination fhir store
      System.out.println("Searching for matches...");
      resource.Search(http, FINAL_FHIR_STORE);

      //handle match
      if (resource.hasMatch()) {
        String matchId = resource.getMatchId();
        resource.SetId(matchId);
        System.out.println("Match found: updating FHIR resource " + resourceType + "/" + matchId + "...");   
        http.PUT(String.format(ENDPOINT_TEMPLATE, FINAL_FHIR_STORE, resourceType, matchId), resource.toJSON());
      } else {
        System.out.println("No match found: moving FHIR resource " + resourceType + "/" + resourceId + "...");   
        http.PUT(String.format(ENDPOINT_TEMPLATE, FINAL_FHIR_STORE, resourceType, resourceId), resource.toJSON());
      }

    } catch (IOException ex){
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
