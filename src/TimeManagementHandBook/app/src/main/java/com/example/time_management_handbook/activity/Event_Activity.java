package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.Event_Of_The_Day_DAO;
import com.example.time_management_handbook.adapter.Prolonged_Event_DAO;
import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;
import com.example.time_management_handbook.model.Prolonged_Event_DTO;
import com.example.time_management_handbook.model.TaskDTO;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Event_Activity extends AppCompatActivity {

    public static int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Context context = this;

        Toolbar toolbar = findViewById(R.id.eaToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText tv_event_name = findViewById(R.id.eaName_textInput);
        TextInputEditText tv_event_datestart = findViewById(R.id.eaDateStart_textInput);
        TextInputEditText tv_event_dateend = findViewById(R.id.eaDateEnd_textInput);
        TextInputEditText tv_event_location = findViewById(R.id.eaLocation_textInput);
        TextInputEditText tv_event_description = findViewById(R.id.eaDescription_textInput);
        TextView notificationE = findViewById(R.id.eaNotification_textInput);
        ImageView notificaltion = findViewById(R.id.eaNotification_dialog);
        ImageView eStartTimeDialog = findViewById(R.id.eaDateStart_dialog);
        ImageView eEndTimeDialog = findViewById(R.id.eaDateEnd_dialog);

        eStartTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog(R.id.eaDateStart_textInput);
            }
        });
        eEndTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog(R.id.eaDateEnd_textInput);
            }
        });
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
                day.setMaxValue(365);
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
                        String text = day.getValue() +"d "+hour.getValue()+"h "+ minute.getValue()+"m "+sec.getValue()+"s";
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        //Nhận dữ liệu
        Intent intent = getIntent();
        if (intent.getSerializableExtra("myevent") instanceof Event_Of_The_Day_DTO){
            Event_Of_The_Day_DTO event = (Event_Of_The_Day_DTO) intent.getSerializableExtra("myevent");
            Log.d("TAG", "Received data: " + event);
            tv_event_name.setText(event.getSummary());
            tv_event_datestart.setText(event.getStartTime().format(formatter).toString());
            tv_event_dateend.setText(event.getEndTime().format(formatter).toString());
            tv_event_location.setText(event.getLocation());
            tv_event_description.setText(event.getDescription());
            Duration duration = event.getNotification_period();
            String notification  = "";
            /*if (duration.toDays() == 0) {
                notification = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            } else {
                notification = String.valueOf(duration.toDays()) + "d " +
                        String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                        "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            }*/
            notification = String.valueOf(duration.toDays()) + "d " +
                    String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
            notificationE.setText(notification);
            switch(event.getColor())
            {
                case 1:
                    RadioButton green = findViewById(R.id.green_radiobt);
                    green.setChecked(true);
                    break;
                case 2:
                    RadioButton red = findViewById(R.id.red_radiobt);
                    red.setChecked(true);
                    break;
                case 3:
                    RadioButton pink = findViewById(R.id.pink_radiobt);
                    pink.setChecked(true);
                    break;
                case 4:
                    RadioButton blue = findViewById(R.id.blue_radiobt);
                    blue.setChecked(true);
                    break;
                case 5:
                    RadioButton yellow = findViewById(R.id.yellow_radiobt);
                    yellow.setChecked(true);
                    break;
                case 6:
                    RadioButton orange = findViewById(R.id.orange_radiobt);
                    orange.setChecked(true);
                    break;
            }
        }
        else if (intent.getSerializableExtra("myevent") instanceof Prolonged_Event_DTO){
            Prolonged_Event_DTO event = (Prolonged_Event_DTO) intent.getSerializableExtra("myevent");
            Log.d("TAG", "Received data: " + event);
            tv_event_name.setText(event.getSummary());
            tv_event_datestart.setText(event.getStartDate().format(formatter).toString());
            tv_event_dateend.setText(event.getEndDate().format(formatter).toString());
            tv_event_location.setText(event.getLocation());
            tv_event_description.setText(event.getDescription());
            switch(event.getColor())
            {
                case 1:
                    RadioButton green = findViewById(R.id.green_radiobt);
                    green.setChecked(true);
                    break;
                case 2:
                    RadioButton red = findViewById(R.id.red_radiobt);
                    red.setChecked(true);
                    break;
                case 3:
                    RadioButton pink = findViewById(R.id.pink_radiobt);
                    pink.setChecked(true);
                    break;
                case 4:
                    RadioButton blue = findViewById(R.id.blue_radiobt);
                    blue.setChecked(true);
                    break;
                case 5:
                    RadioButton yellow = findViewById(R.id.yellow_radiobt);
                    yellow.setChecked(true);
                    break;
                case 6:
                    RadioButton orange = findViewById(R.id.orange_radiobt);
                    orange.setChecked(true);
                    break;
            }
        }

        RadioGroup customRadioGroup = findViewById(R.id.eaColor_radio);
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

        Button saveButton = findViewById(R.id.eaSave_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = tv_event_name.getText().toString();
                String eventLocation = tv_event_location.getText().toString();
                String eventDescription = tv_event_description.getText().toString();
                String timeStart = tv_event_datestart.getText().toString();
                String timeEnd = tv_event_dateend.getText().toString();
                String notification = notificationE.getText().toString();

                LocalDateTime timeStartParse = LocalDateTime.parse(timeStart, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                LocalDateTime timeEndParse = LocalDateTime.parse(timeEnd, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

                String timeStartLParse = timeStartParse.format(dateTimeFormatter);
                String timeEndLParse  = timeEndParse.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

                LocalDateTime timeStartL = LocalDateTime.parse(timeStartLParse, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
                LocalDateTime timeEndL = LocalDateTime.parse(timeEndLParse, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));


                LocalDate timeStartD = timeStartL.toLocalDate();
                LocalDate timeEndD = timeEndL.toLocalDate();

                String[] parts = notification.split(" ");
                Duration duration = null;
                try {
                    int ngay = Integer.parseInt(parts[0].replace("d", ""));
                    int gio = Integer.parseInt(parts[1].replace("h", ""));
                    int phut = Integer.parseInt(parts[2].replace("m", ""));
                    int giay = Integer.parseInt(parts[3].replace("s", ""));

                    if (ngay!= 0) {
                        duration = Duration.ofDays(ngay)
                                .plusHours(gio)
                                .plusMinutes(phut)
                                .plusSeconds(giay);
                    } else if ( gio != 0) {
                        duration = Duration.ofHours(gio)
                                .plusMinutes(phut)
                                .plusSeconds(giay);
                    } else if (phut != 0) {
                        duration = Duration.ofMinutes(phut)
                                .plusSeconds(giay);
                    }
                    else {
                        duration = Duration.ofSeconds(giay);
                    }

                } catch (NumberFormatException e) {
                    Log.d("Error cast to duration - NumberFormatException: ", e.getMessage());
                } catch (IllegalArgumentException e) {
                    Log.d("Error cast to duration - IllegalArgumentException: ", e.getMessage());
                }

                Log.d("Hello moi nguoi", duration.toString());

                // Kiểm tra và cập nhật các thuộc tính của đối tượng sự kiện
                if (intent.getSerializableExtra("myevent") instanceof Event_Of_The_Day_DTO) {
                    Event_Of_The_Day_DTO event = (Event_Of_The_Day_DTO) intent.getSerializableExtra("myevent");
                    event.setSummary(eventName);
                    event.setLocation(eventLocation);
                    event.setStartTime(timeStartL);
                    event.setEndTime(timeEndL);
                    event.setNotification_period(duration);
                    event.setDescription(eventDescription);
                    event.setColor(selectedIndex);

                    // Gọi hàm cập nhật sự kiện trong cơ sở dữ liệu
                    int rowsAffected = Event_Of_The_Day_DAO.getInstance().UpdateEventOfTheDay(event);
                    if (rowsAffected > 0) {
                        // Cập nhật thành công
                        Toast.makeText(getApplicationContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
                        // Kết thúc Activity hoặc thực hiện các hành động khác sau khi cập nhật thành công
                    } else {
                        // Cập nhật thất bại
                        Toast.makeText(getApplicationContext(), "Failed to update event", Toast.LENGTH_SHORT).show();
                    }
                } else if (intent.getSerializableExtra("myevent") instanceof Prolonged_Event_DTO) {
                    Prolonged_Event_DTO event = (Prolonged_Event_DTO) intent.getSerializableExtra("myevent");
                    event.setSummary(eventName);
                    event.setLocation(eventLocation);
                    event.setStartDate(timeStartD);
                    event.setEndDate(timeEndD);
                    event.setNotification_period(duration);
                    event.setDescription(eventDescription);
                    event.setColor(selectedIndex);

                    int rowsAffected = Prolonged_Event_DAO.getInstance().UpdateProlongedEvent(event);
                    if (rowsAffected > 0) {
                        // Cập nhật thành công
                        Toast.makeText(getApplicationContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
                        // Kết thúc Activity hoặc thực hiện các hành động khác sau khi cập nhật thành công
                    } else {
                        // Cập nhật thất bại
                        Toast.makeText(getApplicationContext(), "Failed to update event", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(Event_Activity.this, new TimePickerDialog.OnTimeSetListener() {
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