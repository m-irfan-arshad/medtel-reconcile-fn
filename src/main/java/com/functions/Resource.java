package com.functions;

import java.io.IOException;

import org.json.JSONObject;

public class Resource {

    public JSONObject resource;
    public JSONObject match;

    public Resource(JSONObject json) {
        resource = json;
        match = new JSONObject();
    }

    public String getId() {
        return resource.getString("id");
    }

    public void SetId(String newId) {
        resource.put("id", newId);
    }

    public JSONObject toJSON() {
        return resource;
    }

    public String toString() {
        return resource.toString();
    }

    //only used by resources that implement matching
    public JSONObject Search(HTTPHandler http) throws IOException {
        match = new JSONObject();
        return null;
    }

    public boolean hasMatch() {
        return match.has("entry");
    }

    //by default getMatchId() fetches first resource id in entry JSONArray
    public String getMatchId() {
        if (hasMatch()) {
            return match.getJSONArray("entry").getJSONObject(0).getJSONObject("resource").getString("id");
        }
        return null;
    }
}