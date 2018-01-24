package com.example.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.login.Helper.DatabaseHandler;
import com.example.user.login.Helper.UserSessionManager;
import com.example.user.login.Helper.url_link;
import com.example.user.login.model.Token;

import net.sf.json.JSONArray;

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
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity{

    TextView nameTxt,emailTxt, usernameTxt;
    CardView Medi,Immun,Prescription;
    ImageButton btnclickme, btnback;
    String token="";
    private DatabaseHandler db;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        session = new UserSessionManager(getApplicationContext());
        db = new DatabaseHandler(this);
        nameTxt = (TextView) findViewById(R.id.nameDetailTxt);
        emailTxt= (TextView) findViewById(R.id.emailDetailTxt);
        Medi = (CardView) findViewById(R.id.Medi_lay);
        Immun = (CardView) findViewById(R.id.Immun_lay);
        Prescription = (CardView) findViewById(R.id.Presc_lay);
        if(session.checkLogin())
            finish();
        if (!session.checkLogin()){
            HashMap<String, String> user = session.getUserDetails();
            token = user.get(UserSessionManager.KEY_TOKEN);
        }else{
            session.logoutUser();
        }

        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        btnclickme = (ImageButton) findViewById(R.id.more);
        btnclickme.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create the instance of Menu
                PopupMenu popup = new PopupMenu(DetailActivity.this, btnclickme);
                // Inflating menu using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                // registering OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(DetailActivity.this, "Kamu telah memilih : " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });
        //usernameTxt = (TextView) findViewById(R.id.usernameDetailTxt);

        //GET INTENT
        Intent i=this.getIntent();

        //RECEIVE DATA
        String name=i.getExtras().getString("NAME_KEY");
        String email=i.getExtras().getString("EMAIL_KEY");
        final String id=i.getExtras().getString("ID");

        //BIND DATA
        nameTxt.setText(name);
        emailTxt.setText(email);
        //  usernameTxt.setText(username);

        //medication onClick
        Medi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_medication,null);
                //final EditText mType = (EditText) mView.findViewById(R.id.etType);
                final EditText mTitle = (EditText) mView.findViewById(R.id.etTitle);
                final EditText mOccurence = (EditText) mView.findViewById(R.id.etOccurence);
                final EditText mClassification = (EditText) mView.findViewById(R.id.etClassification);
                final EditText mRefferedby = (EditText) mView.findViewById(R.id.etReferredby);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCancelable(false);
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mtype = "Medication";
                        String title = mTitle.getText().toString().toLowerCase();
                        String occurence = mOccurence.getText().toString().toLowerCase();
                        String classification = mClassification.getText().toString().toLowerCase();
                        String refferedby = mRefferedby.getText().toString().toLowerCase();
                        String type = "addlist";
                        new Masuk().execute(type,token,mtype,title,occurence,classification,refferedby,id);
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //immunization onClick
        Immun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_immunization,null);
                final EditText mType = (EditText) mView.findViewById(R.id.etType);
                final EditText mTitle = (EditText) mView.findViewById(R.id.etTitle);
                final EditText mOccurence = (EditText) mView.findViewById(R.id.etOccurence);
                final EditText mClassification = (EditText) mView.findViewById(R.id.etClassification);
                final EditText mRefferedby = (EditText) mView.findViewById(R.id.etReferredby);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCancelable(false);
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mtype = mType.getText().toString().toLowerCase();
                        String title = mTitle.getText().toString().toLowerCase();
                        String occurence = mOccurence.getText().toString().toLowerCase();
                        String classification = mClassification.getText().toString().toLowerCase();
                        String refferedby = mRefferedby.getText().toString().toLowerCase();
                        String type = "addlist";
                        new Masuk1().execute(type,token,mtype,title,occurence,classification,refferedby,id);
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //Prescription onClick
        Prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_prescription, null);
                final EditText mStartdate = (EditText) mView.findViewById(R.id.etStartdate);
                final EditText mDrug = (EditText) mView.findViewById(R.id.etDrug);
                final EditText mDosage = (EditText) mView.findViewById(R.id.etDosage);
                final EditText mQuantity = (EditText) mView.findViewById(R.id.etQuantity);
                final EditText mRefill = (EditText) mView.findViewById(R.id.etRefill);
                final EditText mMedication = (EditText) mView.findViewById(R.id.etMedication);
                final EditText mNote = (EditText) mView.findViewById(R.id.etNote);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCancelable(false);
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String startDate = mStartdate.getText().toString().toLowerCase();
                        String drug = mDrug.getText().toString().toLowerCase();
                        String dossage = mDosage.getText().toString().toLowerCase();
                        String quantity = mQuantity.getText().toString().toLowerCase();
                        String refill = mRefill.getText().toString().toLowerCase();
                        String medication = mMedication.getText().toString().toLowerCase();
                        String note = mNote.getText().toString().toLowerCase();
                        String type = "addprescription";
                        new Masuk2().execute(type,token,id,startDate,drug,dossage,quantity,refill,medication,note);
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //masuk medication
    public class Masuk extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        android.app.AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String addlist_url;
            url_link link = new url_link();
            addlist_url = link.getUrl_link(type);
            if (type.equals("addlist")) {
                try {
                    String token = params[1];
                    String mtype = params[2];
                    String title = params[3];
                    String occurance = params[4];
                    String classification = params[5];
                    String referredby = params[6];
                    String id = params[7];
                    URL url = new URL(addlist_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                            + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(mtype, "UTF-8")
                            + "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8")
                            + "&" + URLEncoder.encode("occurance", "UTF-8") + "=" + URLEncoder.encode(occurance, "UTF-8")
                            + "&" + URLEncoder.encode("classification", "UTF-8") + "=" + URLEncoder.encode(classification, "UTF-8")
                            + "&" + URLEncoder.encode("referredby", "UTF-8") + "=" + URLEncoder.encode(referredby, "UTF-8")
                            + "&" + URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
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
            alertDialog = new android.app.AlertDialog.Builder(DetailActivity.this).create();
            alertDialog.setTitle("Add Medication");
            pDialog = new ProgressDialog(DetailActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonObj;
            String reason = "";
            int status = 100;
            try {
                jsonObj = XML.toJSONObject(result);
                JSONObject jObj = new JSONObject(jsonObj.toString());
                JSONObject forgetpassword = jObj.getJSONObject("list");
                status = forgetpassword.getInt("status");
                reason = forgetpassword.getString("reason");
            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            pDialog.dismiss();
            alertDialog.setMessage(reason);
            //notifyDataSetChanged();
            alertDialog.show();
        }
    }

    //masuk immunization
    public class Masuk1 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        android.app.AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String addlist_url;
            url_link link = new url_link();
            addlist_url = link.getUrl_link(type);
            if (type.equals("addlist")) {
                try {
                    String token = params[1];
                    String mtype = params[2];
                    String title = params[3];
                    String occurance = params[4];
                    String classification = params[5];
                    String referredby = params[6];
                    String id = params[7];
                    URL url = new URL(addlist_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                            + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(mtype, "UTF-8")
                            + "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8")
                            + "&" + URLEncoder.encode("occurance", "UTF-8") + "=" + URLEncoder.encode(occurance, "UTF-8")
                            + "&" + URLEncoder.encode("classification", "UTF-8") + "=" + URLEncoder.encode(classification, "UTF-8")
                            + "&" + URLEncoder.encode("referredby", "UTF-8") + "=" + URLEncoder.encode(referredby, "UTF-8")
                            + "&" + URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
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
            alertDialog = new android.app.AlertDialog.Builder(DetailActivity.this).create();
            alertDialog.setTitle("Add Immunization");
            pDialog = new ProgressDialog(DetailActivity.this);
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
                JSONObject list = jObj.getJSONObject("list");
                status = list.getInt("status");
                reason = list.getString("reason");
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
                        Intent Home = new Intent(DetailActivity.this, LoginActivity.class);
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

    //masuk prescription
    public class Masuk2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        android.app.AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String addprescription_url;
            url_link link = new url_link();
            addprescription_url = link.getUrl_link(type);
            if (type.equals("addprescription")) {
                try {
                    String token = params[1];
                    String id = params[2];
                    String startDate = params[3];
                    String drug = params[4];
                    String dossage = params[4];
                    String quantity = params[5];
                    String refill = params[6];
                    String medication = params[7];
                    String note = params[8];

                    URL url = new URL(addprescription_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                            + "&" + URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                            + "&" + URLEncoder.encode("startDate", "UTF-8") + "=" + URLEncoder.encode(startDate, "UTF-8")
                            + "&" + URLEncoder.encode("drug", "UTF-8") + "=" + URLEncoder.encode(drug, "UTF-8")
                            + "&" + URLEncoder.encode("dossage", "UTF-8") + "=" + URLEncoder.encode(dossage, "UTF-8")
                            + "&" + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8")
                            + "&" + URLEncoder.encode("refill", "UTF-8") + "=" + URLEncoder.encode(refill, "UTF-8")
                            + "&" + URLEncoder.encode("medication", "UTF-8") + "=" + URLEncoder.encode(medication, "UTF-8")
                            + "&" + URLEncoder.encode("note", "UTF-8") + "=" + URLEncoder.encode(note, "UTF-8")
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
            alertDialog = new android.app.AlertDialog.Builder(DetailActivity.this).create();
            alertDialog.setTitle("Add Prescription");
            pDialog = new ProgressDialog(DetailActivity.this);
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
                JSONObject list = jObj.getJSONObject("prescription");
                status = list.getInt("status");
                reason = list.getString("reason");
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
                        Intent Home = new Intent(DetailActivity.this, LoginActivity.class);
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
}