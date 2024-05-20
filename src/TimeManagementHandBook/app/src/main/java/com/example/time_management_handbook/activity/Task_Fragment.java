package com.example.time_management_handbook.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.TaskDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Task_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Task_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TaskDAO taskAdapter;
    RecyclerView rcv;
    SearchView searchView;
    private List<TaskDTO> listTaskByCurrentDate;
    Dialog delete_dialog;
    Button cancelButton, deleteButton;



    ExecutorService executorServiceHandle = Executors.newSingleThreadExecutor();
    ExecutorService executorServiceGetTask = Executors.newSingleThreadExecutor();

    public Task_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Task_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Task_Fragment newInstance(String param1, String param2) {
        Task_Fragment fragment = new Task_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_task_, container, false);
        rcv = view.findViewById(R.id.task_rcv);
        searchView = view.findViewById(R.id.task_searchview);
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        listTaskByCurrentDate = TaskDAO.getInstance().getListTask(Home_Activity.acc.getEmail(), roundedDateTime);
        taskAdapter = new TaskDAO(listTaskByCurrentDate,getActivity());
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(taskAdapter);
        searchView.setQueryHint("Enter name of task...");
      searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterTask(newText);
                return true;
            }
        });


        return view;
    }
    private void filterTask(String newText) {
        List<TaskDTO> filterTask = new ArrayList<>();
        for (TaskDTO item : listTaskByCurrentDate){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filterTask.add(item);
            }
        }
        if (filterTask.isEmpty()){
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }
        else {
            taskAdapter.setFilterList(filterTask);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));

        // Get list task by name of task and current date
        executorServiceHandle.execute(new Runnable() {
            @Override
            public void run() {


                List<TaskDTO> listTaskByName = TaskDAO.getInstance().getListTaskByName(Home_Activity.acc.getEmail().toString(), "tESt Dữ", roundedDateTime);
                Log.d("List task by name: ", listTaskByName.toString());

            }
        });

        // Get list task by current date

        executorServiceGetTask.execute(new Runnable() {
            @Override
            public void run() {
                listTaskByCurrentDate = TaskDAO.getInstance().getListTask(Home_Activity.acc.getEmail(), roundedDateTime);
                Log.d("List task: ", listTaskByCurrentDate.toString());
            }
        });

    }
}