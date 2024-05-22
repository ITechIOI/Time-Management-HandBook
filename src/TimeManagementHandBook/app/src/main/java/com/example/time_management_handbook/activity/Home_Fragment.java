package com.example.time_management_handbook.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.HomeAdapter;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView hi_textview;
    TextView currentDate_textview;
    GridView eventView;
    DayOfWeek dayOfWeek;
    private String username;
    private List<Object> listAll = new ArrayList<>();
    private List<Event_Of_The_Day_DTO> listEventOfTheDay = new ArrayList<>();
    private List<Prolonged_Event_DTO> listProlongedEvent = new ArrayList<>();
    private List<TaskDTO> listTask = new ArrayList<>();

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        eventView = (GridView) view.findViewById(R.id.gridView_event);

        LocalDate today =  LocalDate.now();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        String formattedDate = reformatDate(today.toString(), "yyyy-MM-dd", "dd-MM-yyyy");

        username = Home_Activity.acc.getDisplayName();

        Log.d("Rounded day: ", roundedDateTime.toString());
        listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(Home_Activity.acc.getEmail().toString(), roundedDateTime);
        listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(Home_Activity.acc.getEmail().toString(), today);
        listTask = TaskDAO.getInstance().getListTask(Home_Activity.acc.getEmail().toString(), roundedDateTime);

        Log.d("List event of the day in homeFragment", listEventOfTheDay.toString());

        listAll = new ArrayList<>();
        listAll.addAll(listEventOfTheDay);
        listAll.addAll(listProlongedEvent);
        listAll.addAll(listTask);
        setEventandTaskView(listAll);

        dayOfWeek = today.getDayOfWeek();

        hi_textview=view.findViewById(R.id.textView_Hi);
        currentDate_textview = view.findViewById(R.id.textView_CurrentDate);
        try {
            currentDate_textview.setText(String.format("%s, %s", dayOfWeek.toString(), formattedDate));
        } catch (Exception e) {
            Log.d("Get today: ", Objects.requireNonNull(e.getMessage()));
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, this);
        transaction.addToBackStack(null);
        transaction.commit();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("I'm in start view in home fragment", "Yeah");

        LocalDate today =  LocalDate.now();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        String formattedDate = reformatDate(today.toString(), "yyyy-MM-dd", "dd-MM-yyyy");

        username = Home_Activity.acc.getDisplayName();
        listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(Home_Activity.acc.getEmail().toString(), roundedDateTime);
        listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(Home_Activity.acc.getEmail().toString(), today);
        listTask = TaskDAO.getInstance().getListTask(Home_Activity.acc.getEmail().toString(), roundedDateTime);

        setHiTextView(username);
        try {
            currentDate_textview.setText(String.format("%s, %s", dayOfWeek.toString(), formattedDate.toString()));
        } catch(Exception e) {
            Log.d("Get today: ", e.getMessage());
        }

        listAll = new ArrayList<>();
        listAll.addAll(listEventOfTheDay);
        listAll.addAll(listProlongedEvent);
        listAll.addAll(listTask);
        setEventandTaskView(listAll);
    }

    public void setHiTextView(String username)
    {
        hi_textview.setText("Hi, " + username + "!");
    }

    public void setCurrentDateTextView(String currentDate)
    {
        currentDate_textview.setText(currentDate);
    }

    public void setEventandTaskView(List<Object> lData){
        eventView.setAdapter(new HomeAdapter(lData, getActivity()));
    }

    public static String reformatDate(String date, String originalFormat, String targetFormat) {
        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern(originalFormat);
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern(targetFormat);
        LocalDate dateObj = LocalDate.parse(date, originalFormatter);
        return dateObj.format(targetFormatter);
    }
}