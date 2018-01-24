package com.example.user.login.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.login.Helper.UserSessionManager;
import com.example.user.login.InfoUser;
import com.example.user.login.LoginActivity;
import com.example.user.login.PolicyActivity;
import com.example.user.login.R;
import com.example.user.login.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private Button logout,register,signin,about_us,policy,info;
    UserSessionManager session;
    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        logout = (Button)view.findViewById(R.id.logout);
        session = new UserSessionManager(getContext());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = Logout();
                diaBox.show();
            }
        });

        signin = (Button)view.findViewById(R.id.b_login);
        signin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent signin;
                signin = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(signin);
            }
        });

        register = (Button)view.findViewById(R.id.b_register);
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent register;
                register = new Intent(getActivity(), RegisterActivity.class);
                getActivity().startActivity(register);
            }
        });

        info = (Button)view.findViewById(R.id.user);
        info.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent info;
                info = new Intent(getActivity(), InfoUser.class);
                getActivity().startActivity(info);
            }
        });

        about_us = (Button)view.findViewById(R.id.about_us);
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

        policy = (Button) view.findViewById(R.id.policy);
        policy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent info;
                info = new Intent(getActivity(), PolicyActivity.class);
                getActivity().startActivity(info);
            }
        });
        return view;
    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("About Us")
                .setMessage("Chanthel Medical System Versi 1 merupakan apikasi mobile yang ditujukan untuk membantu kerja dari dokter."+
                        " Aplikasi ini berfungsi sebagai medical record data pasien serta mempermudah dalam pembuatan janji antara pasien dengan dokter.")
                .setIcon(R.drawable.ic_launcher)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                    }

                })
                .create();
        return myQuittingDialogBox;

    }

    private AlertDialog Logout()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Log Out Aplication")
                .setMessage("Are you sure want to log out from your account?")
                .setIcon(R.drawable.ic_launcher)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        session.logoutUser();
                        getActivity().finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
