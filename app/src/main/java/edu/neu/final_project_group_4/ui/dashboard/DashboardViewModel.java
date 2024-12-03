package edu.neu.final_project_group_4.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.neu.final_project_group_4.utils.User;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> userFullName;
    private final MutableLiveData<String> userDescription;

    public DashboardViewModel() {
        userFullName = new MutableLiveData<>();
        userDescription = new MutableLiveData<>();
        loadUserDescription(true);
    }

    public void loadUserFullName() {
        userFullName.setValue(User.getInstance().getFullName());
    }

    public LiveData<String> getUserFullName() {
        return userFullName;
    }

    public void loadUserDescription(boolean needFetch) {
        if (needFetch) {
            User.getInstance().fetchUserDescription();
        }
        userDescription.setValue(User.getInstance().getUserDescription());
    }

    public LiveData<String> getUserDescription() {
        return userDescription;
    }
}