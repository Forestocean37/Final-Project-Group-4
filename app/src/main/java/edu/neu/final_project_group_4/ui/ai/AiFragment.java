//package edu.neu.final_project_group_4.ui.ai;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
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
//        // Initialize ViewModel and ChatAdapter
//        aiViewModel = new ViewModelProvider(this).get(AiViewModel.class);
//        chatAdapter = new ChatAdapter();
//
//        // Setup RecyclerView
//        binding.recyclerViewResponses.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerViewResponses.setAdapter(chatAdapter);
//
//        // Observe chat messages
//        aiViewModel.getChatMessages().observe(getViewLifecycleOwner(), messages -> {
//            chatAdapter.setMessages(messages);
//            binding.recyclerViewResponses.scrollToPosition(messages.size() - 1);
//        });
//
//        // Show initial "Ask GenAI" button and hide other elements
//        showInitialState();
//
//        // Setup click listeners
//        setupClickListeners();
//
//        return root;
//    }
//
//    private void showInitialState() {
//        // Initially show only the "Ask GenAI" button
//        binding.buttonAskGenAi.setVisibility(View.VISIBLE);
//
//        // Hide the chat area and scroll options
//        binding.recyclerViewResponses.setVisibility(View.GONE);
//        binding.scrollViewOptions.setVisibility(View.GONE);
//
//        // Hide the bottom menu initially
//        binding.collapsedMenu.setVisibility(View.VISIBLE);
//        binding.expandedMenu.setVisibility(View.GONE);
//
//        // Hide the task buttons initially
//        binding.buttonSummarizeWeek.setVisibility(View.GONE);
//        binding.buttonRescheduleTasks.setVisibility(View.GONE);
//        binding.buttonSuggestTimeSlots.setVisibility(View.GONE);
//    }
//
//
//    private void setupClickListeners() {
//        // Ask GenAI button click
//        binding.buttonAskGenAi.setOnClickListener(v -> {
//            Log.d("AiFragment", "Ask GenAI button clicked");
//            binding.buttonAskGenAi.setVisibility(View.GONE);
//
//            // Show the main options and chat area
//            binding.scrollViewOptions.setVisibility(View.VISIBLE);
//            binding.recyclerViewResponses.setVisibility(View.VISIBLE);
//
//            // Show the option buttons
//            binding.buttonSummarizeWeek.setVisibility(View.VISIBLE);
//            binding.buttonRescheduleTasks.setVisibility(View.VISIBLE);
//            binding.buttonSuggestTimeSlots.setVisibility(View.VISIBLE);
//
//            // Send initial AI message
//            aiViewModel.sendMessage("What can I help you with today?");
//        });
//
//        // Summarize Week button
//        binding.buttonSummarizeWeek.setOnClickListener(v -> {
//            Log.d("AiFragment", "Summarize tasks button clicked");
//            aiViewModel.sendMessage("Summarize my tasks");
//            showTimeframeOptions();
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
//            aiViewModel.sendMessage("Suggest free time slots");
//            showTimeframeOptions();
//        });
//    }
//
//    private void showTimeframeOptions() {
//        // Hide main option buttons
//        binding.buttonSummarizeWeek.setVisibility(View.GONE);
//        binding.buttonRescheduleTasks.setVisibility(View.GONE);
//        binding.buttonSuggestTimeSlots.setVisibility(View.GONE);
//
//        // Create and show timeframe buttons
//        Button dailyButton = new Button(getContext());
//        Button weeklyButton = new Button(getContext());
//        Button monthlyButton = new Button(getContext());
//
//        dailyButton.setText("Daily");
//        weeklyButton.setText("Weekly");
//        monthlyButton.setText("Monthly");
//
//        // Add buttons to the LinearLayout inside ScrollView
//        ViewGroup buttonLayout = binding.buttonLayout;
//        buttonLayout.addView(dailyButton);
//        buttonLayout.addView(weeklyButton);
//        buttonLayout.addView(monthlyButton);
//
//        // Set click listeners for timeframe buttons
//        dailyButton.setOnClickListener(v -> handleTimeframeSelection("Daily"));
//        weeklyButton.setOnClickListener(v -> handleTimeframeSelection("Weekly"));
//        monthlyButton.setOnClickListener(v -> handleTimeframeSelection("Monthly"));
//    }
//
//    private void handleTimeframeSelection(String timeframe) {
//        aiViewModel.sendMessage("Selected timeframe: " + timeframe);
//        // Clear the button layout
//        ViewGroup buttonLayout = binding.buttonLayout;
//        buttonLayout.removeAllViews();
//
//        // Show the main option buttons again
//        binding.buttonSummarizeWeek.setVisibility(View.VISIBLE);
//        binding.buttonRescheduleTasks.setVisibility(View.VISIBLE);
//        binding.buttonSuggestTimeSlots.setVisibility(View.VISIBLE);
//    }
//
//    private void showDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                requireContext(),
//                (view, year, month, day) -> {
//                    String selectedDate = String.format("%02d/%02d/%d", month + 1, day, year);
//                    aiViewModel.sendMessage("Selected date: " + selectedDate);
//                    showConfirmationButton(selectedDate);
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    private void showConfirmationButton(String selectedDate) {
//        // Create and show a confirmation button
//        Button confirmButton = new Button(getContext());
//        confirmButton.setText("Confirm Selection");
//
//        // Add the button to the layout
//        ViewGroup buttonLayout = binding.buttonLayout;
//        buttonLayout.addView(confirmButton);
//
//        confirmButton.setOnClickListener(v -> {
//            aiViewModel.sendMessage("Confirmed date: " + selectedDate);
//            // Here you would typically fetch tasks for the selected date
//            // For now, we'll just show a mock response
//            aiViewModel.sendMessage("Tasks found for " + selectedDate + ":\n" +
//                    "1. Morning Meeting (9:00 AM)\n" +
//                    "2. Project Review (2:00 PM)\n" +
//                    "Please select a new time for these tasks.");
//
//            // Remove the confirmation button
//            buttonLayout.removeView(confirmButton);
//        });
//    }
//
//    // In AiFragment.java
//
//    private void setupBottomMenu() {
//        View collapsedMenu = binding.collapsedMenu;
//        View expandedMenu = binding.expandedMenu;
//
//        // Show the expanded menu when the collapsed menu is clicked
//        collapsedMenu.setOnClickListener(v -> {
//            collapsedMenu.setVisibility(View.GONE);
//            expandedMenu.setVisibility(View.VISIBLE);
//        });
//
//        // Close the expanded menu when the close button is clicked
//        binding.closeMenuButton.setOnClickListener(v -> {
//            expandedMenu.setVisibility(View.GONE);
//            collapsedMenu.setVisibility(View.VISIBLE);
//        });
//
//        // Set menu button click listeners
//        binding.summarizeTasksButton.setOnClickListener(v -> {
//            aiViewModel.sendMessage("Summarize my tasks");
//            expandedMenu.setVisibility(View.GONE);
//            collapsedMenu.setVisibility(View.VISIBLE);
//        });
//
//        binding.rescheduleTasksButton.setOnClickListener(v -> {
//            aiViewModel.sendMessage("Reschedule tasks");
//            expandedMenu.setVisibility(View.GONE);
//            collapsedMenu.setVisibility(View.VISIBLE);
//        });
//
//        binding.suggestTimeButton.setOnClickListener(v -> {
//            aiViewModel.sendMessage("Suggest free time slots");
//            expandedMenu.setVisibility(View.GONE);
//            collapsedMenu.setVisibility(View.VISIBLE);
//        });
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}



// menu bar success.

//package edu.neu.final_project_group_4.ui.ai;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import edu.neu.final_project_group_4.databinding.FragmentAiBinding;
//
//public class AiFragment extends Fragment {
//    private FragmentAiBinding binding;
//    private boolean isMenuExpanded = false;  // Track menu state
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentAiBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        initializeViews();
//        setupClickListeners();
//
//        return root;
//    }
//
//    private void initializeViews() {
//        // Initially, only the Ask GenAI button is visible
//        binding.buttonAskGenAi.setVisibility(View.VISIBLE);
//        binding.collapsedMenu.setVisibility(View.GONE);  // Collapsed menu is initially hidden
//        binding.expandedMenu.setVisibility(View.GONE);  // Expanded menu is initially hidden
//    }
//
//    private void setupClickListeners() {
//        // Ask GenAI Button
//        binding.buttonAskGenAi.setOnClickListener(v -> {
//            // When the Ask GenAI button is clicked, show the collapsed menu button
//            binding.buttonAskGenAi.setVisibility(View.GONE);
//            binding.collapsedMenu.setVisibility(View.VISIBLE);  // Show collapsed menu
//        });
//
//        // Collapsed Menu Button
//        binding.collapsedMenu.setOnClickListener(v -> toggleMenu());
//
//        // Option Buttons in the Expanded Menu
//        binding.buttonSummarize.setOnClickListener(v -> {
//            // Handle summarize button click
//            handleOptionButtonClick();
//            // Add your summarize logic here
//        });
//
//        binding.buttonReschedule.setOnClickListener(v -> {
//            // Handle reschedule button click
//            handleOptionButtonClick();
//            // Add your reschedule logic here
//        });
//
//        binding.buttonSuggest.setOnClickListener(v -> {
//            // Handle suggest button click
//            handleOptionButtonClick();
//            // Add your suggest logic here
//        });
//    }
//
//    private void handleOptionButtonClick() {
//        // Collapse the menu when any option button is clicked
//        collapseMenu();
//    }
//
//    private void collapseMenu() {
//        binding.expandedMenu.setVisibility(View.GONE);
//        binding.collapsedMenu.setVisibility(View.VISIBLE);
//        isMenuExpanded = false;
//    }
//
//    private void expandMenu() {
//        binding.expandedMenu.setVisibility(View.VISIBLE);
//        binding.collapsedMenu.setVisibility(View.GONE);
//        // Make the 3 buttons visible
//        binding.buttonSummarize.setVisibility(View.VISIBLE);
//        binding.buttonReschedule.setVisibility(View.VISIBLE);
//        binding.buttonSuggest.setVisibility(View.VISIBLE);
//        isMenuExpanded = true;
//    }
//
//    private void toggleMenu() {
//        if (isMenuExpanded) {
//            collapseMenu();
//        } else {
//            expandMenu();
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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Calendar;
import java.util.List;

import edu.neu.final_project_group_4.databinding.FragmentAiBinding;

public class AiFragment extends Fragment {

    private Button buttonPrioritize;
    private Button buttonViewInsights;
    private Button buttonBreakDownTask;
    private Button buttonAdjustDeadlines;
    private Button buttonReviewTasks;


    private FragmentAiBinding binding;
    private ChatAdapter chatAdapter;
    private AiViewModel aiViewModel;
    private boolean isMenuExpanded = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        // Initialize new buttons
        buttonPrioritize = binding.buttonPrioritize;
        buttonViewInsights = binding.buttonViewInsights;
        buttonBreakDownTask = binding.buttonBreakDownTask;
        buttonAdjustDeadlines = binding.buttonAdjustDeadlines;
        buttonReviewTasks = binding.buttonReviewTasks;


        // Show initial state
        showInitialState();
        setupClickListeners();

        return root;
    }

    private void showInitialState() {
        binding.buttonAskGenAi.setVisibility(View.VISIBLE);
        binding.recyclerViewResponses.setVisibility(View.GONE);
        binding.collapsedMenu.setVisibility(View.GONE);
        binding.expandedMenu.setVisibility(View.GONE);
        binding.scrollViewOptions.setVisibility(View.GONE);
        binding.buttonLayout.removeAllViews();
    }

    private void setupClickListeners() {
        // Ask GenAI button
        binding.buttonAskGenAi.setOnClickListener(v -> {
            binding.buttonAskGenAi.setVisibility(View.GONE);
            binding.collapsedMenu.setVisibility(View.VISIBLE);
            binding.recyclerViewResponses.setVisibility(View.VISIBLE);
            aiViewModel.sendMessageToAI("What can I help you with today?");
        });

        // Collapsed menu (< icon)
        binding.collapsedMenu.setOnClickListener(v -> {
            if (isMenuExpanded) {
                collapseMenu();
            } else {
                expandMenu();
            }
        });

        // Summarize button
        binding.buttonSummarize.setOnClickListener(v -> {
            collapseMenu();
            showTimeframeOptions("summarize");
        });

        // Reschedule button
        binding.buttonReschedule.setOnClickListener(v -> {
            collapseMenu();
            showTaskSelection();
        });

        // Suggest button
        binding.buttonSuggest.setOnClickListener(v -> {
            collapseMenu();
            showTimeframeOptions("suggest");
        });

        // Prioritize button
        buttonPrioritize.setOnClickListener(v -> {
            collapseMenu();
            aiViewModel.prioritizeTasks();
        });

// View Insights button
        buttonViewInsights.setOnClickListener(v -> {
            collapseMenu();
            aiViewModel.viewInsights();
        });

// Break Down Task button
        buttonBreakDownTask.setOnClickListener(v -> {
            collapseMenu();
            showTaskSelectionForBreakdown();
        });

// Adjust Deadlines button
        buttonAdjustDeadlines.setOnClickListener(v -> {
            collapseMenu();
            aiViewModel.adjustDeadlines();
        });

// Review Tasks button
        buttonReviewTasks.setOnClickListener(v -> {
            collapseMenu();
            aiViewModel.reviewTasks();
        });


    }

    private void expandMenu() {
        isMenuExpanded = true;
        binding.expandedMenu.setVisibility(View.VISIBLE);
        binding.buttonSummarize.setVisibility(View.VISIBLE);
        binding.buttonReschedule.setVisibility(View.VISIBLE);
        binding.buttonSuggest.setVisibility(View.VISIBLE);

        // Show new buttons
        buttonPrioritize.setVisibility(View.VISIBLE);
        buttonViewInsights.setVisibility(View.VISIBLE);
        buttonBreakDownTask.setVisibility(View.VISIBLE);
        buttonAdjustDeadlines.setVisibility(View.VISIBLE);
        buttonReviewTasks.setVisibility(View.VISIBLE);
    }

    private void collapseMenu() {
        isMenuExpanded = false;
        binding.expandedMenu.setVisibility(View.GONE);
        binding.buttonSummarize.setVisibility(View.GONE);
        binding.buttonReschedule.setVisibility(View.GONE);
        binding.buttonSuggest.setVisibility(View.GONE);

        // Hide new buttons
        buttonPrioritize.setVisibility(View.GONE);
        buttonViewInsights.setVisibility(View.GONE);
        buttonBreakDownTask.setVisibility(View.GONE);
        buttonAdjustDeadlines.setVisibility(View.GONE);
        buttonReviewTasks.setVisibility(View.GONE);
    }

    // Show options for Summarize and Suggest
    private void showTimeframeOptions(String actionType) {
        binding.buttonLayout.removeAllViews();
        binding.scrollViewOptions.setVisibility(View.VISIBLE);

        String[] timeframes = {"Daily", "Weekly", "Monthly"};

        for (String timeframe : timeframes) {
            Button timeframeButton = new Button(getContext());
            timeframeButton.setText(timeframe);
            timeframeButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            timeframeButton.setOnClickListener(v -> {
                handleTimeframeSelection(actionType, timeframe);
                binding.scrollViewOptions.setVisibility(View.GONE);
            });

            binding.buttonLayout.addView(timeframeButton);
        }
    }

    private void handleTimeframeSelection(String actionType, String timeframe) {
        if (actionType.equals("summarize")) {
            aiViewModel.summarizeTasks(timeframe);
        } else if (actionType.equals("suggest")) {
            aiViewModel.suggestFreeTimeSlots(timeframe);
        } else {
            aiViewModel.sendMessageToAI("Invalid action.");
        }
        binding.buttonLayout.removeAllViews();
    }


    // Show task selection for Reschedule
    private void showTaskSelection() {
        binding.buttonLayout.removeAllViews();
        binding.scrollViewOptions.setVisibility(View.VISIBLE);

        List<AiViewModel.Task> tasks = aiViewModel.getTasks();
        if (tasks.isEmpty()) {
            aiViewModel.sendMessageToAI("No tasks available to reschedule.");
            return;
        }

        for (AiViewModel.Task task : tasks) {
            Button taskButton = new Button(getContext());
            taskButton.setText(task.getTitle() + " - " + task.getDate());
            taskButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            taskButton.setOnClickListener(v -> {
                aiViewModel.setSelectedTask(task);
                aiViewModel.sendMessageToAI("You've chosen to reschedule: " + task.getTitle());
                showDatePicker(task);
            });

            binding.buttonLayout.addView(taskButton);
        }
    }


    private void showTaskSelectionForBreakdown() {
        binding.buttonLayout.removeAllViews();
        binding.scrollViewOptions.setVisibility(View.VISIBLE);

        List<AiViewModel.Task> tasks = aiViewModel.getTasks();
        if (tasks.isEmpty()) {
            aiViewModel.addMessage("No tasks available to break down.", false);
            return;
        }

        for (AiViewModel.Task task : tasks) {
            Button taskButton = new Button(getContext());
            taskButton.setText(task.getTitle());
            taskButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            taskButton.setOnClickListener(v1 -> {
                aiViewModel.setSelectedTask(task);
                aiViewModel.breakDownTask(task);
                binding.scrollViewOptions.setVisibility(View.GONE);
                binding.buttonLayout.removeAllViews();
            });

            binding.buttonLayout.addView(taskButton);
        }
    }


    private void showDatePicker(AiViewModel.Task selectedTask) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    String newDate = String.format("%02d/%02d/%d", month + 1, day, year);
                    aiViewModel.sendMessageToAI("Rescheduled '" + selectedTask.getTitle() + "' to " + newDate);
                    showConfirmRescheduleButton(selectedTask, newDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showConfirmRescheduleButton(AiViewModel.Task task, String newDate) {
        binding.buttonLayout.removeAllViews();

        Button confirmButton = new Button(getContext());
        confirmButton.setText("Confirm Reschedule for " + newDate);
        confirmButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        confirmButton.setOnClickListener(v -> {
            aiViewModel.sendMessageToAI("Task '" + task.getTitle() + "' rescheduled to " + newDate);
            aiViewModel.confirmReschedule(task, newDate);
            binding.scrollViewOptions.setVisibility(View.GONE);
            binding.buttonLayout.removeAllViews();
        });

        binding.buttonLayout.addView(confirmButton);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}