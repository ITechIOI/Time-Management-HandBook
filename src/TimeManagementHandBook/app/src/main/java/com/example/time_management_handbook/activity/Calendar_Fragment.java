package com.example.time_management_handbook.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.CalendarAdapter;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calendar_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calendar_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView dateView;
    private List<Object> lObject = new ArrayList<>();
    private List<Event_Of_The_Day_DTO> listEventOfTheDay = new ArrayList<>();
    private List<Prolonged_Event_DTO> listProlongedEvent = new ArrayList<>();

    private List<LocalDate> listLocalDateByMonth = new ArrayList<>();
    public Calendar_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Calendar_Fragment newInstance(String param1, String param2) {
        Calendar_Fragment fragment = new Calendar_Fragment();
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
        return inflater.inflate(R.layout.fragment_calendar_month_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalDate today = LocalDate.now();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));

        listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(Home_Activity.acc.getEmail().toString(), roundedDateTime);
        listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(Home_Activity.acc.getEmail().toString(), today);

        dateView = view.findViewById(R.id.textView_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        dateView.setText(today.format(formatter));

        RecyclerView notes = view.findViewById(R.id.recyclerView_notes);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                LocalDate todayDate = LocalDate.of(year, month + 1, dayOfMonth);
                LocalDateTime localTime = todayDate.atStartOfDay();
                dateView.setText(todayDate.format(formatter));

                listEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDay(Home_Activity.acc.getEmail().toString(), localTime);
                listProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEvent(Home_Activity.acc.getEmail().toString(), todayDate);
                lObject = new ArrayList<>();

                lObject.addAll(listEventOfTheDay);
                lObject.addAll(listProlongedEvent);
                notes.setAdapter(new CalendarAdapter(getActivity(), lObject));
                notes.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        lObject = new ArrayList<>();

        lObject.addAll(listEventOfTheDay);
        lObject.addAll(listProlongedEvent);
        notes.setAdapter(new CalendarAdapter(getActivity(), lObject));
        notes.setLayoutManager(new LinearLayoutManager(getContext()));

        List<LocalDate> listLocalDateContainEvent = new ArrayList<>();

        try {
            List<LocalDate> listLocalDateContainingEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDayByDayOfMonth(
                    Home_Activity.acc.getEmail(), roundedDateTime);
            listLocalDateContainEvent.addAll(listLocalDateContainingEventOfTheDay);
            Log.d("Get list local date containing event of the day: ", listLocalDateContainingEventOfTheDay.toString());
        } catch(Exception e) {
            Log.d("Get list local date containing event of the day: ", "fail");
        }

        try {
            List<LocalDate> listLocalDateContainingProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEventByDayOfMonth(
                    Home_Activity.acc.getEmail(), roundedDateTime.toLocalDate());
            listLocalDateContainEvent.addAll(listLocalDateContainingProlongedEvent);
            Log.d("Get list local date contain prolonged event X: ", listLocalDateContainingProlongedEvent.toString());
        } catch(Exception e) {
            Log.d("Get list local date contain prolonged event X: ", "fail");
        }

        // listLocalDate:

        Set<LocalDate> listLocalDateTemp = new HashSet<>();
        for (int i = 0; i < listLocalDateContainEvent.size(); i++) {
            listLocalDateTemp.add(listLocalDateContainEvent.get(i));
        }

        List<LocalDate> listLocalDateByMonth = listLocalDateTemp.stream().collect(Collectors.toList());
        Log.d("List unduplicated local date contains event: ", listLocalDateByMonth.toString());

    }

    @Override
    public void onStart() {
        super.onStart();

        LocalDate today = LocalDate.now();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime roundedDateTime = timeNow.with(LocalTime.from(timeNow.toLocalTime().withSecond(timeNow.getSecond()).withNano(0)));
        List<LocalDate> listLocalDateContainEvent = new ArrayList<>();

        try {
            List<LocalDate> listLocalDateContainingEventOfTheDay = Event_Of_The_Day_DAO.getInstance().getListEventOfTheDayByDayOfMonth(
                    Home_Activity.acc.getEmail(), roundedDateTime);
            listLocalDateContainEvent.addAll(listLocalDateContainingEventOfTheDay);
            Log.d("Get list local date containing event of the day: ", listLocalDateContainingEventOfTheDay.toString());
        } catch(Exception e) {
            Log.d("Get list local date containing event of the day: ", "fail");
        }

        try {
            List<LocalDate> listLocalDateContainingProlongedEvent = Prolonged_Event_DAO.getInstance().getListProlongedEventByDayOfMonth(
                    Home_Activity.acc.getEmail(), roundedDateTime.toLocalDate());
            listLocalDateContainEvent.addAll(listLocalDateContainingProlongedEvent);
            Log.d("Get list local date contain prolonged event X: ", listLocalDateContainingProlongedEvent.toString());
        } catch(Exception e) {
            Log.d("Get list local date contain prolonged event X: ", "fail");
        }

        // listLocalDate:

        Set<LocalDate> listLocalDateTemp = new HashSet<>();
        for (int i = 0; i < listLocalDateContainEvent.size(); i++) {
            listLocalDateTemp.add(listLocalDateContainEvent.get(i));
        }

        List<LocalDate> listLocalDateByMonth = listLocalDateTemp.stream().collect(Collectors.toList());
        Log.d("List unduplicated local date contains event: ", listLocalDateByMonth.toString());

    }

}