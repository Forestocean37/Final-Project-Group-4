package edu.neu.final_project_group_4.utils;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.final_project_group_4.models.TaskModel;

public class Task {
    private FirebaseFirestore db;
    private List<TaskModel> taskList;
    private int nextTask;
    private int tasksToday;

    private static class Instance {
        private static final Task _instance = new Task();
    }

    private Task() {
        db = FirebaseFirestore.getInstance();
        taskList = new ArrayList<>();
        nextTask = -1;
        tasksToday = 0;
    }

    public static Task getInstance() {
        return Instance._instance;
    }

    public void addNewTask(TaskModel task) {
        db.collection("user").document(User.getInstance().getUserId())
                .collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> Log.d("Task",
                        "Successfully added new task with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Task", "Error adding new task", e));
    }

    public interface FetchTasksCallback {
        void onTasksFetched();
    }

    public void fetchTasks(FetchTasksCallback callback) {
        db.collection("user").document(User.getInstance().getUserId())
                .collection("tasks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        taskList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TaskModel taskModel = document.toObject(TaskModel.class);
                            taskModel.setTaskId(document.getId());
                            taskList.add(taskModel);
                        }

                        arrangeTasks();
                        callback.onTasksFetched();
                    } else {
                        Log.w("Task", "Error fetching tasks", task.getException());
                    }
                });
    }

    private void arrangeTasks() {
        taskList.sort(Comparator.comparing(TaskModel::getStartTime));

        String currentTime = formatTime(LocalDateTime.now());
        for (int i = 0; i < taskList.size(); i++) {
            if (currentTime.compareTo(taskList.get(i).getStartTime()) <= 0) {
                nextTask = i;
                break;
            }
        }

        tasksToday = 0;
        if (nextTask >= 0) {
            for (int i = nextTask; i < taskList.size(); i++) {
                if (!isSameDay(currentTime, taskList.get(i).getStartTime())) {
                    break;
                }
                tasksToday++;
            }
        }
    }

    public List<TaskModel> getTaskList() {
        return taskList;
    }

    // Use this method to ensure consistency of timestamp format
    // Modify if needed (for your front-end requirement)
    public String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    private boolean isSameDay(String t1, String t2) {
        return t1.split(" ")[0].equals(t2.split(" ")[0]);
    }

    public TaskModel getNextTask() {
        if (nextTask < 0) {
            return null;
        }
        return taskList.get(nextTask);
    }

    public int getTasksToday() {
        return tasksToday;
    }

    // When updating any task, first update it locally,
    // and then submit the updated model to database through this method
    public void updateTask(String taskId, TaskModel updateTask) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", updateTask.getTitle());
        updateData.put("type", updateTask.getType());
        updateData.put("description", updateTask.getDescription());
        updateData.put("startTime", updateTask.getStartTime());
        updateData.put("people", updateTask.getPeople());
        updateData.put("location", updateTask.getLocation());

        db.collection("user").document(User.getInstance().getUserId())
                .collection("tasks").document(taskId)
                .update(updateData)
                .addOnSuccessListener(unused -> Log.d("Task", "Task updated successfully"))
                .addOnFailureListener(e -> Log.w("Task", "Error updating task", e));
    }

    // Delete the task locally first, and then submit change to database
    public void deleteTask(TaskModel task) {
        db.collection("user").document(User.getInstance().getUserId())
                .collection("tasks").document(task.getTaskId())
                .delete()
                .addOnSuccessListener(unused -> Log.d("Task", "Task deleted successfully"))
                .addOnFailureListener(e -> Log.w("Task", "Error deleting task", e));
    }
}
