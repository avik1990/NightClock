package com.app.nightclock.alarm;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.nightclock.R;
import com.app.nightclock.TimerActivity;
import com.app.nightclock.alarmutils.AlarmReceiver;
import com.app.nightclock.alarmutils.LocalData;
import com.app.nightclock.alarmutils.NotificationScheduler;
import com.app.nightclock.util.Utils;
import com.google.android.flexbox.FlexboxLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    String TAG = "SettingsActivity";
    Context context;
    int hour;
    LinearLayout ll_set_time;
    LocalData localData;
    int min;
    ClipboardManager myClipboard;
    RadioButton rb_bigfont;
    RadioButton rb_default;
    RadioButton rb_largefont;
    RadioButton rb_smallfont;
    SwitchCompat reminderSwitch;
    RadioGroup rg_fonts;
    TextView tvTime;
    CheckBox checkbox;
    List<String> listColor = new ArrayList<>();
    Button choosecolor;
    int clicked = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.localData = new LocalData(getApplicationContext());
        this.context = this;
        //this.myClipboard = (ClipboardManager) getSystemService("clipboard");


        initViews();
        insertColor();

    }


    private void insertColor() {
        listColor.clear();
        listColor.add("#a4c639"); //0
        listColor.add("#fa744f"); //1
        listColor.add("#f79071"); //2
        listColor.add("#f40552"); //3
        listColor.add("#d7385e"); //3
        listColor.add("#00909e"); //3

        listColor.add("#ffffff"); //3
        listColor.add("#ffff00"); //3
        listColor.add("#0000ff"); //3
        listColor.add("#bf00ff"); //3
        listColor.add("#b3cccc"); //3
        listColor.add("#ff00ff"); //3
        listColor.add("#999999"); //3
        listColor.add("#ff3300"); //3

        listColor.add("#4000ff"); //3
        listColor.add("#ff6600"); //3
        listColor.add("#9900cc"); //3
        listColor.add("#3366cc"); //3
        listColor.add("#00ccff"); //3
        listColor.add("#339966"); //3
        listColor.add("#339933"); //3
        listColor.add("#996600"); //3

    }

    private void initViews() {
        choosecolor = findViewById(R.id.choosecolor);
        this.ll_set_time = (LinearLayout) findViewById(R.id.ll_set_time);
        this.tvTime = (TextView) findViewById(R.id.tv_reminder_time_desc);
        this.reminderSwitch = (SwitchCompat) findViewById(R.id.timerSwitch);
        this.hour = this.localData.get_hour();
        this.min = this.localData.get_min();
        this.tvTime.setText(getFormatedTime(this.hour, this.min));
        this.reminderSwitch.setChecked(this.localData.getReminderStatus());
        if (!this.localData.getReminderStatus()) {
            this.ll_set_time.setAlpha(0.4f);
        }
        this.reminderSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsActivity.this.localData.setReminderStatus(isChecked);
                if (isChecked) {
                    Context context = SettingsActivity.this;
                    // NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), SettingsActivity.this.localData.get_min());
                    SettingsActivity.this.ll_set_time.setAlpha(1.0f);
                    return;
                }
                // NotificationScheduler.cancelReminder(SettingsActivity.this, AlarmReceiver.class);
                SettingsActivity.this.ll_set_time.setAlpha(0.4f);
            }
        });

        this.ll_set_time.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SettingsActivity.this.localData.getReminderStatus()) {
                    SettingsActivity settingsActivity = SettingsActivity.this;
                    settingsActivity.showTimePickerDialog(settingsActivity.localData.get_hour(), SettingsActivity.this.localData.get_min());
                }
            }
        });
        checkbox = findViewById(R.id.checkbox);
        this.rb_default = (RadioButton) findViewById(R.id.rb_default);
        this.rg_fonts = (RadioGroup) findViewById(R.id.rg_fonts);
        this.rb_smallfont = (RadioButton) findViewById(R.id.rb_smallfont);
        this.rb_bigfont = (RadioButton) findViewById(R.id.rb_bigfont);
        this.rb_largefont = (RadioButton) findViewById(R.id.rb_largefont);
        this.rg_fonts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bigfont:
                        Utils.setFontTypePrefs("big", SettingsActivity.this.context);
                        Utils.setFontSizePrefs(String.valueOf((int) 40.0f), SettingsActivity.this.context);
                        return;
                    case R.id.rb_default:
                        Utils.setFontTypePrefs("default", SettingsActivity.this.context);
                        Utils.setFontSizePrefs("", SettingsActivity.this.context);
                        return;
                    case R.id.rb_largefont:
                        Utils.setFontTypePrefs("large", SettingsActivity.this.context);
                        Utils.setFontSizePrefs(String.valueOf((int) 45.0f), SettingsActivity.this.context);
                        return;
                    case R.id.rb_smallfont:
                        Utils.setFontTypePrefs("small", SettingsActivity.this.context);
                        Utils.setFontSizePrefs(String.valueOf((int) 35.0f), SettingsActivity.this.context);
                        return;
                    default:
                        return;
                }
            }
        });

        if (Utils.getChecked(context)) {
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }

        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.setChecked(context, b);
            }
        });


        choosecolor.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.color_popuplayout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            inflatecoulorVIews(dialog);
            dialog.show();
        });
    }

    private void inflatecoulorVIews(Dialog dialog) {
        FlexboxLayout llContainer = dialog.findViewById(R.id.v_container);
        Button btnSelectionDone = dialog.findViewById(R.id.btnSelectionDone);
        btnSelectionDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                clicked = -1;
            }
        });
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(5, 5, 5, 5);
        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llContainer.removeAllViews();
        for (int i = 0; i < listColor.size(); i++) {
            View v1 = linf.inflate(R.layout.row_color, null);
            v1.setLayoutParams(buttonLayoutParams);
            LinearLayout outerView = v1.findViewById(R.id.outerView);
            ImageView ivTick = v1.findViewById(R.id.ivTick);
            outerView.setBackgroundColor(Color.parseColor(listColor.get(i)));
            outerView.setId(i);

            if (clicked == i) {
                ivTick.setVisibility(View.VISIBLE);
            } else {
                ivTick.setVisibility(View.GONE);
            }
            ///=======
            outerView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clicked = outerView.getId();
                    Utils.setFontColor(listColor.get(clicked), context);
                    inflatecoulorVIews(dialog);
                }
            });
            llContainer.addView(v1);
        }
    }


    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(context);
    }


    private void showTimePickerDialog(int h, int m) {
        View view = getLayoutInflater().inflate(R.layout.timepicker_header, null);
        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme, new OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String str = SettingsActivity.this.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TimeFormat ");
                stringBuilder.append(DateFormat.is24HourFormat(SettingsActivity.this.context));
                Log.d(str, stringBuilder.toString());
                str = SettingsActivity.this.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("onTimeSet: hour ");
                stringBuilder.append(hour);
                Log.d(str, stringBuilder.toString());
                str = SettingsActivity.this.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("onTimeSet: min ");
                stringBuilder.append(min);
                Log.d(str, stringBuilder.toString());
                SettingsActivity.this.localData.set_hour(hour);
                SettingsActivity.this.localData.set_min(min);
                SettingsActivity.this.tvTime.setText(SettingsActivity.this.getFormatedTime(hour, min));
                Context context = SettingsActivity.this;
                //  NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), SettingsActivity.this.localData.get_min());
            }
        }, h, m, false);
        builder.setCustomTitle(view);
        builder.show();
    }

    public String getFormatedTimeHour(int h) {
        String OLD_FORMAT = "HH";
        String NEW_FORMAT = "hh";
        String oldDateString = String.valueOf(h);
        String newDateString = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH", getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern("hh");
            return sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return newDateString;
        }
    }

    public String getFormatedTime(int h, int m) {
        String OLD_FORMAT = "HH:mm";
        String NEW_FORMAT = "hh:mm a";
        StringBuilder oldDateString = new StringBuilder();
        oldDateString.append(h);
        oldDateString.append(":");
        oldDateString.append(m);
        //oldDateString = oldDateString;
        String newDateString = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", getCurrentLocale());
            Date d = sdf.parse(oldDateString.toString());
            sdf.applyPattern("hh:mm a");
            return sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return newDateString;
        }
    }

    @TargetApi(24)
    public Locale getCurrentLocale() {
        if (VERSION.SDK_INT >= 24) {
            return getResources().getConfiguration().getLocales().get(0);
        }
        return getResources().getConfiguration().locale;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(context, TimerActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }
}
