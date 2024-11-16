package edu.neu.final_project_group_4.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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

    static Task taskAPI = Task.getInstance();
    private static RecyclerView recyclerView;
    private static TasksAdapter tasksAdapter;
    private CalendarView calendarView;
    private TextView tv_no_task;
    private boolean isDetailedView = false;  // Track view state
    private static List<TaskModel> taskList;
    // this is use to filter the task for a specific date. default value is today
    private static List<TaskModel> todayTasks = new ArrayList<>();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    static String selectedDate = dateFormat.format(new Date());

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
        tv_no_task = root.findViewById(R.id.no_task);

        //get the task data from firebase
        taskAPI.fetchTasks(()->{
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
        } else {
            calendarView.setVisibility(View.VISIBLE);
        }
    }

    // Function to filter today's tasks
    private static List<TaskModel> getTodayTasks(List<TaskModel> allTasks, String selectedDate) {
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
        if(todayTasks.size() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            tv_no_task.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            tv_no_task.setVisibility(View.INVISIBLE);
        }
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
            holder.taskType.setText(task.getType());
            holder.taskTime.setText(task.getStartTime());
            holder.taskTitle.setText(task.getTitle());
            holder.taskDetails.setText(task.getDescription());
            holder.taskAddress.setText(task.getLocation().getAddress());

            // Determine if the task is outdated
            boolean isOutdated = isTaskOutdated(task.getStartTime());

            //click event of our button
//            holder.btnEditTask.setOnClickListener(...);
            holder.btnDeleteTask.setOnClickListener(v -> {
                // Create and show the delete confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Handle the delete operation here
                            taskAPI.deleteTask(task);

                            //fetch the latest data again from firebase
                            taskAPI.fetchTasks(()->{
                                taskList = Task.getInstance().getTaskList();
                                getTodayTasks(taskList,selectedDate);
                                tasksAdapter.notifyDataSetChanged();
                            });
                            dialog.dismiss();; // Custom method to delete the task
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        });

                // Show the dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });

            // Determine background color based on task type
            int backgroundColor;
            if (isOutdated) {
                backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.outdated_task);
            }else{
                switch (task.getType()) {
                    case "Personal":
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.personal_task);
                        break;
                    case "Free":
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.free_task);
                        break;
                    case "Social":
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.social_task);
                        break;
                    case "Work":
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.work_task);
                        break;
                    case "To do list":
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.to_do_task);
                        break;
                    default:
                        backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.white); // Optional: Default color
                        break;
                }
            }
            // Set the background color of the item
            holder.itemView.setBackgroundColor(backgroundColor);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        static class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView taskType,taskTime, taskTitle, taskDetails, taskAddress;
            ImageButton btnEditTask, btnDeleteTask;

            TaskViewHolder(View itemView) {
                super(itemView);
                taskType = itemView.findViewById(R.id.task_type);
                taskTime = itemView.findViewById(R.id.task_time);
                taskTitle = itemView.findViewById(R.id.task_title);
                taskDetails = itemView.findViewById(R.id.task_details);
                taskAddress = itemView.findViewById(R.id.task_address);
                btnEditTask = itemView.findViewById(R.id.btnEditTask);
                btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
            }
        }
    }

    //check the task is outdated, need to render the outdated task to gray
    private static boolean isTaskOutdated(String startTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

        try {
            // Parse task's start time
            Date taskDate = dateFormat.parse(startTime);

            // Get the current date and time
            Date currentDate = new Date();

            // Compare task date with the current date
            return taskDate != null && taskDate.before(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false; // Default to not outdated if parsing fails
    }
}
