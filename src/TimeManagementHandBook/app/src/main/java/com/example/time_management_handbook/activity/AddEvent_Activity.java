package com.example.time_management_handbook.activity;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddEvent_Activity extends AppCompatActivity {

    public static GoogleSignInAccount acc;
    public static int selectedIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = findViewById(R.id.eToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText eName = findViewById(R.id.eName_textInput);
        TextInputEditText eStartTime = findViewById(R.id.eDateStart_textInput);
        TextInputEditText eEndTime = findViewById(R.id.eDateEnd_textInput);
        TextInputEditText eLocation = findViewById(R.id.eLocation_textInput);
        RadioGroup customRadioGroup = findViewById(R.id.eColor_radio);
        TextInputEditText eDescription = findViewById(R.id.eDescription_textInput);

        ImageView eStartTimeDialog = findViewById(R.id.eDateStart_dialog);
        ImageView eEndTimeDialog = findViewById(R.id.eDateEnd_dialog);
        Button btnSave = findViewById(R.id.eCreate_button);
        TextView notificationE = findViewById(R.id.eNotification_textInput);
        ImageView notificaltion = findViewById(R.id.eNotification_dialog);
        notificaltion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                NumberPicker day, hour, minute, sec;
                Button buttonOk, buttonCancel;
                dialog.setContentView(R.layout.notification_picker);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.custom_itemdialog));
                day = dialog.findViewById(R.id.day_picker);
                day.setMinValue(0);
                day.setMaxValue(50);
                hour = dialog.findViewById(R.id.hour_picker);
                hour.setMinValue(0);
                hour.setMaxValue(23);
                minute = dialog.findViewById(R.id.minute_picker);
                minute.setMinValue(0);
                minute.setMaxValue(59);
                sec = dialog.findViewById(R.id.sec_picker);
                sec.setMinValue(0);
                sec.setMaxValue(59);
                buttonOk = dialog.findViewById(R.id.buttonOk);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = day.getValue() +"d "+hour.getValue()+"h "+minute.getValue()+"m "+sec.getValue()+"s";
                        notificationE.setText(text);
                        dialog.dismiss();
                    }
                });
                buttonCancel = dialog.findViewById(R.id.buttonCancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        eStartTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog(R.id.eDateStart_textInput);
            }
        });
        eEndTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog(R.id.eDateEnd_textInput);
            }
        });

        customRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Tìm RadioButton được chọn trong RadioGroup
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    selectedIndex = customRadioGroup.indexOfChild(selectedRadioButton) + 1;
                    Log.d("Selected Index", String.valueOf(selectedIndex));
                }
            }
        });

        acc = getLastSignedInAccount(this);
        final String email = acc.getEmail();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eName.getText().toString();
                String location = eLocation.getText().toString();
                String timeStart = eStartTime.getText().toString();
                String timeEnd = eEndTime.getText().toString();

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                LocalDateTime eTimeStartL,eTimeEndL;
                LocalDate dateStartLD, dateEndLD;

                try {
                    eTimeStartL = LocalDateTime.parse(timeStart, dateTimeFormatter);
                    eTimeEndL  = LocalDateTime.parse(timeEnd, dateTimeFormatter);
                    dateStartLD = eTimeStartL.toLocalDate();
                    dateEndLD = eTimeEndL.toLocalDate();
                } catch (Exception e){
                    eTimeStartL = null;
                    eTimeEndL = null;
                    dateStartLD = null;
                    dateEndLD = null;
                }

                Duration duration;
                String durationString = notificationE.getText().toString();
                if (durationString.isEmpty())
                {
                    duration = null;
                }
                else {
                    String[] parts = durationString.split(" ");
                    int ngay = Integer.parseInt(parts[0].replace("d", ""));
                    int gio = Integer.parseInt(parts[1].replace("h", ""));
                    int phut = Integer.parseInt(parts[2].replace("m", ""));
                    int giay = Integer.parseInt(parts[3].replace("s", ""));

                    if (ngay != 0){
                        duration = Duration.ofDays(ngay)
                                .plusHours(gio)
                                .plusMinutes(phut)
                                .plusSeconds(giay);
                    }
                    // Tạo đối tượng Duration
                    else {
                        duration = Duration.ofHours(gio)
                                .plusMinutes(phut)
                                .plusSeconds(giay);
                    }
                }

                int result = -1;
                if (name.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty() || durationString.isEmpty()){
                    eName.setBackgroundResource(R.drawable.custom_textinput_error);
                    eStartTime.setBackgroundResource(R.drawable.custom_textinput_error);
                    eEndTime.setBackgroundResource(R.drawable.custom_textinput_error);
                    notificationE.setBackgroundResource(R.drawable.custom_textinput_error);
                    Toast.makeText(AddEvent_Activity.this, "Please complete all required fields.", Toast.LENGTH_SHORT).show();
                } else if (dateStartLD.isEqual(dateEndLD)){
                    Event_Of_The_Day_DTO newEvent = new Event_Of_The_Day_DTO(
                            null, // EventId sẽ tự động được tạo khi thêm vào cơ sở dữ liệu
                            null, // UserId được truyền vào khi thực hiện lưu sự kiện (không cần trong constructor)
                            name,
                            location,
                            eTimeStartL,
                            eTimeEndL,
                            duration, // Chu kỳ thông báo
                            eDescription.getText().toString(), // Mô tả
                            selectedIndex // Màu sắc (vd: màu mặc định)
                    );
                    eName.setBackgroundResource(R.drawable.custom_textinputlayout);
                    eStartTime.setBackgroundResource(R.drawable.custom_textinputlayout);
                    eEndTime.setBackgroundResource(R.drawable.custom_textinputlayout);
                    notificationE.setBackgroundResource(R.drawable.custom_textinputlayout);
                    result = Event_Of_The_Day_DAO.getInstance().InsertNewEvent(email, newEvent);
                }
                else if(dateStartLD.isBefore(dateEndLD)) {
                    Prolonged_Event_DTO newEvent = new Prolonged_Event_DTO(
                            null,
                            null,
                            eName.getText().toString(),
                            eLocation.getText().toString(),
                            dateStartLD,
                            dateEndLD,
                            duration, // Chu kỳ thông báo
                            eDescription.getText().toString(), // Mô tả
                            selectedIndex // Màu sắc (vd: màu mặc định)
                    );
                    eName.setBackgroundResource(R.drawable.custom_textinputlayout);
                    eStartTime.setBackgroundResource(R.drawable.custom_textinputlayout);
                    eEndTime.setBackgroundResource(R.drawable.custom_textinputlayout);
                    notificationE.setBackgroundResource(R.drawable.custom_textinputlayout);
                    result = Prolonged_Event_DAO.getInstance().InsertNewProlongedEvent(email, newEvent);
                }
                else {
                    eStartTime.setBackgroundResource(R.drawable.custom_textinput_error);
                    eEndTime.setBackgroundResource(R.drawable.custom_textinput_error);
                    Toast.makeText(AddEvent_Activity.this, "Start date is after end date", Toast.LENGTH_SHORT).show();
                }
                if (result != -1)
                {
                    Toast.makeText(getApplicationContext(), "Submit thành công", Toast.LENGTH_SHORT).show();
                    Log.d("Insert event", eName.getText().toString());
                    eName.setText("");
                    eStartTime.setText("");
                    eEndTime.setText("");
                    eLocation.setText("");
                    eDescription.setText("");
                    notificationE.setText("");
                    customRadioGroup.clearFocus();
                    eName.setHint("");
                    eStartTime.setHint("");
                    eEndTime.setHint("");
                    notificationE.setHint("");
                }
                else {
                    Log.d("Insert event","error");
                }
            }
        });
    }
    private void openDateDialog()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, dayOfMonth);
                String formattedDateTime = sdf.format(selectedDateTime.getTime());
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void openTimeDialog(int id){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedDateTime = hourOfDay+":"+minute;
                TextInputEditText editText = findViewById(id);
                editText.setText(formattedDateTime);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void openDateTimeDialog(int id)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute);
                        String formattedDateTime = sdf.format(selectedDateTime.getTime());
                        TextInputEditText editText = findViewById(id);
                        editText.setText(formattedDateTime);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}