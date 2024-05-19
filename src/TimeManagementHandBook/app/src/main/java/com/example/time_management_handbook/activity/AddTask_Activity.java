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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.TaskDAO;
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
        Toolbar toolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
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

        Button btnSave = findViewById(R.id.tCreate_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deadline = tDeadline.getText().toString();
                // Định dạng chuỗi ngày giờ
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                // Chuyển đổi chuỗi thành LocalDateTime
                LocalDateTime lDeadline = LocalDateTime.parse(deadline, formatter);


                TaskDTO newTask = new TaskDTO(
                        null, // EventId sẽ tự động được tạo khi thêm vào cơ sở dữ liệu
                        null, // UserId được truyền vào khi thực hiện lưu sự kiện (không cần trong constructor)
                        tName.getText().toString(),
                        tLocation.getText().toString(),
                        LocalDateTime.now(),
                        lDeadline,
                        Duration.ofMinutes(15), // Chu kỳ thông báo
                        tDescription.getText().toString(), // Mô tả
                        null,
                        selectedIndex // Màu sắc (vd: màu mặc định)
                );

                int result = TaskDAO.getInstance().InsertNewTask(email, newTask);
                if (result != -1)
                {
                    Toast.makeText(getApplicationContext(), "Submit thành công", Toast.LENGTH_SHORT).show();
                    Log.d("Insert task", tName.getText().toString());
                    tName.setText("");
                    tDeadline.setText("");
                    tLocation.setText("");
                    tDescription.setText("");
                    customRadioGroup.clearFocus();
                }
                else {
                    Log.d("Insert task","error");
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