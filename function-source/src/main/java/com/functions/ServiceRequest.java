package com.functions;

import org.json.JSONObject;

public class ServiceRequest extends Resource {
    
    public ServiceRequest(JSONObject json) {
        super(json);
    }

    //does not override search

    public String getEncounterReference() {
        return resource.getJSONObject("encounter").getString("reference");
    }

}
