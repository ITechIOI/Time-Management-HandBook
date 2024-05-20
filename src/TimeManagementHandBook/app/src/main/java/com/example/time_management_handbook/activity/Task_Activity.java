package com.example.time_management_handbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.example.time_management_handbook.adapter.TaskDAO;
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

public class Task_Activity extends AppCompatActivity {
    private int backStackEntryIndex;
    public static int selectedIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.taToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        //toolbar.setNavigationOnClickListener(v -> finish());


        TaskDTO task = null;
        Intent intent = getIntent();
        task = (TaskDTO) intent.getSerializableExtra("mytask");
        Log.d("TAG", "Received data: " + task);
        if (task == null){return;}
        TextView nameTextView = findViewById(R.id.taName_textInput);
        TextView deadlineTextView = findViewById(R.id.taDeadline_textInput);
        ImageView deadlineImageView = findViewById(R.id.taDeadline_dialog);
        TextView locationTextView = findViewById(R.id.taLocation_textInput);
        RadioGroup colorRadio = findViewById(R.id.taColor_radio);
        TextView notificationT = findViewById(R.id.taNotification_textInput);
        ImageView notificaltion = findViewById(R.id.taNotification_dialog);
        TextView descriptionTextView = findViewById(R.id.taDescription_textInput);
        Button saveButton = findViewById(R.id.taSave_button);

        LocalDateTime taskDeadline = task.getEndTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Context context = this;
        Log.d("Deadline for task today: ",taskDeadline.format(formatter));

        locationTextView.setText(task.getLocation());
        nameTextView.setText(task.getName());
        deadlineTextView.setText(taskDeadline.format(formatter));

        Duration duration = task.getNotification_period();
        String notification  = "";
        if (duration.toDays() == 0) {
            notification = String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        } else {
            notification = String.valueOf(duration.toDays()) + "d " +
                    String.valueOf(duration.toHours() % 24) + "h " + String.valueOf(duration.toMinutes() % 60) +
                    "m " + String.valueOf(duration.getSeconds() % 60) + "s";
        }
        notificationT.setText(notification);
        descriptionTextView.setText(task.getDescription());

        switch(task.getColor())
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

        deadlineImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateTimeDialog();
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

        RadioGroup customRadioGroup = findViewById(R.id.taColor_radio);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = nameTextView.getText().toString();
                String taskLocation = locationTextView.getText().toString();
                String taskDescription = descriptionTextView.getText().toString();
                String notification = notificationT.getText().toString();
                String timeEnd = deadlineTextView.getText().toString();

                String[] parts = notification.split(" ");
                int ngay = Integer.parseInt(parts[0].replace("d", ""));
                int gio = Integer.parseInt(parts[1].replace("h", ""));
                int phut = Integer.parseInt(parts[2].replace("m", ""));
                int giay = Integer.parseInt(parts[3].replace("s", ""));

                Duration duration;
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

                LocalDateTime timeEndL = LocalDateTime.parse(timeEnd, formatter);

                // Kiểm tra và cập nhật các thuộc tính của đối tượng sự kiện
                if (intent.getSerializableExtra("mytask") instanceof TaskDTO) {
                    TaskDTO task = (TaskDTO) intent.getSerializableExtra("mytask");
                    task.setName(taskName);
                    task.setLocation(taskLocation);
                    task.setEndTime(timeEndL);
                    task.setNotification_period(duration);
                    task.setDescription(taskDescription);
                    task.setColor(selectedIndex);

                    // Gọi hàm cập nhật sự kiện trong cơ sở dữ liệu
                    int rowsAffected = TaskDAO.getInstance().UpdateTask(task);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(Task_Activity.this, new TimePickerDialog.OnTimeSetListener() {
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