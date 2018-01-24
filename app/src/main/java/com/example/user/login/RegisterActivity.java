package com.example.user.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.example.user.login.Helper.url_link;
import android.content.Context;
import android.telephony.TelephonyManager;

public class RegisterActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    Button buttonRegister;
    private TelephonyManager mTelephonyManager;
    String device_token;
    TextView login;
    private EditText input_username, input_email, input_password, input_confirm_password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_username = (EditText) findViewById(R.id.signup_input_username);
        input_email = (EditText) findViewById(R.id.signup_input_email);
        input_password = (EditText) findViewById(R.id.signup_input_password);
        input_confirm_password = (EditText) findViewById(R.id.signup_input_confirm_password);
        final TextInputLayout con_pass = (TextInputLayout) findViewById(R.id.con_pass_lay);
        final TextInputLayout pass = (TextInputLayout) findViewById(R.id.pass_lay);
        final TextInputLayout user = (TextInputLayout) findViewById(R.id.user_lay);
        final TextInputLayout email = (TextInputLayout) findViewById(R.id.email_lay);

        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (input_password.getText().length()< 6 || input_password.getText().length()> 15 ){
                    pass.setError("Password between 6 and 10 alphanumeric characters.");
                    buttonRegister.setEnabled(false);
                }else {
                    pass.setError(null);
                    buttonRegister.setEnabled(true);
                }
            }
        });

        input_email.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (TextUtils.isEmpty(input_email.getText().toString()) || !input_email.getText().toString().matches(emailPattern)){
                    email.setError("Enter a valid email address.");
                    buttonRegister.setEnabled(false);
                }else {
                    email.setError(null);
                    buttonRegister.setEnabled(true);
                }
            }
        });


        input_username.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (input_username.getText().toString().equals("")){
                    user.setError("Username is Empty.");
                    buttonRegister.setEnabled(false);
                }else {
                    user.setError(null);
                    buttonRegister.setEnabled(true);
                }
            }
        });

        input_confirm_password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (!input_confirm_password.getText().toString().equals(input_password.getText().toString())){
                    con_pass.setError("Password Doesn't Match.");
                    buttonRegister.setEnabled(false);
                }else {
                    con_pass.setError(null);
                    buttonRegister.setEnabled(true);
                }
            }
        });

        login = (TextView) findViewById(R.id.link_login);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = input_username.getText().toString().toLowerCase();
                String email = input_email.getText().toString().toLowerCase();
                String password = input_password.getText().toString();
                String confirm_password = input_confirm_password.getText().toString();
                TelephonyManager device_id = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                device_token = device_id.getDeviceId().toString();
                String type = "register";
                    new Masuk().execute(type,username,email, password, device_token);
            }
        });
    }

    private class Masuk extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String login_url;
            url_link link = new url_link();
            login_url = link.getUrl_link(type);
            if (type.equals("register")) {
                try {
                    String username = params[1];
                    String email = params[2];
                    String password = params[3];
                    String device_token = params[4];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
                            + "&" + URLEncoder.encode("device_token", "UTF-8") + "=" + URLEncoder.encode(device_token, "UTF-8")
                            ;
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Register Status");
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jsonObj = null;
            String reason = "";


            int status = 100;
            try

            {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject MedMasterUser = jObj.getJSONObject("MedMasterUser");
                status = MedMasterUser.getInt("status");
                reason = MedMasterUser.getString("reason");

            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            if (status == 0) {
                alertDialog.setMessage(reason);
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent Home = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(Home);
                    }
                }, 3000);
            } else {
                alertDialog.setMessage(reason);
                alertDialog.show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
    }

    private void getDeviceImei() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = mTelephonyManager.getDeviceId();
        Log.d("msg", "DeviceImei " + deviceid);
    }


}
