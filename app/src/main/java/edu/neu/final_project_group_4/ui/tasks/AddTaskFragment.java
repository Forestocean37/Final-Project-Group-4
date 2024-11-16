package edu.neu.final_project_group_4.ui.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.models.LocationModel;
import edu.neu.final_project_group_4.models.TaskModel;
import edu.neu.final_project_group_4.utils.Task;
import edu.neu.final_project_group_4.utils.User;


public class AddTaskFragment extends Fragment {

    Task taskAPI = Task.getInstance();

    private TextView profileBtn, tvDate, tvTime, tvGreeting,tvAddTask;
    private ImageButton btnDatePicker, btnTimePicker;
    private ArrayList<Button> buttons;
    private HashMap<Button, Integer> buttonColors; // Store original colors for each button
    private Button btnAddTask;
    private TextInputEditText taskDetailInput, taskTitleInput, locationInput;

    // the task data need to create
    private String type;
    private String title;
    private String detail;
    private String startTime; // To store final "dd-MM-yyyy HH:mm" formatted value
    private String location;

    private int selectedYear, selectedMonth, selectedDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_task, container, false);
        View root = inflater.inflate(R.layout.fragment_add_task, container, false); // Replace with your layout name

        tvDate = root.findViewById(R.id.tvDate);
        tvTime = root.findViewById(R.id.tvTime);
        tvAddTask = root.findViewById(R.id.tvAddTask);
        taskDetailInput = root.findViewById(R.id.taskDetailInput);
        taskTitleInput = root.findViewById(R.id.taskTitleInput);
        locationInput = root.findViewById(R.id.locationInput);
        btnDatePicker = root.findViewById(R.id.btnDatePicker);
        btnTimePicker = root.findViewById(R.id.btnTimePicker);
        // Handle date picker
        btnDatePicker.setOnClickListener(v -> showDatePicker());

        // Handle time picker
        btnTimePicker.setOnClickListener(v -> {
            if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0) {
                // Call TimePicker with the previously selected date
                showTimePicker(selectedYear, selectedMonth, selectedDay);
            } else {
                // Handle case where no date has been selected
                Toast.makeText(requireContext(), "Please select a date first!", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize buttons
        buttons = new ArrayList<>();
        buttons.add(root.findViewById(R.id.btnPersonal));
        buttons.add(root.findViewById(R.id.btnFree));
        buttons.add(root.findViewById(R.id.btnSocial));
        buttons.add(root.findViewById(R.id.btnWork));
        buttons.add(root.findViewById(R.id.btnTodoList));

        // Store original colors of buttons
        buttonColors = new HashMap<>();
        buttonColors.put(buttons.get(0), R.color.personal_task); // Personal button color
        buttonColors.put(buttons.get(1), R.color.free_task); // Free button color
        buttonColors.put(buttons.get(2), R.color.social_task); // Social button color
        buttonColors.put(buttons.get(3), R.color.work_task); // Work button color
        buttonColors.put(buttons.get(4), R.color.to_do_task); // To Do List button color

        // Set click listener for each button
        for (Button button : buttons) {
            button.setOnClickListener(v -> handleButtonClick(button));
        }

        // Set initials dynamically if needed
        profileBtn = root.findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_dashboard);
        });
        profileBtn.setText(User.getInstance().getFullName());

        //set Hello information to user name
        tvGreeting = root.findViewById(R.id.tvGreeting);
        String helloText = "Hello " + User.getInstance().getFullName();
        tvGreeting.setText(helloText);

        btnAddTask = root.findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "Type: " + startTime, Toast.LENGTH_SHORT).show();
            if(validateInputs(root)){
                //create a task
                title = taskTitleInput.getText().toString();
                detail = taskDetailInput.getText().toString();
                location = locationInput.getText().toString();
                List<String> people = new ArrayList<>();
                people.add(User.getInstance().getEmail());
                LocationModel newLocation = new LocationModel(location, location);
                TaskModel newTask = new TaskModel(title,type,detail,startTime,people,newLocation);

                //checn if it is a create task or edit task
                if (getArguments() != null && getArguments().containsKey("task")){
                    TaskModel oldTask = getArguments().getParcelable("task");
                    taskAPI.updateTask(oldTask.getTaskId(),newTask);
                    Toast.makeText(requireContext(), "successfully Edit task", Toast.LENGTH_SHORT).show();
                }else{
                    taskAPI.addNewTask(newTask);
                    Toast.makeText(requireContext(), "successfully Add task", Toast.LENGTH_SHORT).show();
                }
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_home);

            }else{
                Toast.makeText(requireContext(), "Type: please fill out all the blank", Toast.LENGTH_SHORT).show();
            }

        });

        // Check if task data is passed in the arguments
        if (getArguments() != null && getArguments().containsKey("task")) {
            TaskModel task = getArguments().getParcelable("task");
            if (task != null) {
                // Task data is available - set up the fragment for editing
//                Toast.makeText(requireContext(), "Edit part", Toast.LENGTH_SHORT).show();
//                Log.e("Task Model", task.toString());
                resetTaskUI(task);
            }
        } else {
            // No task data - set up the fragment for creating a new task
            Toast.makeText(requireContext(), "other part", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void resetTaskUI(TaskModel task) {
        btnAddTask.setText("Edit Task");
        tvAddTask.setText("Edit a Task");

        // Set the tvDate text from the task's startTime (split by space)
        if (task.getStartTime() != null && !task.getStartTime().isEmpty()) {
            String[] dateTime = task.getStartTime().split(" ");
            if (dateTime.length == 2) {
                tvDate.setText(dateTime[0]); // Set the date part
                tvTime.setText(dateTime[1]); // Set the time part
            }
            startTime = task.getStartTime();
        }

        // Set buttons' colors according to the task type
        String taskType = task.getType(); // Convert to uppercase
        for (Button button : buttons) {
            String buttonText = button.getText().toString(); // Get button text
            if (buttonText.equalsIgnoreCase(taskType)) {
                // Set the color to selected for the matching button
                button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.btn_selected));
                type = task.getType();
            }
        }

        // Set taskDetailInput text from the task's description
        if (task.getDescription() != null) {
            taskDetailInput.setText(task.getDescription());
        }

        // Set taskTitleInput text from the task's title
        if (task.getTitle() != null) {
            taskTitleInput.setText(task.getTitle());
        }

        // Set locationInput text from the task's location address
        if (task.getLocation() != null && task.getLocation().getAddress() != null) {
            locationInput.setText(task.getLocation().getAddress());
        }
    }

    private boolean validateInputs(View root) {
        // Check if taskDetailInput is empty
        if (taskDetailInput.getText() == null || taskDetailInput.getText().toString().trim().isEmpty()) {
            taskDetailInput.setError("Task detail cannot be empty");
            return false;
        }

        // Check if taskTitleInput is empty
        if (taskTitleInput.getText() == null || taskTitleInput.getText().toString().trim().isEmpty()) {
            taskTitleInput.setError("Task title cannot be empty");
            return false;
        }

        // Check if locationInput is empty
        if (locationInput.getText() == null || locationInput.getText().toString().trim().isEmpty()) {
            locationInput.setError("Location cannot be empty");
            return false;
        }

        if(type == null || startTime == null){
            return false;
        }


        // All fields are valid
        return true;
    }

    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, yearPicked, monthPicked, dayPicked) -> {
                    // Format and display the selected date
                    selectedYear = yearPicked;
                    selectedMonth = monthPicked;
                    selectedDay = dayPicked;
                    String formattedDate = String.format("%02d-%02d-%04d", dayPicked, monthPicked + 1, yearPicked);
                    tvDate.setText(formattedDate);
                    showTimePicker(selectedYear, selectedMonth, selectedDay);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(int year, int month, int day) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, selectedHour, selectedMinute) -> {
                    // Format and display the selected time
                    String formattedTime = String.format("%02d:%02d %s",
                            (selectedHour % 12 == 0) ? 12 : selectedHour % 12,
                            selectedMinute,
                            (selectedHour >= 12) ? "PM" : "AM");
                    tvTime.setText(formattedTime);
                    startTime = String.format("%02d-%02d-%04d %02d:%02d",
                            day, month + 1, year, selectedHour, selectedMinute);
                }, hour, minute, false); // Use 24-hour format: false for 12-hour
        timePickerDialog.show();
    }

    private String getMonthName(int month) {
        // Convert month index to name
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    // the below is handle the click activity for 5 button
    private void handleButtonClick(Button selectedButton) {
        // Reset all buttons to their original styles
        for (Button button : buttons) {
            int colorResId = buttonColors.get(button);
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), colorResId));;
        }

        // selected button become gray
        selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.btn_selected));
        type = selectedButton.getText().toString();
    }

}