package com.josevi.gastos.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.NotificationInfoListAdapter;
import com.josevi.gastos.dialogs.OneButtonDialog;
import com.josevi.gastos.dialogs.TwoButtonsDialog;
import com.josevi.gastos.models.Notification;
import com.josevi.gastos.models.enums.NotificationTag;
import com.josevi.gastos.repositories.NotificationRepository;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.josevi.gastos.utils.Constantes.NOTIFICATION_EDIT;
import static com.josevi.gastos.utils.Constantes.timeDateFormat;
import static com.josevi.gastos.utils.Constantes.prettyDayDateFormat;

public class NewNotificationActivity extends AppCompatActivity {

    private ImageView calendarTabSelector;
    private ImageView daySelector, timeSelector;
    private TextView dayLabel, timeLabel;
    private EditText title;
    private ImageView addNewInfoButton;
    private EditText newInfoKey, newInfoInfo;
    private RecyclerView infoView;
    private NotificationInfoListAdapter notificationInfoListAdatper;
    private Button saveBtn;

    private NotificationRepository notificationRepository;
    private Calendar dateSelected;
    private Notification notificationToUpdate;
    private Map<String, String> infoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationToUpdate = null;
        try {
            notificationToUpdate = (Notification) getIntent().getParcelableExtra(NOTIFICATION_EDIT);
        }
        catch (Exception e) {}

        initializeLayout();
        initializeParameters();

        configureDateSelector();
        configureTitle();
        configureAddNewInfo();
        configureSaveButton();
    }

    private void initializeLayout() {
        setContentView(R.layout.activity_new_notification);

        calendarTabSelector = findViewById(R.id.new_notification_event_selector);

        daySelector = findViewById(R.id.new_notification_day_btn);
        timeSelector = findViewById(R.id.new_notification_time_btn);
        dayLabel = findViewById(R.id.new_notification_day_text);
        timeLabel = findViewById(R.id.new_notification_time_text);

        title = findViewById(R.id.new_notification_title);

        addNewInfoButton = findViewById(R.id.new_notification_add_new_info_btn);
        newInfoKey = findViewById(R.id.new_notification_new_info_key);
        newInfoInfo = findViewById(R.id.new_notification_new_info_info);
        infoView = findViewById(R.id.new_notification_info_view);

        saveBtn = findViewById(R.id.new_notification_save_btn);
    }

    private void initializeParameters() {
        notificationRepository = new NotificationRepository();
        dateSelected = Calendar.getInstance();
        infoMap = new HashMap<String, String>();
        if (notificationToUpdate != null) {
            dateSelected.setTime(notificationToUpdate.getDate());
            infoMap = notificationToUpdate.getInfoMap();
        }
    }

    public void configureHeader() {}

    public void configureDateSelector() {
        dayLabel.setText(prettyDayDateFormat.format(dateSelected.getTime()));
        timeLabel.setText(timeDateFormat.format(dateSelected.getTime()));

        daySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                mYear = dateSelected.get(Calendar.YEAR);
                mMonth = dateSelected.get(Calendar.MONTH);
                mDay = dateSelected.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(NewNotificationActivity.this, R.style.AppTheme , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateSelected.set(year,monthOfYear,dayOfMonth);
                        dayLabel.setText(prettyDayDateFormat.format(dateSelected.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mHour, mMinute;
                mHour = dateSelected.get(Calendar.HOUR_OF_DAY);
                mMinute = dateSelected.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(NewNotificationActivity.this, R.style.AppTheme , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateSelected.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        dateSelected.set(Calendar.MINUTE,minute);
                        timeLabel.setText(timeDateFormat.format(dateSelected.getTime()));
                    }
                }, mHour, mMinute, true);
                mTimePicker.show();
            }
        });
    }

    private void configureTitle() {
        if (notificationToUpdate != null)
            title.setText(notificationToUpdate.getTitle());
    }

    public void configureAddNewInfo() {
        notificationInfoListAdatper = new NotificationInfoListAdapter(infoMap, this);
        notificationInfoListAdatper.notifyDataSetChanged();
        infoView.setLayoutManager(new LinearLayoutManager(this));
        infoView.setAdapter(notificationInfoListAdatper);

        addNewInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newInfoKey.getText() != null && !newInfoKey.getText().toString().isEmpty()
                        && newInfoInfo.getText() != null && !newInfoInfo.getText().toString().isEmpty()) {
                    infoMap.put(newInfoKey.getText().toString(), newInfoInfo.getText().toString());
                    notificationInfoListAdatper.setNotificationInfos(infoMap);
                    newInfoKey.setText("");
                    newInfoInfo.setText("");
                }
            }
        });
    }

    private void configureSaveButton() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().isEmpty()) {
                    if (notificationToUpdate != null)
                        notificationRepository.update(notificationToUpdate);
                    else {
                        Notification newNotification = new Notification(dateSelected.getTime(), title.getText().toString(), NotificationTag.EVENT);
                        newNotification.setInfoMap(infoMap);
                        notificationRepository.save(newNotification);
                    }
                }
                else {
                    OneButtonDialog noTitleDialog = new OneButtonDialog(NewNotificationActivity.this);
                    noTitleDialog.setMessage("Debes escribir un título.");
                    noTitleDialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!title.getText().toString().isEmpty() || !infoMap.isEmpty()) {
            TwoButtonsDialog exitDialog = new TwoButtonsDialog(this, R.color.red_app);
            exitDialog.setMessage("Si sales, se perderán los cambios.");
            exitDialog.setLeftButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewNotificationActivity.super.onBackPressed();
                }
            });
            exitDialog.show();
        }
        else
            NewNotificationActivity.super.onBackPressed();
    }
}
