package com.functions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Encounter extends Resource {
    public Encounter(JSONObject json) {
        super(json);
    }

    public JSONObject Search(HTTPHandler http, String fhirStore) throws IOException {
        match = http.GET(
            String.format( 
                "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/%1$s/fhir/Encounter"
                + "?subject=%2$s"
                + "&identifier=%3$s",
                fhirStore, getReference(), getVN()
            )
        );
        return match;
    }

    private String getReference() {
         return resource.getJSONObject("subject").getString("reference");
    }

    public String getVN() {
        String VN = null;
        JSONArray identifier = resource.getJSONArray("identifier");
        for (int i = 0; i < identifier.length(); i++) {
            String code = "";
            if (identifier.getJSONObject(i).has("type")) {
                code = identifier.getJSONObject(i)
                                    .getJSONObject("type")
                                    .getJSONArray("coding")
                                    .getJSONObject(0)
                                    .getString("code");
            }
            if (code.equals("VN")) {
                VN = identifier.getJSONObject(i).getString("value");
                break;
            }
        }
        return VN;
    }    

}
