package com.example.user.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.user.login.Helper.DatabaseHandler;
import com.example.user.login.Helper.UserSessionManager;
import  com.example.user.login.Helper.url_link;
import com.example.user.login.model.Token;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity  {
    private DatabaseHandler db;
    Button login;
    TextView forgot_pass,register;
    EditText username_et, password_et;
    String username,password;
    UserSessionManager session;
    String type ="login";
    String login_url;
    String getPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new UserSessionManager(getApplicationContext());
        login = (Button) findViewById(R.id.b_login);
        forgot_pass = (TextView) findViewById(R.id.forgot_pass);
        register = (TextView) findViewById(R.id.register);
        username_et = (EditText) findViewById(R.id.et_username);
        password_et = (EditText) findViewById(R.id.et_password);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.check_login_with_pin);

        forgot_pass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forget);

            }
        });


        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(forget);

            }
        });


        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    type = "loginwithpin";
                }else if (!checkBox.isChecked()){
                    type = "login";
                }else {
                    type ="login";
                }

            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                username = username_et.getText().toString().toLowerCase();
                password = password_et.getText().toString().toLowerCase();
                getPassword= password;
                if (TextUtils.isEmpty(username)) {
                    Toast msg = Toast.makeText(LoginActivity.this, "Username is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                }else if (TextUtils.isEmpty(password)){
                    Toast msg = Toast.makeText(LoginActivity.this, "Password is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                }else{
                    new Masuk().execute(type, username, password);
                }
            }
        });
    }

    private class Masuk extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params [0];
            url_link link = new url_link();
            login_url = link.getUrl_link(type);
            String username = params [1];
            String password = params [2];
            if(type.equals("login")){
                try {
                    URL url = new URL (login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")
                            +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line="";
                    while((line=bufferedReader.readLine())!= null){
                        result += line;

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else if (type.equals("loginwithpin")) {
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result1 = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result1 += line;

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result1;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Login Status");
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jsonObj = null;
            String reason="";
            String token = "";

            int status=100;
            try

            {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject MedMasterUser = jObj.getJSONObject("MedMasterUser");
                status = MedMasterUser.getInt("status");
                reason = MedMasterUser.getString("reason");

                if (status == 0){
                    token = MedMasterUser.getString("token");
                }else{
                    token = null;
                }
            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            if(status == -1){
                alertDialog.setMessage(reason);
                alertDialog.show();
            }else if (status == 0){
                String device_id = password;
                db = new DatabaseHandler(LoginActivity.this);
                Intent Home = new Intent(LoginActivity.this, HomeActivity.class);
                alertDialog.setMessage(jsonObj.toString());
                alertDialog.show();
                alertDialog.dismiss();
                session.createUserLoginSession(username,getPassword,token,type);
                db.CreateToken(new Token(null, token, device_id));
                startActivity(Home);
                finish();
            }
        }


    }



}
