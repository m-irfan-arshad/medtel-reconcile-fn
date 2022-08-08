package com.functions;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Appointment extends Resource {
    public Appointment(JSONObject json) {
        super(json);
    }

    public JSONObject Search(HTTPHandler http, String fhirStore) throws IOException {
        //generate json with all existing appointments for new appointment's patient
        match = http.GET(
            String.format( 
                "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/%1$s/fhir/Appointment"
                + "?patient=%2$s",
                fhirStore, getPatient()
            )
        );

        //get visit number of this appointment
        String vn = getVN(http);

        //check if this visit number matches any existing appointments
        if (hasMatch() && !vn.equals("")) {
            for(int i = 0; i < match.getJSONArray("entry").length(); i++) {
                Appointment appointment = new Appointment(match.getJSONArray("entry").getJSONObject(i));
                if (vn.equals(appointment.getVN(http))) {
                    //if match exists, remove non-matching appointments from json
                    match.getJSONArray("entry").clear();
                    match.getJSONArray("entry").put(appointment);
                    return match;
                }
            }
        }

        return new JSONObject();
    }

    private String getVN(HTTPHandler http) throws IOException {
        if (resource.has("basedOn") && resource.getJSONObject("basedOn").has("reference")) {
            JSONObject serviceRequest = http.GET("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/%1$s/fhir/"
                 + resource.getJSONObject("basedOn").getString("reference")
            );
            Encounter encounter = new Encounter(http.GET("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/%1$s/fhir/"
                + serviceRequest.getString("encounter")
            ));
            return encounter.getVN();
        }
        return "";
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
