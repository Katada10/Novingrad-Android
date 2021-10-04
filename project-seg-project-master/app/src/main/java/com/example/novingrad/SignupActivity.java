package com.example.novingrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    //Inputs for registration
    EditText name, username, password, email, address;
    Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);





        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);

        role = (Spinner) findViewById(R.id.role_spinner);


        ArrayList<String> roles = new ArrayList<String>();

        roles.add("customer");
        roles.add("branch");

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_item, roles);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        role.setAdapter(myAdapter);
    }

    /**
     * A lot of this code was copied from the validation in MainActivity
     * TODO: Make sure usernames are unique
     * @param v
     */
    public void Validate(View v){
        String nametxt = name.getText().toString();
        String emailtxt = email.getText().toString();
        String passwordtxt = password.getText().toString();
        String usernametxt = username.getText().toString();
        final String addressText = address.getText().toString();
        String roletxt = role.getSelectedItem().toString();

        String emailChars = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailChars);

        if (nametxt.length() == 0 || usernametxt.length() == 0 || emailtxt.length() == 0 || passwordtxt.length() == 0
        || addressText.length() == 0) {
            String text = "Please put in user info";
            username.setText("");
            name.setText("");
            email.setText("");
            password.setText("");
            Toast.makeText(SignupActivity.this, text, Toast.LENGTH_SHORT).show();
        } else if (pattern.matcher(emailtxt).matches() == false || usernametxt.matches("[A-Za-z0-9]+") == false) {
            String text = "Invalid";
            username.setText("");
            name.setText("");
            email.setText("");
            password.setText("");
            address.setText("");
            Toast.makeText(SignupActivity.this, text, Toast.LENGTH_SHORT).show();
        } else {

            //At this point there are no input errors, we can add the user to our database

            //Register info to database
            if (roletxt == "branch") {
                addBranch(nametxt, usernametxt, emailtxt, passwordtxt, addressText);

            } else {
                addUser(nametxt, usernametxt, emailtxt, passwordtxt);
            }
            username.setText("");
            name.setText("");
            email.setText("");
            password.setText("");

            BackToLogin();
        }
    }


    /**
     * This method takes the input and creates a user object then uploads it to the database
     * The key for each user is their username
     * @param name
     * @param username
     * @param email
     * @param password
     */
    public void addUser(String name, String username, String email, String password){

        //It will check whether a branch or customer is signing up and add them to the corresponding
        //subsection

            String id = MainActivity.db.getReference("customers").push().getKey();

            Customer user = new Customer(username, email, password, name, id);
            MainActivity.ref.child("customers").child(id).setValue(user);




    }

    /**
     * This method takes the input and creates a user object then uploads it to the database
     * The key for each user is their username
     * @param name
     * @param username
     * @param email
     * @param password
     */
    public void addBranch(String name, String username, String email, String password, String address){

        //It will check whether a branch or customer is signing up and add them to the corresponding
        //subsection

            String id = MainActivity.db.getReference("branches").push().getKey();
            Branch user = new Branch(username, email, password, name, id, address);
            MainActivity.ref.child("branches").child(id).setValue(user);
    }

    public void BackToLogin(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}