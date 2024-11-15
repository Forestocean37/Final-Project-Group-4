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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.models.TaskModel;
import edu.neu.final_project_group_4.utils.Task;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private CalendarView calendarView;
    private boolean isDetailedView = false;  // Track view state
    private List<TaskModel> taskList;
    // this is use to filter the task for a specific date. default value is today
    private List<TaskModel> todayTasks = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String selectedDate = dateFormat.format(new Date());

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

        //get the task data from firebase
        Task.getInstance().fetchTasks(()->{
            taskList = Task.getInstance().getTaskList();
            getTodayTasks(taskList,selectedDate);
            tasksAdapter = new TasksAdapter(todayTasks);
            recyclerView.setAdapter(tasksAdapter);
        });



        // Toggle button to switch views
        Button toggleViewButton = root.findViewById(R.id.toggle_view_button);
        toggleViewButton.setOnClickListener(v -> toggleView());

        // "Add Task" button to add new tasks
        root.findViewById(R.id.add_task_button).setOnClickListener(v -> {
            //jump to add Task fragment
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.addTaskFragment);
        });

        // Listener for calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format selected date to "dd-MM-yyyy"
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            selectedDate = dateFormat.format(calendar.getTime()); // Update selectedDate
            //would cause change in todayTasks
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

    // Function to filter today's tasks
    private List<TaskModel> getTodayTasks(List<TaskModel> allTasks, String selectedDate) {
        todayTasks.clear(); // in case there are

        // Loop through tasks to find today's tasks
        for (TaskModel task : allTasks) {
            String startTime = task.getStartTime();

            // Extract date part from startTime
            if (startTime != null && !startTime.isEmpty()) {
                try {
                    Date taskDate = dateFormat.parse(startTime.split(" ")[0]); // Only date part (dd-MM-yyyy)
                    String taskDateString = dateFormat.format(taskDate);

                    // Compare with today's date
                    if (selectedDate.equals(taskDateString)) {
                        todayTasks.add(task); // Add to today's tasks list
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return todayTasks;
    }


    private void updateDailyTasks(String selectedDate) {
        //update to the new date task
        getTodayTasks(taskList,selectedDate);
        tasksAdapter.notifyDataSetChanged();
    }

    private void updateDailyTasksWithTime(String selectedDate) {
//        dailyTasks.add(new Task("9:00 AM", "Task A", "Details for Task A", "Address: Location X"));
//        dailyTasks.add(new Task("11:00 AM", "Task B", "Details for Task B", "Address: Location Y"));
//        dailyTasks.add(new Task("2:00 PM", "Task C", "Details for Task C", "Address: Location Z"));
        tasksAdapter.notifyDataSetChanged();
    }

    private static class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
        private final List<TaskModel> tasks;

        TasksAdapter(List<TaskModel> tasks) {
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
            TaskModel task = tasks.get(position);
            holder.taskTime.setText(task.getStartTime());
            holder.taskTitle.setText(task.getTitle());
            holder.taskDetails.setText(task.getDescription());
            holder.taskAddress.setText(task.getLocation().getAddress());
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

//    // Task data model
//    private static class Task {
//        private final String time;
//        private final String title;
//        private final String details;
//        private final String address;
//
//        public Task(String time, String title, String details, String address) {
//            this.time = time;
//            this.title = title;
//            this.details = details;
//            this.address = address;
//        }
//
//        public String getTime() {
//            return time;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDetails() {
//            return details;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//    }
}
