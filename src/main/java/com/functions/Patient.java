package com.functions;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;

public class Patient extends Resource {
    
    public Patient(JSONObject json) {
        super(json);
    }

    public JSONObject Search(HTTPHandler http, String fhirStore) throws IOException {
        match = http.GET(
            String.format( 
                "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/%1$s/fhir/Patient"
                + "?name=%2$s"
                + "&family=%3$s"
                + "&birthdate=%4$s"
                + "&identifier=http://hl7.org/fhir/sid/us-ssn|%5$s"
                + "&identifier=%6$s",
                fhirStore, getFirstName(), getLastName(), getDOB(), getSSN(), getMRN()
            )
        );
        return match;
    }

    private String getFirstName() {
        return resource.getJSONArray("name").getJSONObject(0).getJSONArray("given").getString(0);
    }

    private String getLastName() {
        return resource.getJSONArray("name").getJSONObject(0).getString("family");
    }

    private String getDOB() {
        return resource.getString("birthDate");
    }

    private String getMRN() {
        String MRN = null;
        JSONArray identifier = resource.getJSONArray("identifier");
        for (int i = 0; i < identifier.length(); i++) {
            String code = identifier.getJSONObject(i)
                                    .getJSONObject("type")
                                    .getJSONArray("coding")
                                    .getJSONObject(0)
                                    .getString("code");
            if (code.equals("MRN")) {
                MRN = identifier.getJSONObject(i).getString("value");
            }
        }
        return MRN;
    }

    private String getSSN() {
        String SSN = null;
        JSONArray identifier = resource.getJSONArray("identifier");
        for (int i = 0; i < identifier.length(); i++) {
            String code = identifier.getJSONObject(i)
                                    .getJSONObject("type")
                                    .getJSONArray("coding")
                                    .getJSONObject(0)
                                    .getString("code");
            if (code.equals("SB")) {
                SSN = identifier.getJSONObject(i).getString("value");
            }
        }
        return SSN;
    }
}
