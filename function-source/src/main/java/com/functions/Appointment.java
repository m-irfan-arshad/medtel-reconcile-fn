package com.functions;
import static com.functions.ReconcileFunction.FINAL_URL;
import static com.functions.HTTPHandler.*;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;


public class Appointment extends Resource {
    public Appointment(JSONObject json) {
        super(json);
    }

    public JSONObject Search() throws IOException {
        //generate json with all existing appointments for new appointment's patient
        match = GET(
            String.format( 
                FINAL_URL
                + "Appointment"
                + "?patient=%1$s",
                getPatient()
            )
        );

        //get visit number of this appointment
        String vn = getVN();

        //check if this visit number matches any existing appointments
        if (hasMatch() && !vn.equals("")) {
            System.out.println("Searching for Appointment with matching Visit Number: " + vn + "...");
            for(int i = 0; i < match.getJSONArray("entry").length(); i++) {
                JSONObject entry = match.getJSONArray("entry").getJSONObject(i);
                Appointment appointment = new Appointment(entry.getJSONObject("resource"));

                if (vn.equals(appointment.getVN())) {
                    //if match exists, remove non-matching appointments from json
                    match.getJSONArray("entry").clear();
                    match.getJSONArray("entry").put(entry);
                    return match;
                }
            }
        }

        match = new JSONObject();

        //if no visit number match exists, match by fields
        System.out.println("Searching for Appointment with matching patient/practitioner/location/date...");
        match = GET(
            String.format( 
                FINAL_URL
                + "Appointment"
                + "?patient=%1$s"
                + "&practitioner=%2$s"
                + "&location=%3$s"
                + "&date=%4$s",
                getPatient(), getPractitioner(), getLocation(), getDate()
            )
        );

        return match;
    }

    public String getVN() throws IOException {
        if (resource.has("basedOn") && resource.getJSONArray("basedOn").getJSONObject(0).has("reference")) {
            ServiceRequest serviceRequest = new ServiceRequest(
                GET(FINAL_URL + getServiceRequestReference()));
            Encounter encounter = new Encounter(
                GET(FINAL_URL + serviceRequest.getEncounterReference()));
            return encounter.getVN();
        }
        return "";
    }

    public String getServiceRequestReference() {
        return resource.getJSONArray("basedOn").getJSONObject(0).getString("reference");
    }

    public String getPatient() {
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

    public String getPractitioner() {
        String practitioner = null;
        JSONArray participant = resource.getJSONArray("participant");
        for (int i = 0; i < participant.length(); i++) {
            String reference = participant.getJSONObject(i)
                                    .getJSONObject("actor")
                                    .getString("reference");
            if (reference.contains("Practitioner/")) {
                practitioner = reference;
                break;
            }
        }
        return practitioner;
    }

    public String getLocation() {
        String location = null;
        JSONArray participant = resource.getJSONArray("participant");
        for (int i = 0; i < participant.length(); i++) {
            String reference = participant.getJSONObject(i)
                                    .getJSONObject("actor")
                                    .getString("reference");
            if (reference.contains("Location/")) {
                location = reference;
                break;
            }
        }
        return location;
    }

    public String getDate() {
       return resource.getString("start");
    }
}
