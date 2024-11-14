package edu.neu.final_project_group_4.models;

public class LocationModel {
    // Name of online providers (Zoom, Google Meet, etc.), or "Offline"
    private String type;

    // If online: url; If offline: street address
    private String address;

    public LocationModel() {
    }

    public LocationModel(String type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
