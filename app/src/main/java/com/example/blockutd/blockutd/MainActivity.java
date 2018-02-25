package com.example.blockutd.blockutd;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.util.Log.*;
import static java.io.Console.*;

public class MainActivity extends AppCompatActivity {

    private long endTime;
    private static long beginTime;
    private static Context context;
    private boolean permissionGranted;
    private List<UsageStats> stats;
    private UsageStatsManager usageStatsManager;
    private static MainActivity instance;

    //public variables
    public static ArrayList<String> bad_apps;
    public static ArrayList<Long> stats_for_apps;
    public static long totalMinutes;

    public static MainActivity getInstance() {
        return instance;
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }
    //if they turn on the
    public static void resetBeginTime(){beginTime = System.currentTimeMillis();}
    public static int getTotalMinutes() {return (int)(totalMinutes);}

    protected void onCreate(Bundle savedInstanceState) {
        bad_apps = new ArrayList<String>();
        stats_for_apps = new ArrayList<Long>();
        totalMinutes = 0;


        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        endTime = System.currentTimeMillis();
        if(beginTime < 10)
            beginTime = endTime-1000*60*60*12; //12 hours ago
        permissionGranted = checkForPermission(getContext());
        if(!permissionGranted) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            permissionGranted = checkForPermission(getContext());
        }
        if(permissionGranted) {
            usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, beginTime,
                    endTime);

//            String all_stats = "";
//            for(int i = 0; i < stats.size(); ++i) {
//                all_stats += "" + stats.get(i).getPackageName() + " Total time " + (stats.get(i).getLastTimeStamp()-stats.get(i).getFirstTimeStamp()) + "\n";
//            }
//            //check to make sure list works
//            TextView allStats = (TextView) findViewById(R.id.first);
//            allStats.setText(all_stats);
        }
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());

        return mode == MODE_ALLOWED;
    }

    private void calculatePayment() {
        //instantiating stats arraylist
        for(int a = 0; a < bad_apps.size(); a++) {
            stats_for_apps.add((long)0);
        }

        //simply adding milliseconds
        for(int i = 0; i < stats.size(); i++ ) {
            for(int j = 0; j < bad_apps.size(); j++) {
                if(stats.get(i).getPackageName().contains(bad_apps.get(j))) {
                    long min = beginTime;
                    if(beginTime > stats.get(i).getFirstTimeStamp()) {
                        min = stats.get(i).getFirstTimeStamp();
                    }
                    stats_for_apps.set(j,stats_for_apps.get(j) + (stats.get(i).getLastTimeStamp() - min));
                }
            }
        }

        //calculating minutes instead of miliseconds
        for(int b = 0; b < stats_for_apps.size(); ++b) {
            long z = stats_for_apps.get(b)/1000/60;
            z = Math.round(100*z)/100;
            stats_for_apps.set(b, z);
            //rounding to nearest .01 minutes
            totalMinutes += z;
        }
    }

    public void saveSettings(View view) {
        CheckBox temp = (CheckBox) findViewById(R.id.fb);
        if(temp.isChecked()) {
            bad_apps.add("facebook");
        }
        temp = (CheckBox) findViewById(R.id.instagram);
        if(temp.isChecked()) {
            bad_apps.add("instagram");
        }
        temp = (CheckBox) findViewById(R.id.twitter);
        if(temp.isChecked()) {
            bad_apps.add("twitter");
        }
        temp = (CheckBox) findViewById(R.id.snapchat);
        if(temp.isChecked()) {
            bad_apps.add("snapchat");
        }
        temp = (CheckBox) findViewById(R.id.reddit);
        if(temp.isChecked()) {
            bad_apps.add("reddit");
        }
        temp = (CheckBox) findViewById(R.id.youtube);
        if(temp.isChecked()) {
            bad_apps.add("youtube");
        }
        temp = (CheckBox) findViewById(R.id.netflix);
        if(temp.isChecked()) {
            bad_apps.add("netflix");
        }
        temp = (CheckBox) findViewById(R.id.tumblr);
        if(temp.isChecked()) {
            bad_apps.add("tumblr");
        }
        Intent myIntent = new Intent(MainActivity.this, EtherHandler.class);
        calculatePayment();
        startActivity(myIntent);

    }
}
