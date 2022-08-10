package com.functions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Encounter extends Resource {
    public Encounter(JSONObject json) {
        super(json);
    }

    public JSONObject Search(HTTPHandler http) throws IOException {
        String vn = getVN();
        System.out.println("Searching for Encounter with matching Visit Number: " + vn + "...");
        match = http.GET(
            String.format( 
                ReconcileFunction.FINAL_URL
                + "Encounter"
                + "?subject=%1$s"
                + "&identifier=%2$s",
                getReference(), vn
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
