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

    public static JSONObject GET(String urlString) throws IOException {
        // if (verbose) {
        //     System.out.println("GET request: sending to " + urlString);
        // }
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
            // if (verbose) {
            //     System.out.println("GET request: " + http.getResponseCode() + " " + http.getResponseMessage());
            //     System.out.print("GET request: " + response);
            // }
            scanner.close();
            http.disconnect();
            return new JSONObject(response);
        }
    }

    public static JSONObject PUT(String urlString, JSONObject data) throws IOException {
        return PUT(urlString, data.toString());
    }

    public static JSONObject PUT(String urlString, String data) throws IOException {
        // if (verbose) {
        //     System.out.println("PUT request: sending to " + urlString);
        // }
        URL url = new URL(urlString);
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
        try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
            String response = scanner.hasNext() ? scanner.next() : "";
            // if (verbose) {
            //     System.out.println("PUT request: " + http.getResponseCode() + " " + http.getResponseMessage());
            //     System.out.print("PUT request: " + response);
            // }
            scanner.close();
            http.disconnect();
            return new JSONObject(response);
        }
    }

    public static JSONObject POST(String urlString, JSONObject data) throws IOException {
        return POST(urlString, data.toString());
    }

    public static JSONObject POST(String urlString, String data) throws IOException {
        // if (verbose) {
        //     System.out.println("POST request: sending to " + urlString);
        // }
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        http.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);

        InputStream responseStream = http.getResponseCode() / 100 == 2
            ? http.getInputStream()
            : http.getErrorStream();
        try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
            String response = scanner.hasNext() ? scanner.next() : "";
            // if (verbose) {
            //     System.out.println("POST request: " + http.getResponseCode() + " " + http.getResponseMessage());
            //     System.out.print("POST request: " + response);
            // }
            scanner.close();
            http.disconnect();
            return new JSONObject(response);
        }
    }

    public static JSONObject DELETE(String urlString) throws IOException {
        // if (verbose) {
        //     System.out.println("DELETE request: sending to " + urlString);
        // }
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    
        InputStream responseStream = http.getResponseCode() / 100 == 2
            ? http.getInputStream()
            : http.getErrorStream();
        try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
            String response = scanner.hasNext() ? scanner.next() : "";
            // if (verbose) {
            //     System.out.println("DELETE request: " + http.getResponseCode() + " " + http.getResponseMessage());
            //     System.out.print("DELETE request: " + response);
            // }
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
