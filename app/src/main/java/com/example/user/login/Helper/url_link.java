package com.example.user.login.Helper;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.app.Activity;

import com.example.user.login.HomeActivity;
/**
 * Created by user on 14/07/2017.
 */

public class url_link {
    private Context context;
    public static String url_link = "http://192.168.1.226:8099/cms/api/";



    public String getUrl_link(String type){
        return url_link+type+".php";
    }

    public void toHome(View view){

    }



}
