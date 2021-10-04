package com.example.novingrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView welcomeScreen;
        Button nextBtn = (Button) findViewById(R.id.nextPage);

        welcomeScreen = (TextView) findViewById(R.id.welcome);

        //Slight changes accessing the current user through the tag
        User user =(User) MainActivity.username.getTag();
        welcomeScreen.setText("Welcome "+ user.getName()+"! You are logged in as "+ user.getType()+".") ;


        switch(user.getType()){
            case "administrator":
                //Admin Screen
                type = "admin";
                nextBtn.setText("Go to Admin Page");
                break;

            case "branch":
                //Employee Screen
                type= "branch";
                nextBtn.setText("Go to Branch Page");
                break;

            case "customer":
                //Customer screen
                type = "customer";
                nextBtn.setText("Go to Customer Page");
                break;
        }


    }

    public void OnNextBtnClick(View V){

        Intent intent;
        switch(type){
            case "admin":
                intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                break;

            case "branch":
                intent = new Intent(this, EmployeeActivity.class);
                startActivity(intent);
                break;
            case "customer":
                intent = new Intent(this, CustomerActivity.class);
                startActivity(intent);
                break;
        }
    }
}