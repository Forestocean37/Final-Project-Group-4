package edu.neu.final_project_group_4.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.databinding.FragmentHomeBinding;
import edu.neu.final_project_group_4.models.TaskModel;
import edu.neu.final_project_group_4.utils.Task;
import edu.neu.final_project_group_4.utils.User;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Set up button click listeners in HomeFragment
        binding.buttonPersonal.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task_type", "Personal");
            Navigation.findNavController(v).navigate(R.id.navigation_tasks, bundle);
        });

        binding.buttonSocial.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task_type", "Social");
            Navigation.findNavController(v).navigate(R.id.navigation_tasks, bundle);
        });

        String helloText = "Hello User: " + User.getInstance().getFullName();
        binding.textHelloUser.setText(helloText);

        Task.getInstance().fetchTasks(() -> {
            String taskNum = Task.getInstance().getTasksToday() + " tasks today";
            binding.textTitle2.setText(taskNum);

            // Temporary using string. Will be changed to a component (task card)
            TaskModel nextTask = Task.getInstance().getNextTask();
            String people = nextTask.getPeople().size() > 1
                    ? nextTask.getPeople().get(0) + "..."
                    : nextTask.getPeople().get(0);
            String location = nextTask.getLocation().getType().equals("Offline")
                    ? nextTask.getLocation().getAddress()
                    : nextTask.getLocation().getType();

            String nextTaskText = String.format("%s\nDescription: %.20s\n%s\n%s\n%s",
                    nextTask.getTitle(), nextTask.getDescription(),
                    nextTask.getStartTime().split(" ")[1], people, location);
            binding.textUpcomingTemp.setText(nextTaskText);
        });

        return root;
    }

/* This test code could be an example of adding new tasks
    private void test() {
        String startTime = Task.getInstance().formatTime(LocalDateTime.now());
        List<String> people = Arrays.asList("Alex", "Mike", "Tommy");
        LocationModel location = new LocationModel("Zoom", "https://123321.zoom.com");
        TaskModel task = new TaskModel("Test Task 2", "Social", "Another test task", startTime, people, location);
        Task.getInstance().addNewTask(task);
    }
*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
