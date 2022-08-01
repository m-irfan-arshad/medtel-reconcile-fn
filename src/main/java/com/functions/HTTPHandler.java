package com.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;
import org.json.JSONObject;
import com.google.api.services.healthcare.v1.CloudHealthcareScopes;
import com.google.auth.oauth2.GoogleCredentials;

public class HTTPHandler {
    private boolean verbose;

    public HTTPHandler() {
        verbose = false;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public JSONObject GET(String urlString) throws IOException {
        if (verbose) {
            System.out.println("GET request: sending to " + urlString);
        }
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    
        InputStream responseStream = http.getResponseCode() / 100 == 2
            ? http.getInputStream()
            : http.getErrorStream();
        try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
            String response = scanner.hasNext() ? scanner.next() : "";
            if (verbose) {
                System.out.println("GET request: " + http.getResponseCode() + " " + http.getResponseMessage());
                System.out.print("GET request: " + response);
            }
            scanner.close();
            http.disconnect();
            return new JSONObject(response);
        }
    }

    public JSONObject PUT(String urlString, JSONObject data) throws IOException {
        if (verbose) {
            System.out.println("PUT request: sending to " + urlString);
        }
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        http.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        byte[] out = data.toString().getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);

        InputStream responseStream = http.getResponseCode() / 100 == 2
            ? http.getInputStream()
            : http.getErrorStream();
        try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
            String response = scanner.hasNext() ? scanner.next() : "";
            if (verbose) {
                System.out.println("PUT request: " + http.getResponseCode() + " " + http.getResponseMessage());
                System.out.print("PUT request: " + response);
            }
            scanner.close();
            http.disconnect();
            return new JSONObject(response);
        }
    }

    private static String getAccessToken() throws IOException {
        GoogleCredentials credential =
            GoogleCredentials.getApplicationDefault()
                .createScoped(Collections.singleton(CloudHealthcareScopes.CLOUD_PLATFORM));
    
        return credential.refreshAccessToken().getTokenValue();
    }
}
