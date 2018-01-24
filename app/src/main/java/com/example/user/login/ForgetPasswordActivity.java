package com.example.user.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import 	android.view.Gravity;
import com.example.user.login.Helper.url_link;
import android.widget.Toast;
import android.text.TextUtils;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ForgetPasswordActivity extends Activity {
    EditText et_email;
    Button email;
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        et_email = (EditText) findViewById(R.id.et_email);
        email = (Button) findViewById(R.id.b_email);

        email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast msg = Toast.makeText(ForgetPasswordActivity.this, "Please Insert Your Email Addres.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                }else{
                    String type = "forgetpassword";
                    new Masuk().execute(type, email);
                }

            }
        });
    }
    public class Masuk extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        Context context;

        AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params [0];
            String login_url;
            url_link link = new url_link();
            login_url = link.getUrl_link(type);
            if(type.equals("forgetpassword")){
                try {
                    String email = params [1];
                    URL url = new URL (login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
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
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(ForgetPasswordActivity.this).create();
            alertDialog.setTitle("Sending Message");
            pDialog = new ProgressDialog(ForgetPasswordActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jsonObj = null;
            String reason="";
            int status=100;
            try

            {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject forgetpassword = jObj.getJSONObject("forgetpassword");
                status = forgetpassword.getInt("status");
                reason = forgetpassword.getString("reason");

            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            if(status == 0){
                alertDialog.setMessage(jsonObj.toString());
                alertDialog.show();
            }else if (status==-1){
                alertDialog.setMessage(jsonObj.toString());
                alertDialog.show();
            }
        }


    }
}