package edu.neu.final_project_group_4.models;

import java.util.List;

public class TaskModel {
    private String title;
    private String type;
    private String description;
    private String startTime;
    private List<String> people;
    private LocationModel location;

    private String taskId;

    public TaskModel() {
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
        this.startTime = startTime;
        this.people = people;
        this.location = location;
    }

    public TaskModel(String title, String type, String description, String startTime, LocationModel location) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
    }

    public TaskModel(String title, String type, String startTime, LocationModel location) {
        this.title = title;
        this.startTime = startTime;
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
}
