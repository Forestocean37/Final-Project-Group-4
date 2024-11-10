package edu.neu.final_project_group_4.ui.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AiViewModel extends ViewModel {
    private final MutableLiveData<List<String>> chatMessages;
    private final List<String> messages;

    public AiViewModel() {
        chatMessages = new MutableLiveData<>();
        messages = new ArrayList<>();
    }

    public LiveData<List<String>> getChatMessages() {
        return chatMessages;
    }

    public void sendMessage(String request) {
        messages.add("User: " + request);

        String response;
        switch (request) {
            case "Summarize my week":
                response = "Choose Daily, Weekly, or Monthly.";
                break;
            case "Reschedule tasks":
                response = "Select a date to reschedule.";
                break;
            case "Suggest free time slots":
                response = "Would you like suggestions for today, this week, or this month?";
                break;
            default:
                response = "I'm here to assist with your tasks!";
        }

        messages.add("AI: " + response);
        chatMessages.setValue(new ArrayList<>(messages));
    }

}
