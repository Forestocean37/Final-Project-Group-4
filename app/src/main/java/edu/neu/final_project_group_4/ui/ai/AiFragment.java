//package edu.neu.final_project_group_4.ui.ai;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import java.util.Calendar;
//import edu.neu.final_project_group_4.databinding.FragmentAiBinding;
//
//public class AiFragment extends Fragment {
//    private FragmentAiBinding binding;
//    private ChatAdapter chatAdapter;
//    private AiViewModel aiViewModel;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentAiBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        Log.d("AiFragment", "Fragment created");
//
//        aiViewModel = new ViewModelProvider(this).get(AiViewModel.class);
//        chatAdapter = new ChatAdapter();
//
//        // RecyclerView setup
//        binding.recyclerViewResponses.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerViewResponses.setAdapter(chatAdapter);
//
//        // Log visibility states
//        Log.d("AiFragment", "Initial visibility - recyclerViewResponses: " + binding.recyclerViewResponses.getVisibility());
//        Log.d("AiFragment", "Initial visibility - scrollViewOptions: " + binding.scrollViewOptions.getVisibility());
//        Log.d("AiFragment", "Initial visibility - buttonAskGenAi: " + binding.buttonAskGenAi.getVisibility());
//
//        // Show initial "Ask GenAI" button
//        binding.buttonAskGenAi.setOnClickListener(v -> {
//            Log.d("AiFragment", "Ask GenAI button clicked");
//
//            // Hide the "Ask GenAI" button
//            binding.buttonAskGenAi.setVisibility(View.GONE);
//
//            // Use a handler to delay the visibility update for other elements
//            new android.os.Handler().postDelayed(() -> {
//                requireActivity().runOnUiThread(() -> {
//                    Log.d("AiFragment", "Updating visibility on UI thread");
//
//                    // Set visibility to VISIBLE
//                    binding.scrollViewOptions.setVisibility(View.VISIBLE);
//                    binding.recyclerViewResponses.setVisibility(View.VISIBLE);
//
//                    // Force layout refresh
//                    binding.scrollViewOptions.invalidate();
//                    binding.recyclerViewResponses.invalidate();
//                    binding.scrollViewOptions.requestLayout();
//                    binding.recyclerViewResponses.requestLayout();
//
//                    // Ensure the views are updated on the main thread
//                    binding.scrollViewOptions.post(() -> {
//                        binding.scrollViewOptions.setVisibility(View.VISIBLE);
//                        binding.recyclerViewResponses.setVisibility(View.VISIBLE);
//                        Log.d("AiFragment", "Visibility forced update complete");
//                    });
//
//                    // Send initial AI message
//                    aiViewModel.sendMessage("What can I help you with today?");
//                });
//            }, 500); // Delay by 500 milliseconds (0.5 seconds)
//        });
//
//
//
//
//        // Summarize My Week button
//        binding.buttonSummarizeWeek.setOnClickListener(v -> {
//            Log.d("AiFragment", "Summarize Week button clicked");
//            showSubOptions("Summarize");
//        });
//
//        // Reschedule Tasks button
//        binding.buttonRescheduleTasks.setOnClickListener(v -> {
//            Log.d("AiFragment", "Reschedule Tasks button clicked");
//            showDatePicker();
//        });
//
//        // Suggest Free Time Slots button
//        binding.buttonSuggestTimeSlots.setOnClickListener(v -> {
//            Log.d("AiFragment", "Suggest Free Time button clicked");
//            showSubOptions("Suggest");
//        });
//
//        return root;
//    }
//
//    // Method to show a date picker
//    private void showDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
//                (view, selectedYear, selectedMonth, selectedDay) -> {
//                    String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
//                    aiViewModel.sendMessage("Tasks for " + selectedDate);
//                    showTimeSelection();
//                }, year, month, day);
//
//        datePickerDialog.show();
//    }
//
//    // Method to show time selection prompt
//    private void showTimeSelection() {
//        aiViewModel.sendMessage("Please confirm a new time for the task.");
//    }
//
//    // Method to handle sub-options (e.g., Daily, Weekly, Monthly)
//    private void showSubOptions(String type) {
//        if ("Summarize".equals(type)) {
//            aiViewModel.sendMessage("Choose: Daily, Weekly, or Monthly");
//        } else if ("Suggest".equals(type)) {
//            aiViewModel.sendMessage("Choose time frame: Daily, Weekly, or Monthly");
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}


package edu.neu.final_project_group_4.ui.ai;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Calendar;
import edu.neu.final_project_group_4.databinding.FragmentAiBinding;

public class AiFragment extends Fragment {
    private FragmentAiBinding binding;
    private ChatAdapter chatAdapter;
    private AiViewModel aiViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d("AiFragment", "Fragment created");

        // Initialize ViewModel and ChatAdapter
        aiViewModel = new ViewModelProvider(this).get(AiViewModel.class);
        chatAdapter = new ChatAdapter();

        // Setup RecyclerView
        binding.recyclerViewResponses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewResponses.setAdapter(chatAdapter);

        // Observe chat messages
        aiViewModel.getChatMessages().observe(getViewLifecycleOwner(), messages -> {
            chatAdapter.setMessages(messages);
            binding.recyclerViewResponses.scrollToPosition(messages.size() - 1);
        });

        // Show initial "Ask GenAI" button and hide other elements
        showInitialState();

        // Setup click listeners
        setupClickListeners();

        return root;
    }

    private void showInitialState() {
        // Initially show only the "Ask GenAI" button
        binding.buttonAskGenAi.setVisibility(View.VISIBLE);

        // Hide the chat area and scroll options
        binding.recyclerViewResponses.setVisibility(View.GONE);
        binding.scrollViewOptions.setVisibility(View.GONE);

        // Hide the bottom menu initially
        binding.collapsedMenu.setVisibility(View.VISIBLE);
        binding.expandedMenu.setVisibility(View.GONE);

        // Hide the task buttons initially
        binding.buttonSummarizeWeek.setVisibility(View.GONE);
        binding.buttonRescheduleTasks.setVisibility(View.GONE);
        binding.buttonSuggestTimeSlots.setVisibility(View.GONE);
    }


    private void setupClickListeners() {
        // Ask GenAI button click
        binding.buttonAskGenAi.setOnClickListener(v -> {
            Log.d("AiFragment", "Ask GenAI button clicked");
            binding.buttonAskGenAi.setVisibility(View.GONE);

            // Show the main options and chat area
            binding.scrollViewOptions.setVisibility(View.VISIBLE);
            binding.recyclerViewResponses.setVisibility(View.VISIBLE);

            // Show the option buttons
            binding.buttonSummarizeWeek.setVisibility(View.VISIBLE);
            binding.buttonRescheduleTasks.setVisibility(View.VISIBLE);
            binding.buttonSuggestTimeSlots.setVisibility(View.VISIBLE);

            // Send initial AI message
            aiViewModel.sendMessage("What can I help you with today?");
        });

        // Summarize Week button
        binding.buttonSummarizeWeek.setOnClickListener(v -> {
            Log.d("AiFragment", "Summarize tasks button clicked");
            aiViewModel.sendMessage("Summarize my tasks");
            showTimeframeOptions();
        });

        // Reschedule Tasks button
        binding.buttonRescheduleTasks.setOnClickListener(v -> {
            Log.d("AiFragment", "Reschedule Tasks button clicked");
            showDatePicker();
        });

        // Suggest Free Time Slots button
        binding.buttonSuggestTimeSlots.setOnClickListener(v -> {
            Log.d("AiFragment", "Suggest Free Time button clicked");
            aiViewModel.sendMessage("Suggest free time slots");
            showTimeframeOptions();
        });
    }

    private void showTimeframeOptions() {
        // Hide main option buttons
        binding.buttonSummarizeWeek.setVisibility(View.GONE);
        binding.buttonRescheduleTasks.setVisibility(View.GONE);
        binding.buttonSuggestTimeSlots.setVisibility(View.GONE);

        // Create and show timeframe buttons
        Button dailyButton = new Button(getContext());
        Button weeklyButton = new Button(getContext());
        Button monthlyButton = new Button(getContext());

        dailyButton.setText("Daily");
        weeklyButton.setText("Weekly");
        monthlyButton.setText("Monthly");

        // Add buttons to the LinearLayout inside ScrollView
        ViewGroup buttonLayout = binding.buttonLayout;
        buttonLayout.addView(dailyButton);
        buttonLayout.addView(weeklyButton);
        buttonLayout.addView(monthlyButton);

        // Set click listeners for timeframe buttons
        dailyButton.setOnClickListener(v -> handleTimeframeSelection("Daily"));
        weeklyButton.setOnClickListener(v -> handleTimeframeSelection("Weekly"));
        monthlyButton.setOnClickListener(v -> handleTimeframeSelection("Monthly"));
    }

    private void handleTimeframeSelection(String timeframe) {
        aiViewModel.sendMessage("Selected timeframe: " + timeframe);
        // Clear the button layout
        ViewGroup buttonLayout = binding.buttonLayout;
        buttonLayout.removeAllViews();

        // Show the main option buttons again
        binding.buttonSummarizeWeek.setVisibility(View.VISIBLE);
        binding.buttonRescheduleTasks.setVisibility(View.VISIBLE);
        binding.buttonSuggestTimeSlots.setVisibility(View.VISIBLE);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    String selectedDate = String.format("%02d/%02d/%d", month + 1, day, year);
                    aiViewModel.sendMessage("Selected date: " + selectedDate);
                    showConfirmationButton(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showConfirmationButton(String selectedDate) {
        // Create and show a confirmation button
        Button confirmButton = new Button(getContext());
        confirmButton.setText("Confirm Selection");

        // Add the button to the layout
        ViewGroup buttonLayout = binding.buttonLayout;
        buttonLayout.addView(confirmButton);

        confirmButton.setOnClickListener(v -> {
            aiViewModel.sendMessage("Confirmed date: " + selectedDate);
            // Here you would typically fetch tasks for the selected date
            // For now, we'll just show a mock response
            aiViewModel.sendMessage("Tasks found for " + selectedDate + ":\n" +
                    "1. Morning Meeting (9:00 AM)\n" +
                    "2. Project Review (2:00 PM)\n" +
                    "Please select a new time for these tasks.");

            // Remove the confirmation button
            buttonLayout.removeView(confirmButton);
        });
    }

    // In AiFragment.java

    private void setupBottomMenu() {
        View collapsedMenu = binding.collapsedMenu;
        View expandedMenu = binding.expandedMenu;

        // Show the expanded menu when the collapsed menu is clicked
        collapsedMenu.setOnClickListener(v -> {
            collapsedMenu.setVisibility(View.GONE);
            expandedMenu.setVisibility(View.VISIBLE);
        });

        // Close the expanded menu when the close button is clicked
        binding.closeMenuButton.setOnClickListener(v -> {
            expandedMenu.setVisibility(View.GONE);
            collapsedMenu.setVisibility(View.VISIBLE);
        });

        // Set menu button click listeners
        binding.summarizeTasksButton.setOnClickListener(v -> {
            aiViewModel.sendMessage("Summarize my tasks");
            expandedMenu.setVisibility(View.GONE);
            collapsedMenu.setVisibility(View.VISIBLE);
        });

        binding.rescheduleTasksButton.setOnClickListener(v -> {
            aiViewModel.sendMessage("Reschedule tasks");
            expandedMenu.setVisibility(View.GONE);
            collapsedMenu.setVisibility(View.VISIBLE);
        });

        binding.suggestTimeButton.setOnClickListener(v -> {
            aiViewModel.sendMessage("Suggest free time slots");
            expandedMenu.setVisibility(View.GONE);
            collapsedMenu.setVisibility(View.VISIBLE);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}