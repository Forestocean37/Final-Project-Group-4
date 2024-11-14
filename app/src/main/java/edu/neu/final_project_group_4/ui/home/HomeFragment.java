package edu.neu.final_project_group_4.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.databinding.FragmentHomeBinding;
import edu.neu.final_project_group_4.utils.Auth;

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
            Auth.signOut(getActivity());
        });

        binding.buttonSocial.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task_type", "Social");
            Navigation.findNavController(v).navigate(R.id.navigation_tasks, bundle);
        });

        return root;
    }

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
