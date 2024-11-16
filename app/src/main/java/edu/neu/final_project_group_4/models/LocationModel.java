package edu.neu.final_project_group_4.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class LocationModel implements Parcelable {
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

    @NonNull
    @Override
    public String toString() {
        return "LocationModel{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    // Parcelable implementation
    protected LocationModel(Parcel in) {
        type = in.readString();
        address = in.readString();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(address);
    }
}
