package com.example.dell.testlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 21/4/2560.
 */

public class Session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context ctx;


    public Session(Context ctx){

        this.ctx = ctx;
        preferences = ctx.getSharedPreferences("myApp", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoggedin(boolean loggedin){
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return preferences.getBoolean("loggedInmode", false);
    }

    public void setSaveDate(long time){
        editor.putLong("time", time);
        editor.commit();
    }

    public long getDate(){
        return preferences.getLong("time", 0);
    }

    public void setDateTypeString(String dateTypeString){
        editor.putString("datetime", dateTypeString);
        editor.commit();
    }

    public String getDateTypeString(){
        return preferences.getString("datetime", "dd:MM:yy:HH:mm:ss");
    }

/////////////////////////////////////////////////////////////////////////////
    public  void setTestsaveTime(String testsaveTime){
        editor.putString("testdate", testsaveTime);
        editor.commit();
    }
    public String getTestsaveTime(){
        return preferences.getString("testdate","dd:MM:yy:HH:mm:ss");
    }

    public void setClick(boolean click){
        editor.putBoolean("clickInmode", click);
        editor.commit();
    }

    public boolean getclick(){
        return preferences.getBoolean("clickInmode", false);
    }


}
