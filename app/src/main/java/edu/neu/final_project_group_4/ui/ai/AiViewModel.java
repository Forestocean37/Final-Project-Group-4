// stable code
//package edu.neu.final_project_group_4.ui.ai;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AiViewModel extends ViewModel {
//    private final MutableLiveData<List<String>> chatMessages;
//    private final List<String> messages;
//    private final List<Task> tasks;
//    private Task selectedTask;
//
//    public AiViewModel() {
//        chatMessages = new MutableLiveData<>();
//        messages = new ArrayList<>();
//        tasks = new ArrayList<>();
//
//        // Mock tasks (5 fake data entries)
//        tasks.add(new Task("Task 1", "Complete project report", "2024-11-10 10:00"));
//        tasks.add(new Task("Task 2", "Team meeting", "2024-11-11 14:00"));
//        tasks.add(new Task("Task 3", "Code review session", "2024-11-12 16:30"));
//        tasks.add(new Task("Task 4", "Design brainstorming", "2024-11-13 09:00"));
//        tasks.add(new Task("Task 5", "Client presentation", "2024-11-15 11:00"));
//    }
//
//    public List<Task> getTasks() {
//        return tasks;
//    }
//
//    public LiveData<List<String>> getChatMessages() {
//        return chatMessages;
//    }
//
//    public void setSelectedTask(Task task) {
//        selectedTask = task;
//    }
//
//    public void confirmReschedule(Task task, String newDate) {
//        if (task != null) {
//            task.setDate(newDate);
//            sendMessage("Task '" + task.getTitle() + "' has been successfully rescheduled to " + newDate);
//        }
//    }
//
//    public void sendMessage(String request) {
//        messages.add("AI: " + request);
//        chatMessages.setValue(new ArrayList<>(messages));
//    }
//
//    // Summarize tasks based on timeframe
//    public void summarizeTasks(String timeframe) {
//        StringBuilder summary = new StringBuilder("Tasks for " + timeframe.toLowerCase() + ":");
//
//        for (Task task : tasks) {
//            if (isTaskInTimeframe(task, timeframe)) {
//                summary.append("\n- ").append(task.getTitle())
//                        .append(": ").append(task.getDescription())
//                        .append(" on ").append(task.getDate());
//            }
//        }
//
//        if (summary.toString().equals("Tasks for " + timeframe.toLowerCase() + ":")) {
//            summary.append("\nNo tasks available for the selected timeframe.");
//        }
//
//        sendMessage(summary.toString());
//    }
//
//    // Suggest free time slots based on timeframe
//    public void suggestFreeTimeSlots(String timeframe) {
//        StringBuilder suggestions = new StringBuilder("Suggested free time slots for " + timeframe.toLowerCase() + ":");
//
//        if (timeframe.equalsIgnoreCase("daily")) {
//            suggestions.append("\n- 12:00 PM - 1:00 PM");
//            suggestions.append("\n- 3:00 PM - 4:00 PM");
//        } else if (timeframe.equalsIgnoreCase("weekly")) {
//            suggestions.append("\n- Monday 2:00 PM - 3:00 PM");
//            suggestions.append("\n- Wednesday 11:00 AM - 12:00 PM");
//        } else if (timeframe.equalsIgnoreCase("monthly")) {
//            suggestions.append("\n- 1st and 15th of the month, 10:00 AM - 11:00 AM");
//        }
//
//        // Include any tasks within the timeframe for context
//        for (Task task : tasks) {
//            if (isTaskInTimeframe(task, timeframe)) {
//                suggestions.append("\nNote: Task '").append(task.getTitle())
//                        .append("' scheduled on ").append(task.getDate());
//            }
//        }
//
//        sendMessage(suggestions.toString());
//    }
//
//    // Helper method to check if a task is within the specified timeframe
//    private boolean isTaskInTimeframe(Task task, String timeframe) {
//        if (timeframe.equalsIgnoreCase("daily")) {
//            return task.getDate().contains("2024-11-10"); // Example for today's date
//        } else if (timeframe.equalsIgnoreCase("weekly")) {
//            return task.getDate().startsWith("2024-11-1"); // Any date in the week of Nov 10-17
//        } else if (timeframe.equalsIgnoreCase("monthly")) {
//            return task.getDate().startsWith("2024-11"); // Any date in November 2024
//        }
//        return false;
//    }
//
//    // Inner class for Task representation
//    public static class Task {
//        private String title;
//        private String description;
//        private String date;
//
//        public Task(String title, String description, String date) {
//            this.title = title;
//            this.description = description;
//            this.date = date;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public String getDate() {
//            return date;
//        }
//
//        public void setDate(String newDate) {
//            this.date = newDate;
//        }
//    }
//}


package edu.neu.final_project_group_4.ui.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.neu.final_project_group_4.BuildConfig;

public class AiViewModel extends ViewModel {
    private final MutableLiveData<List<String>> chatMessages;
    private final List<String> messages;
    private final List<Task> tasks;
    private Task selectedTask;
    private GenerativeModelFutures model;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AiViewModel() {
        chatMessages = new MutableLiveData<>();
        messages = new ArrayList<>();
        tasks = new ArrayList<>();

        // Mock tasks (5 fake data entries)
        tasks.add(new Task("Task 1", "Complete project report", "2024-11-10 10:00", "Work", "High", true, "2024-11-09"));
        tasks.add(new Task("Task 2", "Team meeting", "2024-11-11 14:00", "Work", "Medium", false, null));
        tasks.add(new Task("Task 3", "Code review session", "2024-11-12 16:30", "Work", "Medium", false, null));
        tasks.add(new Task("Task 4", "Design brainstorming", "2024-11-13 09:00", "Work", "High", false, null));
        tasks.add(new Task("Task 5", "Client presentation", "2024-11-15 11:00", "Work", "High", false, null));
        tasks.add(new Task("Task 6", "Buy groceries", "2024-11-10 17:00", "Personal", "Low", true, "2024-11-10"));
        tasks.add(new Task("Task 7", "Birthday party", "2024-11-14 19:00", "Social", "Low", false, null));
        tasks.add(new Task("Task 8", "Gym workout", "2024-11-10 06:00", "Personal", "Medium", true, "2024-11-10"));
        tasks.add(new Task("Task 9", "Plan vacation", "2024-12-01 00:00", "Personal", "High", false, null));
        tasks.add(new Task("Task 10", "Doctor appointment", "2024-11-16 10:00", "Personal", "Medium", false, null));


        // Set up the GenerativeModel
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", BuildConfig.GEMINI_API_KEY);
        model = GenerativeModelFutures.from(gm);
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

    public void addMessage(String message, boolean isUserMessage) {
        if (isUserMessage) {
            messages.add("User: " + message);
        } else {
            messages.add("AI: " + message);
        }
        chatMessages.postValue(new ArrayList<>(messages));
    }

    void sendMessageToAI(String prompt) {
        Content content = new Content.Builder().addText(prompt).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String resultText = result.getText();
                        addMessage(resultText, false); // Add AI's response
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        addMessage("Sorry, an error occurred.", false);
                    }
                },
                executor
        );
    }

    public void summarizeTasks(String timeframe) {
        List<Task> tasksInTimeframe = getTasksInTimeframe(timeframe);
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please summarize my tasks for the ").append(timeframe.toLowerCase()).append(".\n");

        for (Task task : tasksInTimeframe) {
            prompt.append("- ").append(task.getTitle())
                    .append(": ").append(task.getDescription())
                    .append(" on ").append(task.getDate()).append("\n");
        }

        if (tasksInTimeframe.isEmpty()) {
            prompt.append("I have no tasks for the ").append(timeframe.toLowerCase()).append(".");
        }

        addMessage(prompt.toString(), true); // Add user's message
        sendMessageToAI(prompt.toString()); // Send prompt to AI
    }

    public void suggestFreeTimeSlots(String timeframe) {
        String prompt = "Please suggest some free time slots for the " + timeframe.toLowerCase() + ".";
        addMessage(prompt, true); // Add user's message
        sendMessageToAI(prompt);
    }

    public void confirmReschedule(Task task, String newDate) {
        if (task != null) {
            String prompt = "I would like to reschedule the task '" + task.getTitle() + "' to " + newDate + ".";
            addMessage(prompt, true); // Add user's message
            sendMessageToAI(prompt);
            task.setDate(newDate); // Update the task date locally
        }
    }

    public void prioritizeTasks() {
        StringBuilder prompt = new StringBuilder("Please prioritize my tasks based on deadlines, complexity, and importance.\n");
        for (Task task : tasks) {
            prompt.append("- ").append(task.getTitle())
                    .append(": ").append(task.getDescription())
                    .append(", Deadline: ").append(task.getDate())
                    .append(", Type: ").append(task.getType())
                    .append(", Complexity: ").append(task.getComplexity())
                    .append("\n");
        }

        addMessage(prompt.toString(), true);
        sendMessageToAI(prompt.toString());
    }

    public void viewInsights() {
        String prompt = "Please provide insights on my productivity based on my task completion data.";
        addMessage(prompt, true);
        sendMessageToAI(prompt);
    }

    public void breakDownTask(Task task) {
        String prompt = "Please break down the following task into subtasks:\n"
                + task.getTitle() + ": " + task.getDescription();
        addMessage(prompt, true);
        sendMessageToAI(prompt);
    }

    public void adjustDeadlines() {
        StringBuilder prompt = new StringBuilder("Please suggest new deadlines for overloaded days.\n");
        for (Task task : tasks) {
            prompt.append("- ").append(task.getTitle())
                    .append(": ").append(task.getDescription())
                    .append(", Deadline: ").append(task.getDate())
                    .append(", Type: ").append(task.getType())
                    .append("\n");
        }

        addMessage(prompt.toString(), true);
        sendMessageToAI(prompt.toString());
    }

    public void reviewTasks() {
        StringBuilder prompt = new StringBuilder("Please review my completed tasks and provide feedback and suggestions.\n");
        for (Task task : tasks) {
            if (task.isCompleted()) {
                prompt.append("- ").append(task.getTitle())
                        .append(": ").append(task.getDescription())
                        .append(", Completed on: ").append(task.getCompletionDate())
                        .append("\n");
            }
        }

        if (prompt.toString().equals("Please review my completed tasks and provide feedback and suggestions.\n")) {
            prompt.append("I have not completed any tasks yet.");
        }

        addMessage(prompt.toString(), true);
        sendMessageToAI(prompt.toString());
    }


    private List<Task> getTasksInTimeframe(String timeframe) {
        List<Task> tasksInTimeframe = new ArrayList<>();
        for (Task task : tasks) {
            if (isTaskInTimeframe(task, timeframe)) {
                tasksInTimeframe.add(task);
            }
        }
        return tasksInTimeframe;
    }

    private boolean isTaskInTimeframe(Task task, String timeframe) {
        // Implement proper date parsing and comparison here
        return true; // Placeholder for simplicity
    }

    // Inner class for Task representation
    public static class Task {
        private String title;
        private String description;
        private String date;
        private String type; // e.g., personal, work, social
        private String complexity; // e.g., low, medium, high
        private boolean completed;
        private String completionDate;

        public Task(String title, String description, String date, String type, String complexity, boolean completed, String completionDate) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.type = type;
            this.complexity = complexity;
            this.completed = completed;
            this.completionDate = completionDate;
        }

        // Getters and setters for new fields

        public String getType() { return type; }
        public String getComplexity() { return complexity; }
        public boolean isCompleted() { return completed; }
        public String getCompletionDate() { return completionDate; }

        // Existing getters and setters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDate() { return date; }
        public void setDate(String newDate) { this.date = newDate; }
    }

}
