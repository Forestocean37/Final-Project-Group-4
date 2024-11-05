package edu.neu.final_project_group_4.ui.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AiViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is AI fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
