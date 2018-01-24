package com.example.user.login.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.login.DetailActivity;
import com.example.user.login.Helper.ArrayList_Helper;
import com.example.user.login.Helper.CustomAdapter;
import com.example.user.login.Helper.UserSessionManager;
import com.example.user.login.Helper.url_link;
import com.example.user.login.HomeActivity;
import com.example.user.login.R;
import com.example.user.login.model.AppointmentList;

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
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Oclemmy on 5/10/2016 for ProgrammingWizards Channel and http://www.Camposha.com.
 */
public class HomeFragment extends Fragment {
    CustomAdapter adapter=null;
    TextView text;
    String token = "";
    Context thiscontext;
    ArrayList_Helper arr = new ArrayList_Helper();
    String Diag = null;
    ProgressDialog pDialog;
    String xml="asss";
    String result;
    ListView lv;
    String type_url;
    private EditText editsearch;
    UserSessionManager session;

    AlertDialog alertDialog;
    ArrayList<AppointmentList> appointmentLists = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.home_fragment,container,false);
        session = new UserSessionManager(getContext());
        String type = "searchappointments";
        HomeActivity homeActivity = (HomeActivity) getActivity();
        editsearch = (EditText) rootView.findViewById(R.id.search);
        HashMap<String, String> user = session.getUserDetails();
        token = user.get(UserSessionManager.KEY_TOKEN);
        lv = (ListView) rootView.findViewById(R.id.ListAppointment);
        new Masuk().execute(type, token);
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });
        //new getData().execute(type,token);
        return rootView;
    }

    public class Masuk extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            type_url = params [0];
            String login_url;
            url_link link = new url_link();
            login_url = link.getUrl_link(type_url);
            if(type_url.equals("searchappointments") ){
                try {
                    String token = params [1];
                    URL url = new URL (login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")
                            +"&"+URLEncoder.encode("appointmentType","UTF-8")+"="+URLEncoder.encode("1","UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    result = "";
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
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(getContext()).create();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected void onPostExecute(String result) {

            JSONObject jsonObj = null;
            JSONObject jsonPatietnt = null;
            String reason="";
            JSONArray ja = null;
            int status=100;

            try

            {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject forgetpassword = jObj.getJSONObject("Appointments");
                ja = forgetpassword.getJSONArray("Appointment");
                status = forgetpassword.getInt("status");
                if(status!=0){
                    reason = forgetpassword.getString("reason");
                }


            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
           if(status == 0){
                String get = "getlist";

                try
                {
                    JSONObject jo;
                    appointmentLists.clear();
                    AppointmentList appointmentList;
                    for (int i=0;i<ja.length();i++)
                    {
                        jo=ja.getJSONObject(i);
                        String name= jo.getString("fname");
                        String middlename= jo.getString("mname");
                        String lastname= jo.getString("lname");
                        String pid= jo.getString("pid");
                        String pc_catname= jo.getString("pc_catid");
                        String app_id= jo.getString("pc_eid");
                        if(pc_catname.equals("10")){
                            Diag = new getMedical().execute(get,token,pid).get();
                            appointmentList = new AppointmentList();
                            appointmentList.setApp_Id(app_id);
                            appointmentList.setId(pid);
                            appointmentList.setFirstname(name);
                            appointmentList.setMiddlename(middlename);
                            appointmentList.setLastname(lastname);
                            appointmentList.setDiag(Diag);
                            appointmentLists.add(appointmentList);
                        }

                    }
                    adapter = new CustomAdapter(getContext(),appointmentLists);
                    lv.setAdapter(adapter);
                    lv.setDivider(null);
                    lv.setDividerHeight(0);
                    pDialog.dismiss();
                    session.Relogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

           }else {
               pDialog.dismiss();
               try {
                   session.Relogin();

                   HashMap<String, String> user = session.getUserDetails();
                   String new_token = user.get(UserSessionManager.KEY_TOKEN);
                   String type="searchappointments";
                   new Masuk().execute(type, new_token);
               } catch (ExecutionException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }


    }


    public class getMedical extends AsyncTask<String, Void, String >{

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj, jo;
            String tittle="";
            JSONObject jsonPatietnt = null;
            String reason = "";
            JSONArray ja = null;
            int status = 100;
            String result = "";
            String Medical="";
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
                                        tittle = jo.getString("title");
                                        Medical += " " + tittle;
                                    }
                                }
                            } else {
                                Medical = "Kosong";
                            }

                            return tittle;
                        } catch (JSONException e) {
                            Log.e("JSON exception", e.getMessage());
                            e.printStackTrace();
                        }

                    }else{
                        return Medical = "Kosong";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tittle;
        }
    }

}




