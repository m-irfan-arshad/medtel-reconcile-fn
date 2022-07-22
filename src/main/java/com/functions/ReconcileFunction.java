package com.functions;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.auth.oauth2.GoogleCredentials;
import com.functions.ReconcileFunction.PubSubMessage;
import com.google.api.services.healthcare.v1.CloudHealthcareScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;

public class ReconcileFunction implements BackgroundFunction<PubSubMessage> {

  private static final String FINAL_FHIRSTORE = "finalFhirStore";
  private static final String ENDPOINT = "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/%1$s/fhir/%2$s/%3$s";

  @Override
  public void accept(PubSubMessage message, Context context) {
    String initFhirPath = message.data != null ? new String(Base64.getDecoder().decode(message.data)) : "null";

    //parse fhir path from PubSub
    String[] args = initFhirPath.split("/");
    String initFhirStore = args[7];
    String fhirResourceType = args[9];
    String fhirResourceId = args[10];

    System.out.println(String.format("Attempting to move FHIR resource %1$s %2$s from %3$s to %4$s...", fhirResourceType, fhirResourceId, initFhirStore, FINAL_FHIRSTORE));
    
    String data = "";

    //make GET request
    System.out.println("GET request: sending...");
    try {
      URL url = new URL(String.format(ENDPOINT, initFhirStore, fhirResourceType, fhirResourceId));
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod("GET");
      http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
      http.setRequestProperty("Content-Type", "application/json; charset=utf-8");

      InputStream responseStream = http.getResponseCode() / 100 == 2
          ? http.getInputStream()
          : http.getErrorStream();
      Scanner s = new Scanner(responseStream).useDelimiter("\\A");
      data = s.hasNext() ? s.next() : "";
      
      System.out.println("GET request: " + http.getResponseCode() + " " + http.getResponseMessage());
      //System.out.print("GET response: " + data);

      s.close();
      http.disconnect();
    } catch (IOException ex){
      System.out.println("IOException: " + ex);
    }

    //make PUT request
    System.out.println("PUT request: sending...");
    try {
      URL url = new URL(String.format(ENDPOINT, FINAL_FHIRSTORE, fhirResourceType, fhirResourceId));
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("PUT");
      http.setDoOutput(true);
      http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
      http.setRequestProperty("Content-Type", "application/json; charset=utf-8");

      byte[] out = data.getBytes(StandardCharsets.UTF_8);

      OutputStream stream = http.getOutputStream();
      stream.write(out);

      InputStream responseStream = http.getResponseCode() / 100 == 2
          ? http.getInputStream()
          : http.getErrorStream();
      Scanner s = new Scanner(responseStream).useDelimiter("\\A");
      //String response = s.hasNext() ? s.next() : "";
      
      System.out.println("PUT request: " + http.getResponseCode() + " " + http.getResponseMessage());
      //System.out.print("PUT response: " + response);

      s.close();
      http.disconnect();
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

  private static String getAccessToken() throws IOException {
    GoogleCredentials credential =
        GoogleCredentials.getApplicationDefault()
            .createScoped(Collections.singleton(CloudHealthcareScopes.CLOUD_PLATFORM));

    return credential.refreshAccessToken().getTokenValue();
  }
}
