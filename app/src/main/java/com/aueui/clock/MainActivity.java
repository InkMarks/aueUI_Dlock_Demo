package com.aueui.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aueui.clock.Activity.AddAlarm;
import com.aueui.clock.Adpters.AuePagerAdpter;
import com.aueui.clock.Utils.AnimationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ViewPager mViewPager;
    private TextView tv_alarm_clock, tv_time, tv_stopwatch, tv_timer, tv_date, tv_min, tv_sec;
    private List<View> views;
    private View alarm, time, stopwatch, timer;
    private Button button;
    private boolean isPaused = false;
    private String timePass;
    private int PassedTime;
    private boolean isStart = false;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        initView();
        initTime();
        initStopWatch();
    }

    private void initStopWatch() {
        tv_min = stopwatch.findViewById(R.id.tv_min);
        tv_sec = stopwatch.findViewById(R.id.tv_sec);
    }

    @SuppressLint("SetTextI18n")
    private void initTime() {
        tv_date = time.findViewById(R.id.date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String[] str = new String[]{"", "日", "一", "二", "三", "四", "五", "六"};
        tv_date.setText(simpleDateFormat.format(date) + "\t周" + str[Calendar.DAY_OF_WEEK]);
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        tv_alarm_clock = findViewById(R.id.tv_alarm_clock);
        tv_time = findViewById(R.id.tv_time);
        tv_stopwatch = findViewById(R.id.tv_stopwatch);
        tv_timer = findViewById(R.id.tv_timer);
        button = findViewById(R.id.button);
        tv_alarm_clock.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_stopwatch.setOnClickListener(this);
        tv_timer.setOnClickListener(this);
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
        views = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater();
        alarm = layoutInflater.inflate(R.layout.alarm, null);
        time = layoutInflater.inflate(R.layout.time, null);
        stopwatch = layoutInflater.inflate(R.layout.stopwatch, null);
        timer = layoutInflater.inflate(R.layout.timer, null);
        views.add(alarm);
        views.add(time);
        views.add(stopwatch);
        views.add(timer);
        mViewPager.setPageMargin(20);
        mViewPager.setAdapter(new AuePagerAdpter(views));
        mViewPager.setCurrentItem(0);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            boolean isFromTime = false;

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        tv_alarm_clock.setTextColor(getResources().getColor(R.color.blue));
                        tv_time.setTextColor(Color.BLACK);
                        tv_stopwatch.setTextColor(Color.BLACK);
                        tv_timer.setTextColor(Color.BLACK);
                        if (isFromTime) {
                            button.setBackground(getResources().getDrawable(R.drawable.add));
                            button.setVisibility(View.VISIBLE);
                            button.setAnimation(AnimationUtils.moveToViewLocation());
                        }
                        isFromTime = false;
                        break;
                    case 1:
                        tv_alarm_clock.setTextColor(Color.BLACK);
                        tv_time.setTextColor(getResources().getColor(R.color.blue));
                        tv_stopwatch.setTextColor(Color.BLACK);
                        tv_timer.setTextColor(Color.BLACK);
                        button.setVisibility(View.GONE);
                        button.setAnimation(AnimationUtils.moveToViewBottom());
                        isFromTime = true;
                        break;
                    case 2:
                        tv_alarm_clock.setTextColor(Color.BLACK);
                        tv_time.setTextColor(Color.BLACK);
                        tv_stopwatch.setTextColor(getResources().getColor(R.color.blue));
                        tv_timer.setTextColor(Color.BLACK);
                        if (isFromTime) {
                            button.setVisibility(View.VISIBLE);
                            button.setAnimation(AnimationUtils.moveToViewLocation());
                        }
                        button.setBackground(getResources().getDrawable(R.drawable.go));
                        button.setPadding(0, 2, 0, 0);
                        isFromTime = false;
                        break;
                    case 3:
                        tv_alarm_clock.setTextColor(Color.BLACK);
                        tv_time.setTextColor(Color.BLACK);
                        tv_stopwatch.setTextColor(Color.BLACK);
                        tv_timer.setTextColor(getResources().getColor(R.color.blue));
                        if (isFromTime) {
                            button.setVisibility(View.VISIBLE);
                            button.setAnimation(AnimationUtils.moveToViewLocation());
                        }
                        button.setBackground(getResources().getDrawable(R.drawable.go));
                        button.setPadding(0, 2, 0, 0);
                        button.setTextSize(20);
                        isFromTime = false;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (!isPaused) {
                        setPassedTime();
                        UpData();
                    }
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    handler.sendEmptyMessage(2);
                    break;
            }
        }
    };

    private void startTime() {
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private void UpData() {
        tv_min.setText(getMin());
        tv_sec.setText(getSec());
    }

    public void setPassedTime() {
        PassedTime = PassedTime + 1;
        timePass = this.getMin() + "" + this.getSec();
    }

    public CharSequence getMin() {
        return String.valueOf(PassedTime / 60);
    }

    public CharSequence getSec() {
        int sec = PassedTime % 60;
        return sec < 10 ? "0" + sec : String.valueOf(sec);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_alarm_clock:
                tv_alarm_clock.setTextColor(getResources().getColor(R.color.blue));
                tv_time.setTextColor(Color.BLACK);
                tv_stopwatch.setTextColor(Color.BLACK);
                tv_timer.setTextColor(Color.BLACK);
                mViewPager.setCurrentItem(0);
                button.setBackground(getResources().getDrawable(R.drawable.add));
                break;
            case R.id.tv_time:
                tv_alarm_clock.setTextColor(Color.BLACK);
                tv_time.setTextColor(getResources().getColor(R.color.blue));
                tv_stopwatch.setTextColor(Color.BLACK);
                tv_timer.setTextColor(Color.BLACK);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_stopwatch:
                tv_alarm_clock.setTextColor(Color.BLACK);
                tv_time.setTextColor(Color.BLACK);
                tv_stopwatch.setTextColor(getResources().getColor(R.color.blue));
                tv_timer.setTextColor(Color.BLACK);
                mViewPager.setCurrentItem(2);
                button.setBackground(getResources().getDrawable(R.drawable.go));
                break;
            case R.id.tv_timer:
                tv_alarm_clock.setTextColor(Color.BLACK);
                tv_time.setTextColor(Color.BLACK);
                tv_stopwatch.setTextColor(Color.BLACK);
                tv_timer.setTextColor(getResources().getColor(R.color.blue));
                mViewPager.setCurrentItem(3);
                button.setBackground(getResources().getDrawable(R.drawable.go));
                break;
            case R.id.button:
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, AddAlarm.class));
                        break;
                    case 2:
                        if (!isStart) {
                            Toast.makeText(MainActivity.this,getResources().getText(R.string.stopwatch_tips_reset),Toast.LENGTH_SHORT).show();
                            handler.removeMessages(1);
                            startTime();
                            isPaused = false;
                            button.setBackground(getResources().getDrawable(R.drawable.stop));
                            isStart = true;
                        } else {
                            isPaused = true;
                            // PassedTime = 0;
                            button.setBackground(getResources().getDrawable(R.drawable.go));
                            isStart = false;
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                switch (mViewPager.getCurrentItem()) {
                    case 2:
//                        handler.removeMessages(1);
//                        isPaused = true;
//                        PassedTime = 0;
//                        button.setText("重置");
//                        isStart = false;
                        recreate();
                        break;
                }
                break;
        }
        return false;
    }
}
