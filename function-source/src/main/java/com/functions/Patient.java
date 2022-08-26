package com.functions;
import static com.functions.ReconcileFunction.FINAL_URL;
import static com.functions.HTTPHandler.GET;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;

public class Patient extends Resource {
    
    public Patient(JSONObject json) {
        super(json);
    }

    public JSONObject Search() throws IOException {
        System.out.println("Searching for Patient with matching name/DOB/SSN/MRN...");
        match = GET(
            String.format( 
                FINAL_URL 
                + "Patient"
                + "?name=%1$s"
                + "&family=%2$s"
                + "&birthdate=%3$s"
                + "&identifier=http://hl7.org/fhir/sid/us-ssn|%4$s"
                + "&identifier=%5$s",
                getFirstName(), getLastName(), getDOB(), getSSN(), getMRN()
            )
        );
        return match;
    }

    public String getFirstName() {
        return resource.getJSONArray("name").getJSONObject(0).getJSONArray("given").getString(0);
    }

    public String getLastName() {
        return resource.getJSONArray("name").getJSONObject(0).getString("family");
    }

    public String getDOB() {
        return resource.getString("birthDate");
    }

    public String getMRN() {
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

    public String getSSN() {
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
