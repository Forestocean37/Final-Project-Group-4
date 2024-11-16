package edu.neu.final_project_group_4.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TaskModel implements Parcelable {
    private String title;
    private String type;
    private String description;
    private String startTime;
    private List<String> people;
    private LocationModel location;

    private String taskId;

    public TaskModel() {
        this.title = "NULL";
        this.type = "UNKNOWN";
        this.description = "...";
        this.startTime = "NULL NULL";
        this.people = new ArrayList<>();
        this.location = new LocationModel("NULL", "NULL");
    }

    public TaskModel(String title, String type, String description, String startTime, List<String> people, LocationModel location) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.startTime = startTime;
        this.people = people;
        this.location = location;
    }

    public TaskModel(String title, String type, String startTime, List<String> people, LocationModel location) {
        this.title = title;
        this.type = type;
        this.description = "...";
        this.startTime = startTime;
        this.people = people;
        this.location = location;
    }

    public TaskModel(String title, String type, String description, String startTime, LocationModel location) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.startTime = startTime;
        this.people = new ArrayList<>();
        this.location = location;
    }

    public TaskModel(String title, String type, String startTime, LocationModel location) {
        this.title = title;
        this.type = type;
        this.description = "...";
        this.startTime = startTime;
        this.people = new ArrayList<>();
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskModel{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", people=" + people +
                ", location=" + location +  // Calls the toString() method of LocationModel
                ", taskId='" + taskId + '\'' +
                '}';
    }

    // Parcelable implementation

    protected TaskModel(Parcel in) {
        title = in.readString();
        type = in.readString();
        description = in.readString();
        startTime = in.readString();
        people = in.createStringArrayList(); // Read list of strings
        location = in.readParcelable(LocationModel.class.getClassLoader()); // Read Parcelable LocationModel
        taskId = in.readString();
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeStringList(people); // Write list of strings
        dest.writeParcelable(location, flags); // Write Parcelable LocationModel
        dest.writeString(taskId);
    }
}
