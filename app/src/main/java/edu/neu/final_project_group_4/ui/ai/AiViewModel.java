package edu.neu.final_project_group_4.ui.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class AiViewModel extends ViewModel {
    private final MutableLiveData<List<String>> chatMessages;
    private final List<String> messages;
    private final List<Task> tasks;
    private Task selectedTask;

    public AiViewModel() {
        chatMessages = new MutableLiveData<>();
        messages = new ArrayList<>();
        tasks = new ArrayList<>();

        // Mock tasks (5 fake data entries)
        tasks.add(new Task("Task 1", "Complete project report", "2024-11-10 10:00"));
        tasks.add(new Task("Task 2", "Team meeting", "2024-11-11 14:00"));
        tasks.add(new Task("Task 3", "Code review session", "2024-11-12 16:30"));
        tasks.add(new Task("Task 4", "Design brainstorming", "2024-11-13 09:00"));
        tasks.add(new Task("Task 5", "Client presentation", "2024-11-15 11:00"));
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public LiveData<List<String>> getChatMessages() {
        return chatMessages;
    }

    public void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public void confirmReschedule(Task task, String newDate) {
        if (task != null) {
            task.setDate(newDate);
            sendMessage("Task '" + task.getTitle() + "' has been successfully rescheduled to " + newDate);
        }
    }

    public void sendMessage(String request) {
        messages.add("AI: " + request);
        chatMessages.setValue(new ArrayList<>(messages));
    }

    // Summarize tasks based on timeframe
    public void summarizeTasks(String timeframe) {
        StringBuilder summary = new StringBuilder("Tasks for " + timeframe.toLowerCase() + ":");

        for (Task task : tasks) {
            if (isTaskInTimeframe(task, timeframe)) {
                summary.append("\n- ").append(task.getTitle())
                        .append(": ").append(task.getDescription())
                        .append(" on ").append(task.getDate());
            }
        }

        if (summary.toString().equals("Tasks for " + timeframe.toLowerCase() + ":")) {
            summary.append("\nNo tasks available for the selected timeframe.");
        }

        sendMessage(summary.toString());
    }

    // Suggest free time slots based on timeframe
    public void suggestFreeTimeSlots(String timeframe) {
        StringBuilder suggestions = new StringBuilder("Suggested free time slots for " + timeframe.toLowerCase() + ":");

        if (timeframe.equalsIgnoreCase("daily")) {
            suggestions.append("\n- 12:00 PM - 1:00 PM");
            suggestions.append("\n- 3:00 PM - 4:00 PM");
        } else if (timeframe.equalsIgnoreCase("weekly")) {
            suggestions.append("\n- Monday 2:00 PM - 3:00 PM");
            suggestions.append("\n- Wednesday 11:00 AM - 12:00 PM");
        } else if (timeframe.equalsIgnoreCase("monthly")) {
            suggestions.append("\n- 1st and 15th of the month, 10:00 AM - 11:00 AM");
        }

        // Include any tasks within the timeframe for context
        for (Task task : tasks) {
            if (isTaskInTimeframe(task, timeframe)) {
                suggestions.append("\nNote: Task '").append(task.getTitle())
                        .append("' scheduled on ").append(task.getDate());
            }
        }

        sendMessage(suggestions.toString());
    }

    // Helper method to check if a task is within the specified timeframe
    private boolean isTaskInTimeframe(Task task, String timeframe) {
        if (timeframe.equalsIgnoreCase("daily")) {
            return task.getDate().contains("2024-11-10"); // Example for today's date
        } else if (timeframe.equalsIgnoreCase("weekly")) {
            return task.getDate().startsWith("2024-11-1"); // Any date in the week of Nov 10-17
        } else if (timeframe.equalsIgnoreCase("monthly")) {
            return task.getDate().startsWith("2024-11"); // Any date in November 2024
        }
        return false;
    }

    // Inner class for Task representation
    public static class Task {
        private String title;
        private String description;
        private String date;

        public Task(String title, String description, String date) {
            this.title = title;
            this.description = description;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String newDate) {
            this.date = newDate;
        }
    }
}
