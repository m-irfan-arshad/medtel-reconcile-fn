package com.functions;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Appointment extends Resource {
    public Appointment(JSONObject json) {
        super(json);
    }

    public JSONObject Search(HTTPHandler http, String fhirStore) throws IOException {
        match = http.GET(
            String.format( 
                "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/%1$s/fhir/Appointment"
                + "?patient=%2$s",
                fhirStore, getPatient()
            )
        );
        return match;
    }

    private String getPatient() {
        String patient = null;
        JSONArray participant = resource.getJSONArray("participant");
        for (int i = 0; i < participant.length(); i++) {
            String reference = participant.getJSONObject(i)
                                    .getJSONObject("actor")
                                    .getString("reference");
            if (reference.contains("Patient/")) {
                patient = reference;
                break;
            }
        }
        return patient;
    }
}
