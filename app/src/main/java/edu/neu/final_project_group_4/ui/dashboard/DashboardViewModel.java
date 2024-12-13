package edu.neu.final_project_group_4.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.util.List;

import edu.neu.final_project_group_4.models.TaskModel;
import edu.neu.final_project_group_4.utils.Task;
import edu.neu.final_project_group_4.utils.User;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> userFullName;
    private final MutableLiveData<String> userEmail;
    private final MutableLiveData<String> monthRemainedTasks;
    private final MutableLiveData<String> userDescription;

    public DashboardViewModel() {
        userFullName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        monthRemainedTasks = new MutableLiveData<>();
        userDescription = new MutableLiveData<>();
        loadUserDescription(true);
    }

    public void loadUserFullName() {
        String localValue = User.getInstance().getLocalUserFullName();
        if (!localValue.isEmpty()) {
            userFullName.setValue(localValue);
            User.getInstance().clearLocalUserFullName();
        } else {
            userFullName.setValue(User.getInstance().getFullName());
        }
    }

    public LiveData<String> getUserFullName() {
        return userFullName;
    }

    public void loadUserEmail() {
        userEmail.setValue(User.getInstance().getEmail());
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public void loadRemainedTasks() {
        monthRemainedTasks.setValue(Integer.toString(monthRemainedTasks()));
    }

    public LiveData<String> getMonthRemainedTasks() {
        return monthRemainedTasks;
    }

    public void loadUserDescription(boolean needFetch) {
        if (needFetch) {
            User.getInstance().fetchUserDescription();
        }

        String localValue = User.getInstance().getLocalUserDescription();
        if (!localValue.isEmpty()) {
            userDescription.setValue(localValue);
            User.getInstance().clearLocalUserDescription();
        } else {
            userDescription.setValue(User.getInstance().getUserDescription());
        }
    }

    public LiveData<String> getUserDescription() {
        return userDescription;
    }

    private int monthRemainedTasks() {
        List<TaskModel> tasks = Task.getInstance().getTaskList();
        int taskCount = 0;
        LocalDateTime currentTime = LocalDateTime.now();

        for (TaskModel task : tasks) {
            LocalDateTime startTime = Task.getInstance().convertToTime(task.getStartTime());
            if (currentTime.isBefore(startTime) &&
                Task.getInstance().isSameMonth(currentTime, startTime) &&
                !task.isCompleted()) {
                taskCount += 1;
            }
        }

        return taskCount;
    }
}