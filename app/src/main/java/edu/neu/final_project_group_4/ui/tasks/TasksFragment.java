package edu.neu.final_project_group_4.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.final_project_group_4.R;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private List<Task> dailyTasks;
    private CalendarView calendarView;
    private boolean isDetailedView = false;  // Track view state

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        if (getArguments() != null) {
            String taskType = getArguments().getString("task_type", "No Task Type");
            TextView textView = root.findViewById(R.id.text_task_type);
            textView.setText("Task Type: " + taskType);
        }

        calendarView = root.findViewById(R.id.calendar_view);
        recyclerView = root.findViewById(R.id.recycler_view_daily_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dailyTasks = new ArrayList<>();
        tasksAdapter = new TasksAdapter(dailyTasks);
        recyclerView.setAdapter(tasksAdapter);

        // Toggle button to switch views
        Button toggleViewButton = root.findViewById(R.id.toggle_view_button);
        toggleViewButton.setOnClickListener(v -> toggleView());

        // "Add Task" button to add new tasks
        root.findViewById(R.id.add_task_button).setOnClickListener(v -> {
            // Display a Toast or launch a new activity/fragment for adding tasks
            Toast.makeText(getContext(), "Add Task button clicked!", Toast.LENGTH_SHORT).show();
            // Implement actual add task logic or navigation here
        });

        // Listener for calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            updateDailyTasks(selectedDate);
        });

        return root;
    }

    private void toggleView() {
        isDetailedView = !isDetailedView;
        if (isDetailedView) {
            calendarView.setVisibility(View.GONE);
            updateDailyTasksWithTime("Sample Date"); // Replace with the actual selected date
        } else {
            calendarView.setVisibility(View.VISIBLE);
            updateDailyTasks("Selected Date");
        }
    }

    private void updateDailyTasks(String selectedDate) {
        dailyTasks.clear();
        dailyTasks.add(new Task("9:00 AM", "Task 1", "Task details for Task 1", "Address: Location A"));
        dailyTasks.add(new Task("11:00 AM", "Task 2", "Task details for Task 2", "Address: Location B"));
        dailyTasks.add(new Task("2:00 PM", "Task 3", "Task details for Task 3", "Address: Location C"));
        tasksAdapter.notifyDataSetChanged();
    }

    private void updateDailyTasksWithTime(String selectedDate) {
        dailyTasks.clear();
        dailyTasks.add(new Task("9:00 AM", "Task A", "Details for Task A", "Address: Location X"));
        dailyTasks.add(new Task("11:00 AM", "Task B", "Details for Task B", "Address: Location Y"));
        dailyTasks.add(new Task("2:00 PM", "Task C", "Details for Task C", "Address: Location Z"));
        tasksAdapter.notifyDataSetChanged();
    }

    private static class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
        private final List<Task> tasks;

        TasksAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_card, parent, false);
            return new TaskViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.taskTime.setText(task.getTime());
            holder.taskTitle.setText(task.getTitle());
            holder.taskDetails.setText(task.getDetails());
            holder.taskAddress.setText(task.getAddress());
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        static class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView taskTime, taskTitle, taskDetails, taskAddress;

            TaskViewHolder(View itemView) {
                super(itemView);
                taskTime = itemView.findViewById(R.id.task_time);
                taskTitle = itemView.findViewById(R.id.task_title);
                taskDetails = itemView.findViewById(R.id.task_details);
                taskAddress = itemView.findViewById(R.id.task_address);
            }
        }
    }

    // Task data model
    private static class Task {
        private final String time;
        private final String title;
        private final String details;
        private final String address;

        public Task(String time, String title, String details, String address) {
            this.time = time;
            this.title = title;
            this.details = details;
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }

        public String getAddress() {
            return address;
        }
    }
}
