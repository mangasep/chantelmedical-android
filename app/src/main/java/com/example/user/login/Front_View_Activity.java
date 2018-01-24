package com.example.user.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class Front_View_Activity extends Activity {

    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front__view_);

        login = (Button) findViewById(R.id.b_login);
        register = (Button) findViewById(R.id.b_register);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent login = new Intent(Front_View_Activity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent register = new Intent(Front_View_Activity.this, RegisterActivity.class);
                startActivity(register);
            }
        });


    }
    }
