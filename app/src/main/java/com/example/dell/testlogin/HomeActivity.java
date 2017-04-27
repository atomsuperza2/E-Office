package com.example.dell.testlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    Session session;
    Button button1;
    Button button2;
    private SimpleDateFormat format;
    private SimpleDateFormat cFormat;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new Session(this);
        final TextView textView = (TextView)findViewById(R.id.txt1);
        textView.setText(session.getTestsaveTime());
//        textView.setText(Long.toString(session.getDate()));

        final TextView tc = (TextView)findViewById(R.id.textView1);
        tc.setText(defultDate());

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button1.setEnabled(true);
        button2.setEnabled(true);
        checkCondition();

        format = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        calendar = Calendar.getInstance();

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                session.setClick(true);
                session.setTestsaveTime(format.format(calendar.getTime()));
                button1.setEnabled(false);

            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                session.setClick(false);
                Intent newActivity = new Intent(HomeActivity.this, DetailActivity.class);
                startActivity(newActivity);
            }
        });
//            if(button1.isEnabled()){
//            button1.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View v){
//                    session.setTestsaveTime(format.format(calendar.getTime()));
//                    checkTime();
//                    button2.setEnabled(true);
//
//                }
//            });
//            }
//
//            if(!button2.isEnabled()){
//            button2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    button2.setEnabled(false);
//
//
//                }
//            });
//        }
    }

    private String defultDate(){

        cFormat = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String defultDate = cFormat.format(calendar.getTime());

        return defultDate;

    }

    public void checkCondition(){
        if (isClickedToDay()&&isCheckTime()){
            button1.setEnabled(false);
        }else{
            button1.setEnabled(true);
        }
    }
    public boolean isClickedToDay()
    {
        if(session.getclick()) {
            return true;
        }else {
            return  false;
        }
    }
    public boolean isCheckTime(){
        if(session.getTestsaveTime().equals(defultDate())){
            return true;
        }
        else{
            return false;
        }
    }
}
