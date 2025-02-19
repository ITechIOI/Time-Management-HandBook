package com.example.time_management_handbook.activity;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.TaskDAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.TaskDTO;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddTask_Activity extends AppCompatActivity {

    public static GoogleSignInAccount acc;
    public static int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Context context = this;
        Toolbar toolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView imageView = findViewById(R.id.tDeadline_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog();
            }
        });

        TextInputEditText tName = findViewById(R.id.tName_textInput);
        TextInputEditText tDeadline = findViewById(R.id.tDeadline_textInput);
        TextInputEditText tLocation = findViewById(R.id.tLocation_textInput);
        RadioGroup customRadioGroup = findViewById(R.id.tColor_radio);
        TextInputEditText tDescription = findViewById(R.id.tDescription_textInput);
        TextView notificationT = findViewById(R.id.tNotification_textInput);
        ImageView notificaltion = findViewById(R.id.tNotification_dialog);
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
                        notificationT.setText(text);
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
        customRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    selectedIndex = customRadioGroup.indexOfChild(selectedRadioButton) + 1;
                    Log.d("Selected Index", String.valueOf(selectedIndex));
                }
            }
        });

        acc = Home_Activity.acc;
        final String email = acc.getEmail();

        Button btnSave = findViewById(R.id.tCreate_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deadline = tDeadline.getText().toString();
                String name = tName.getText().toString();
                String location = tLocation.getText().toString();
                String durationString = notificationT.getText().toString();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime lDeadline = deadline.isEmpty() ? null : LocalDateTime.parse(deadline, formatter);

                Duration duration;

                if (durationString.isEmpty())
                {
                    duration= null;
                }
                else {
                    String[] parts = notificationT.getText().toString().split(" ");
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
                    else {
                        duration = Duration.ofHours(gio)
                                .plusMinutes(phut)
                                .plusSeconds(giay);
                    }
                }

                int result;
                boolean flag1 = true, flag2 = true, flag3 = true;

                if (name.isEmpty()) {
                    tName.setBackgroundResource(R.drawable.custom_textinput_error);
                    flag1 = false;
                }
                if (deadline.isEmpty()) {
                    tDeadline.setBackgroundResource(R.drawable.custom_textinput_error);
                    flag2 = false;
                }
                if (durationString.isEmpty()) {
                    notificationT.setBackgroundResource(R.drawable.custom_textinput_error);
                    flag3 = false;
                }
                if (flag1) {
                    tName.setBackgroundResource(R.drawable.custom_textinputlayout);
                }
                if (flag2) {
                    tDeadline.setBackgroundResource(R.drawable.custom_textinputlayout);
                }
                if (flag3) {
                    tName.setBackgroundResource(R.drawable.custom_textinputlayout);
                }

                if (flag3 && flag1 && flag2){
                    if (lDeadline.isAfter(LocalDateTime.now())) {
                        TaskDTO newTask = new TaskDTO(
                                null,
                                null,
                                tName.getText().toString(),
                                tLocation.getText().toString(),
                                LocalDateTime.now(),
                                lDeadline,
                                duration,
                                tDescription.getText().toString(),
                                LocalDateTime.MAX,
                                selectedIndex
                        );
                        tName.setBackgroundResource(R.drawable.custom_textinputlayout);
                        tDeadline.setBackgroundResource(R.drawable.custom_textinputlayout);
                        notificationT.setBackgroundResource(R.drawable.custom_textinputlayout);
                        result = TaskDAO.getInstance().InsertNewTask(email, newTask);
                        if (result != -1)
                        {
                            Toast.makeText(getApplicationContext(), "Add new event successfully", Toast.LENGTH_SHORT).show();
                            Log.d("Insert task", tName.getText().toString());
                            tName.setText("");
                            tDeadline.setText("");
                            tLocation.setText("");
                            tDescription.setText("");
                            notificationT.setText("");
                            customRadioGroup.clearFocus();
                        }
                        else {
                            Log.d("Insert task","error");
                        }
                    } else {
                        Toast.makeText(AddTask_Activity.this, "Deadline must be after now", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    tDeadline.setBackgroundResource(R.drawable.custom_textinput_error);
                    Toast.makeText(AddTask_Activity.this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
                }



            }
        });



    }
    private void openDateTimeDialog()
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTask_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute);
                        String formattedDateTime = sdf.format(selectedDateTime.getTime());
                        TextInputEditText editText = findViewById(R.id.tDeadline_textInput);
                        editText.setText(formattedDateTime);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}