package com.example.time_management_handbook.activity;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
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
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = findViewById(R.id.eToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //Cac thanh phan cua layout
        TextInputEditText eName = findViewById(R.id.eName_textInput);
        TextInputEditText eDate = findViewById(R.id.eDate_textInput);
        TextInputEditText eStartTime = findViewById(R.id.eStartTime_textInput);
        TextInputEditText eEndTime = findViewById(R.id.eEndTime_textInput);
        TextInputEditText eLocation = findViewById(R.id.eLocation_textInput);
        LinearLayout linearLayout = findViewById(R.id.eColor_radio);
        RadioGroup customRadioGroup = findViewById(R.id.color_radio);
        TextInputEditText eDescription = findViewById(R.id.eDescription_textInput);


        ImageView eDateDialog = findViewById(R.id.eDate_dialog);
        ImageView eStartTimeDialog = findViewById(R.id.eStartTime_Dialog);
        ImageView eEndTimeDialog = findViewById(R.id.eEndTime_dialog);
        Button btnSave = findViewById(R.id.eCreate_button);

        eDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });
        eStartTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(R.id.eStartTime_textInput);
            }
        });
        eEndTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(R.id.eEndTime_textInput);
            }
        });

        customRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Tìm RadioButton được chọn trong RadioGroup
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    // Lấy chỉ mục của RadioButton được chọn trong RadioGroup
                    selectedIndex = customRadioGroup.indexOfChild(selectedRadioButton) + 1;

                    // Log chỉ mục của RadioButton được chọn
                    Log.d("Selected Index", String.valueOf(selectedIndex));
                }
            }
        });

        acc = getLastSignedInAccount(this);
        final String email = acc.getEmail();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateString = eDate.getText().toString();
                String timeStart = eStartTime.getText().toString();
                String timeEnd = eEndTime.getText().toString();

                // DateTimeFormatter for parsing date in format "dd/MM/yyyy"
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // DateTimeFormatter for parsing time in format "HH:mm"
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:m");

                LocalDate date = LocalDate.parse(dateString, dateFormatter);
                LocalTime timeStartLT = LocalTime.parse(timeStart, timeFormatter);
                LocalTime timeEndLT = LocalTime.parse(timeEnd, timeFormatter);

                LocalDateTime eTimeStartL = LocalDateTime.of(date, timeStartLT);
                LocalDateTime eTimeEndL = LocalDateTime.of(date, timeEndLT);


                Event_Of_The_Day_DTO newEvent = new Event_Of_The_Day_DTO(
                        null, // EventId sẽ tự động được tạo khi thêm vào cơ sở dữ liệu
                        null, // UserId được truyền vào khi thực hiện lưu sự kiện (không cần trong constructor)
                        eName.getText().toString(),
                        eLocation.getText().toString(),
                        eTimeStartL,
                        eTimeEndL,
                        Duration.ofMinutes(15), // Chu kỳ thông báo
                        eDescription.getText().toString(), // Mô tả
                        selectedIndex // Màu sắc (vd: màu mặc định)
                );

                int result = Event_Of_The_Day_DAO.getInstance().InsertNewEvent(email, newEvent);
                if (result != -1)
                {
                    Toast.makeText(getApplicationContext(), "Submit thành công", Toast.LENGTH_SHORT).show();
                    Log.d("Insert event", eName.getText().toString());
                    eName.setText("");
                    eDate.setText("");
                    eStartTime.setText("");
                    eEndTime.setText("");
                    eLocation.setText("");
                    eDescription.setText("");
                    linearLayout.clearFocus();
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
                TextInputEditText editText = findViewById(R.id.eDate_textInput);
                editText.setText(formattedDateTime);
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

}