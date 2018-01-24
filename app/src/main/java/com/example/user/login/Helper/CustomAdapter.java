package com.example.user.login.Helper;

/**
 * Created by user on 21/07/2017.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.login.DetailActivity;
import com.example.user.login.HomeActivity;
import com.example.user.login.R;
import com.example.user.login.model.AppointmentList;
import com.example.user.login.model.Token;

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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Oclemy on 7/15/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class CustomAdapter  extends BaseAdapter{

    Context c;
    HomeActivity homeActivity = new HomeActivity();
    ArrayList<AppointmentList> appointmentLists;
    ArrayList<AppointmentList> searchlist=null;
    UserSessionManager session;
    public CustomAdapter(Context c, ArrayList<AppointmentList> appointmentLists) {
        this.c = c;
        this.appointmentLists = appointmentLists;
        this.searchlist = new ArrayList<AppointmentList>();
        this.searchlist.addAll(appointmentLists);
    }

    @Override
    public int getCount() {
        return appointmentLists.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);

        }

        TextView nameTxt= (TextView) view.findViewById(R.id.nameTxt);
        TextView emailTxt= (TextView) view.findViewById(R.id.emailTxt);
        Button accept = (Button)view.findViewById(R.id.accept);
        Button decline = (Button)view.findViewById(R.id.decline);
        AppointmentList appointmentList= (AppointmentList) this.getItem(i);

        final String name=appointmentList.getFirstname();
        final String middlename=appointmentList.getMiddlename();
        final String lastname=appointmentList.getLastname();
        final String diag=appointmentList.getDiag();
        final String Fullname;
        final String App_Id = appointmentList.getApp_Id();
        final String Id=appointmentList.getId();


        if (middlename.equals("")){
            Fullname= name+" "+ lastname;
            emailTxt.setText (diag);

        }else if(name.equals("")){
            Fullname = middlename+" "+ lastname;
            emailTxt.setText (diag);

        }else if (lastname.equals("")){
            Fullname = name+" "+ middlename;
            emailTxt.setText (diag);

        }else if (!name.equals("") && !middlename.equals("") && !lastname.equals("")){
            Fullname = name+" "+ middlename+" "+ lastname;
            emailTxt.setText (diag);
        }else {
            Fullname = "Tanpa Nama";
            emailTxt.setText (diag);
        }

        nameTxt.setText(Fullname);


        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c)
                        .setTitle("Delete Appointment List")
                        .setMessage("Are you sure you want to delete this appointment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler db = new DatabaseHandler(c);
                                String type = "deleteappointment";
                                String token = null;
                                session = new UserSessionManager(c);
                                HashMap<String, String> user = session.getUserDetails();
                                token = user.get(UserSessionManager.KEY_TOKEN);
                                new Delete_appointment().execute(type,token,App_Id);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        ;

                builder.show();

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN DETAIL ACTIVITY
                openDetailActivity(Fullname,diag,Id);

            }
        });

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN DETAIL ACTIVITY
                openDetailActivity(Fullname,diag);

            }
        });*/

        if (i % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            view.setBackgroundColor(Color.parseColor("#f2efef"));
        }
        return view;
    }
    ////open activity
    private void openDetailActivity(String...details)
    {
        Intent i=new Intent(c,DetailActivity.class);
        i.putExtra("NAME_KEY",details[0]);
        i.putExtra("EMAIL_KEY",details[1]);
        i.putExtra("ID",details[2]);
        c.startActivity(i);

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appointmentLists.clear();
        if (charText.length() == 0) {
            appointmentLists.addAll(searchlist);
        }
        else
        {
            for (AppointmentList wp : searchlist) {
                String full_nm = wp.getFirstname()+" "+wp.getMiddlename()+" "+wp.getLastname();
                if (full_nm.toLowerCase(Locale.getDefault()).contains(charText)) {
                    appointmentLists.add(wp);
                }else if (wp.getDiag().toLowerCase(Locale.getDefault()).contains(charText)) {
                    appointmentLists.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    class Delete_appointment extends AsyncTask< String, String, String>{
        ProgressDialog pDialog;
        String app;
        android.app.AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(c).create();
            pDialog = new ProgressDialog(c);
            alertDialog.setTitle("Delete Appointment");
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String Medical="";
            String type = params[0];
            String login_url;
            url_link link = new url_link();
            login_url = link.getUrl_link(type);
            if (type.equals("deleteappointment")) {
                try {
                    String token = params[1];
                    String id = params[2];
                    app=id;
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                            + "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonObj = null, jo;
            JSONObject jsonPatietnt = null;
            String reason = "";
            JSONArray ja = null;
            int status = 100;
            try {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject forgetpassword = jObj.getJSONObject("appointment");
                status = forgetpassword.getInt("status");
                reason = forgetpassword.getString("reason");
                if(status==-1){
                    try {
                        session.Relogin();
                        HashMap<String, String> user = session.getUserDetails();
                        String new_token = user.get(UserSessionManager.KEY_TOKEN);
                        new Delete_appointment().execute("deleteappointment",new_token,app);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            pDialog.dismiss();
            alertDialog.setMessage(reason);
            notifyDataSetChanged();
            alertDialog.show();
        }
    }

}
