package com.example.dell.testlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
 private Button btnBack;
    private  Button btnAtt;
    private Button btnJobLogout;
    private Button btnTest;
    private Session session;
    private static final String TAG = "MyActivity";
    private SimpleDateFormat format;
    private SimpleDateFormat cFormat;
    private Calendar calendar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        showInfo();

        session = new Session(this);
        if (!session.loggedin()) {
            logout();
        }
        // btnBack
        btnBack = (Button) findViewById(R.id.btnBack);
        // Perform action on click
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                logout();

//                SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.commit();
//
//                Log.d(LOG, editor.toString());
//
//                Intent newActivity = new Intent(DetailActivity.this,MainActivity.class);
//                startActivity(newActivity);

            }
        });

        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            Intent newActivity = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(newActivity);
            }
        });

        btnAtt = (Button) findViewById(R.id.btnAtt);
        btnJobLogout = (Button) findViewById(R.id.btnJobLogout);

        btnAtt.setEnabled(true);
        btnJobLogout.setEnabled(false);

        format = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        calendar = Calendar.getInstance();

        final long time= System.currentTimeMillis();

            if (btnAtt.isEnabled()) {
                btnAtt.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        checkAttendance();
                        session.setDateTypeString(format.format(calendar.getTime()));
                        Log.d(TAG,session.getDateTypeString());
//                        session.setSaveDate(time);
//                        Log.d(TAG,Long.toString(session.getDate()));
//                        btnAtt.setEnabled(false);
//                        btnJobLogout.setEnabled(true);
                        checkTime();
                        btnJobLogout.setEnabled(true);
                    }
                });
            }

            if (!btnJobLogout.isEnabled()) {

                btnJobLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jobLogout();
                        btnJobLogout.setEnabled(false);
//                        btnAtt.setEnabled(true);

                    }
                });
            }
    }

    public void checkTime(){
        if (session.getTestsaveTime().compareTo(defultDate())<0){
            btnAtt.setEnabled(true);
        }else{
            btnAtt.setEnabled(false);
        }

    }

    //logout mehtod
private void logout(){
    session.setLoggedin(false);
    finish();
    startActivity(new Intent(DetailActivity.this, MainActivity.class));
}


//showInfo
    public void showInfo()
    {
        final TextView tMemberID = (TextView)findViewById(R.id.txtMemberID);
        final TextView tFirstname = (TextView)findViewById(R.id.txtFirstname);
        final TextView tLastname = (TextView)findViewById(R.id.txtLastname);
        final TextView tTitle = (TextView)findViewById(R.id.txtTitle);
        final TextView tEmail = (TextView)findViewById(R.id.txtEmail);
        final TextView tTel = (TextView)findViewById(R.id.txtTel);

        String url = "http://www.mocky.io/v2/58f8f8491100003f1ea175aa";

        Intent intent= getIntent();
        final String MemberID = intent.getStringExtra("id");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", MemberID));


        String resultServer  = getHttpPost(url,params);

        String strMemberID = "";
        String strFirstname = "";
        String strLastname = "";
        String strTitle = "";
        String strEmail = "";
        String strTel = "";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strMemberID = c.getString("id");
            strFirstname = c.getString("firstname");
            strLastname = c.getString("lastname");
            strTitle = c.getString("title");
            strEmail = c.getString("email");
            strTel = c.getString("phone_number");

            if(!strMemberID.equals(""))
            {
                tMemberID.setText(strMemberID);
                tFirstname.setText(strFirstname);
                tLastname.setText(strLastname);
                tTitle.setText(strTitle);
                tEmail.setText(strEmail);
                tTel.setText(strTel);
            }
            else
            {
                tMemberID.setText("-");
                tFirstname.setText("-");
                tLastname.setText("-");
                tTitle.setText("-");
                tEmail.setText("-");
                tTel.setText("-");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

//checkAttendance
    private void checkAttendance() {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        String url = "http://www.mocky.io/v2/58fed8fc110000dd06f5fe04";

        Intent intent= getIntent();
        final String MemberID = intent.getStringExtra("id");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", MemberID));

        String resultServer  = getHttpPost(url,params);

      //defult value
        String strTime = "";
        String strError = "Attendance false!";
        String strSuccess =("Attendance Success" + strTime);
        String strStatus = "0";


        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatus = c.getString("Status");
            strError = c.getString("Error");
            strSuccess = c.getString("Success");


        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(strStatus.equals("200")){
            ad.setTitle("Success! ");
            ad.setPositiveButton("Close", null);
            ad.setMessage(strSuccess);

            ad.show();

        }else{
            ad.setTitle("Error! ");
            ad.setPositiveButton("Close", null);
            ad.setMessage(strError);
            ad.show();
        }

    }

    //office Logout
    private void jobLogout(){

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        String url = "http://www.mocky.io/v2/58fed8fc110000dd06f5fe04";

        Intent intent= getIntent();
        final String MemberID = intent.getStringExtra("id");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", MemberID));


        String resultServer  = getHttpPost(url,params);

        //defult value
        String strError = "logout false!";
        String strSuccess ="logout Success";
        String strStatus = "0";
        String strTime = "";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatus = c.getString("Status");
            strError = c.getString("Error");
            strSuccess = c.getString("Success");
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(strStatus.equals("200")){
            ad.setTitle("Success! ");
            ad.setPositiveButton("Close", null);
            ad.setMessage(strSuccess);
            ad.show();

        }else{
            ad.setTitle("Error! ");
            ad.setPositiveButton("Close", null);
            ad.setMessage(strError);
            ad.show();
        }
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }

}