package com.example.user.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.user.login.Helper.DatabaseHandler;
import com.example.user.login.Helper.UserSessionManager;
import com.example.user.login.fragment.AccountFragment;
import com.example.user.login.fragment.HomeFragment;

import java.util.HashMap;


public class HomeActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener{

    AHBottomNavigation bottomNavigation;
    UserSessionManager session;
    DatabaseHandler db = new DatabaseHandler(this);
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());
        bottomNavigation= (AHBottomNavigation) findViewById(R.id.myBottomNavigation_ID);
        bottomNavigation.setOnTabSelectedListener(this);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        this.createNavItems();
        if(session.checkLogin())
            finish();
        if (!session.checkLogin()){
            HashMap<String, String> user = session.getUserDetails();
            token = user.get(UserSessionManager.KEY_TOKEN);
        }else{
            session.logoutUser();
        }
    }

    private void createNavItems()
    {    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(UserSessionManager.KEY_TOKEN);
        //CREATE ITEMS
        AHBottomNavigationItem crimeItem=new AHBottomNavigationItem("Appointment",R.drawable.icon_appoimnet_152px);
        AHBottomNavigationItem docsItem=new AHBottomNavigationItem("My Account",R.drawable.icon_myaccount_152px);


        //ADD ITEMS TO BAR
        bottomNavigation.addItem(crimeItem);
        bottomNavigation.addItem(docsItem);

        //PROPERTIES
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#379ee9"));
        bottomNavigation.setAccentColor(Color.parseColor("#ffffff"));
        bottomNavigation.setInactiveColor(Color.parseColor("#3a4147"));

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setCurrentItem(0);

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if(position==0)
        {
            HomeFragment homeFragmente=new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id,homeFragmente).commit();
        }else if(position==1)
        {
            AccountFragment documentaryFragment=new AccountFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id,documentaryFragment).commit();
        }
        return true;
    }

    public  String getToken (){
        return token;
    }
    public Context getContext(){
        return HomeActivity.this;
    }
}
