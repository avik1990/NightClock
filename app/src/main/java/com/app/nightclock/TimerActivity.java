package com.app.nightclock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nightclock.alarmutils.LocalData;
import com.app.nightclock.util.Utils;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity implements OnClickListener {
    boolean clicked = false;
    Context context;
    TextView digital_ampm;
    CustomDigitalClock digital_clock;
    DigitalClock digital_clock1;
    CustomDigitalClockSecond digital_clock_second;
    CustomDigitalClockMin digital_clock_min;
    TextView digital_day;
    int hour;
    ImageView iv_lock;
    ImageView iv_settings;
    LocalData localData;
    float screenWidth,screenHeight;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            TextView textView = TimerActivity.this.tv_batterystatus;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(level));
            stringBuilder.append(" %");
            textView.setText(stringBuilder.toString());
        }
    };
    RelativeLayout main_view;
    int min;
    TextView tv_alarmset;
    TextView tv_battery;
    TextView tv_batterystatus;
    TextView tv_brack1;
    TextView tv_brack2;
    TextView tv_date;
    LinearLayout v_alarm;
    ImageView iv_rotate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView((int) R.layout.activity_main);
        context = this;
        localData = new LocalData(this.context);


        //Toast.makeText(context, ""+Utils.getAutoStart(context), Toast.LENGTH_SHORT).show();
        if(!Utils.getAutoStart(context)) {
            AutoStartHelper.getInstance().getAutoStartPermission(context);
        }

        initViews();

        digital_clock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
                UpdateDateSet();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        try {
            hour = this.localData.get_hour();
            min = this.localData.get_min();
        } catch (Exception e) {
            v_alarm.setVisibility(View.GONE);
        }
        localData = new LocalData(getApplicationContext());
        registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        hideSystemUI();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }
        else
        {
            Display display = getWindowManager().getDefaultDisplay();
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }

        setTextSize();

    }

    private void setTextSize() {
        Paint paint = new Paint();
        float textWidth = paint.measureText("00");
        float textSize = (int) ((screenWidth / textWidth) * paint.getTextSize());
        paint.setTextSize(textSize);

        textWidth = paint.measureText("00");
        textSize = (int) ((screenWidth / textWidth) * paint.getTextSize());

        // Re-measure with font size near our desired result
        paint.setTextSize(textSize);

        // Check height constraints
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        float textHeight = metrics.descent - metrics.ascent;
        if (textHeight > screenHeight) {
            textSize = (int) (textSize * (screenHeight / textHeight));
            paint.setTextSize(textSize);
        }
        Log.e("TEXTSIZE",""+(textSize*.17));
       // return textSize;

    }

    private void UpdateDateSet() {
        digital_day.setText(Utils.getCurrentDay());
        digital_ampm.setText(Utils.getAMPM());
        tv_date.setText(Utils.getCurrentDate());
    }


    private void initViews() {
        iv_rotate = findViewById(R.id.iv_rotate);
        digital_clock_min = findViewById(R.id.digital_clock_min);
        digital_clock_second = findViewById(R.id.digital_clock_second);
        tv_alarmset = (TextView) findViewById(R.id.tv_alarmset);
        v_alarm = (LinearLayout) findViewById(R.id.v_alarm);
        tv_battery = (TextView) findViewById(R.id.tv_battery);
        digital_clock = findViewById(R.id.digital_clock);
        digital_clock1 = (DigitalClock) findViewById(R.id.digital_clock1);
        tv_batterystatus = (TextView) findViewById(R.id.tv_batterystatus);
        tv_date = findViewById(R.id.tv_date);

        digital_day = findViewById(R.id.digital_day);

        tv_brack1 = (TextView) findViewById(R.id.tv_brack1);
        tv_brack2 = (TextView) findViewById(R.id.tv_brack2);
        digital_ampm = findViewById(R.id.digital_ampm);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        main_view = (RelativeLayout) findViewById(R.id.main_view);
        iv_settings.setOnClickListener(this);
        iv_lock.setOnClickListener(this);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        digital_clock.setTypeface(font);
        digital_ampm.setTypeface(font);
        tv_battery.setTypeface(font);

        Typeface fontdb = Typeface.createFromAsset(getAssets(), "fonts/digibd.ttf");
        tv_batterystatus.setTypeface(fontdb);
        tv_date.setTypeface(fontdb);
        digital_clock.setTypeface(fontdb);
        digital_clock_second.setTypeface(fontdb);
        digital_clock_min.setTypeface(fontdb);
        digital_clock1.setTypeface(fontdb);
        tv_brack1.setTypeface(fontdb);
        tv_brack2.setTypeface(fontdb);
        digital_day.setTypeface(fontdb);

        digital_day.setText(Utils.getCurrentDay());
        digital_ampm.setText(Utils.getAMPM());
        tv_date.setText(Utils.getCurrentDate());

        main_view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                TimerActivity.this.hideSystemUI();
                if (iv_settings.getVisibility() == View.VISIBLE) {
                    iv_settings.setVisibility(View.GONE);
                    iv_lock.setVisibility(View.GONE);
                    iv_rotate.setVisibility(View.GONE);

                } else {
                    iv_rotate.setVisibility(View.VISIBLE);
                    iv_settings.setVisibility(View.VISIBLE);
                    iv_lock.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        if (Utils.getBtnClicked(this.context)) {
            lockOrientation(this);
            iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock));
            return;
        }
        iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock));
        iv_rotate.setOnClickListener(this);
        unlockOrientation(this);
    }

    public Locale getCurrentLocale() {
        if (VERSION.SDK_INT >= 24) {
            return getResources().getConfiguration().getLocales().get(0);
        }
        return getResources().getConfiguration().locale;
    }

    public String getFormatedTime(int h, int m) {
        String OLD_FORMAT = "HH:mm";
        String NEW_FORMAT = "hh:mm a";
        StringBuilder oldDateString = new StringBuilder();
        oldDateString.append(h);
        oldDateString.append(":");
        oldDateString.append(m);
        oldDateString = oldDateString;
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

    private void setFontSize() {
        if (!Utils.getFontTypePrefs(this.context).isEmpty() && !Utils.getFontTypePrefs(this.context).equalsIgnoreCase("default")) {
            digital_clock.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) + 10));
            tv_batterystatus.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) - 10));
            tv_date.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) - 10));
            digital_day.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) - 10));
            digital_ampm.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) - 10));
            digital_clock1.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) + 10));
            tv_battery.setTextSize(2, (float) (Integer.parseInt(Utils.getFontSizePrefs(this.context)) - 10));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hideSystemUI();
    }


    protected void onResume() {
        super.onResume();
        changeColor();
        try {
            hour = this.localData.get_hour();
            min = this.localData.get_min();
        } catch (Exception e) {
            try {
                this.v_alarm.setVisibility(View.GONE);
            } catch (Exception e2) {
                return;
            }
        }
        if (getFormatedTime(this.hour, this.min).isEmpty()) {
            v_alarm.setVisibility(View.GONE);
        } else {
            v_alarm.setVisibility(View.VISIBLE);
            tv_alarmset.setText(getFormatedTime(this.hour, this.min));
            if (localData.getReminderStatus()) {
                v_alarm.setVisibility(View.VISIBLE);
            } else {
                v_alarm.setVisibility(View.GONE);
            }
        }
    }

    private void changeColor() {
        if (Utils.getChecked(context)) {
            digital_ampm.setVisibility(View.INVISIBLE);
        } else {
            digital_ampm.setVisibility(View.VISIBLE);
        }
        if (!Utils.getFontColor(context).isEmpty()) {
            digital_clock.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            digital_clock_min.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            digital_clock_second.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            tv_battery.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            tv_date.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            digital_day.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            digital_ampm.setTextColor(Color.parseColor(Utils.getFontColor(context)));
            tv_batterystatus.setTextColor(Color.parseColor(Utils.getFontColor(context)));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBatInfoReceiver);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(3846);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void onClick(View v) {
        if (v == this.iv_settings) {
            startActivity(new Intent(this.context, SettingsActivity.class));
        } else if (v == iv_lock) {
            if (Utils.getBtnClicked(context)) {
                unlockOrientation(this);
                Utils.setbuttonClicked(context, false);
                this.iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock));
                return;
            } else {
                hideSystemUI();
                Utils.setbuttonClicked(this.context, true);
                lockOrientation(this);
                iv_lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock));
            }
        } else if (v == iv_rotate) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            }
        }
    }

    public void onBackPressed() {
        if (!Utils.getBtnClicked(this.context)) {
            super.onBackPressed();
        }
    }

    public static void lockOrientation(Activity activity) {
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int tempOrientation = activity.getResources().getConfiguration().orientation;
        int orientation = 0;
        switch (tempOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        }
        activity.setRequestedOrientation(orientation);
    }

    public static void unlockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


}
