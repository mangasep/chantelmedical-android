package com.example.user.login.Helper;

import android.content.Context;
import android.util.Log;

import com.example.user.login.ForgetPasswordActivity;
import com.example.user.login.HomeActivity;
import com.example.user.login.model.AppointmentList;
import com.example.user.login.HomeActivity;

import org.json.JSONArray;
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
import java.util.ArrayList;

/**
 * Created by user on 31/07/2017.
 */

public class ArrayList_Helper {
    public String GetDiag(String... params) {
        JSONObject jsonObj, jo;
        JSONObject jsonPatietnt = null;
        String reason = "";
        JSONArray ja = null;
        int status = 100;
        String result = "";
        String Diag="";
        String type = params[0];
        String login_url;
        url_link link = new url_link();
        login_url = link.getUrl_link(type);
        if (type.equals("getlist")) {
            try {
                String token = params[1];
                String id = params[2];
                if (!id.equals("")) {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                            + "&" + URLEncoder.encode("patientId", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();


                    try {
                        jsonObj = XML.toJSONObject(result);
                        JSONObject jObj = new JSONObject(jsonObj.toString());
                        JSONObject forgetpassword = jObj.getJSONObject("Listitems");
                        status = forgetpassword.getInt("status");


                        if (status == 0) {
                            ja = forgetpassword.getJSONArray("listitem");
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                String diag_type = jo.getString("type");
                                if (diag_type.equals("medical_problem")) {
                                    String tittle = jo.getString("title");
                                    Diag += " " + tittle;
                                }
                            }
                        } else {
                            Diag = "Kosong";
                        }

                        return Diag;
                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        e.printStackTrace();
                    }

                }else{
                    return Diag = "Kosong";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Diag;
    }
}

