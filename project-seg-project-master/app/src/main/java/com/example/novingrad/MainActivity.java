package com.example.novingrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Pattern;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Instance for db for the whole application, as well as a reference to it
    public static FirebaseDatabase db;
    public static DatabaseReference ref;

    //THIS MUST BE STATIC AS IT IS TAGGED WITH THE CURRENT USER FOR OTHER PAGES TO KNOW WHO IS LOGGED IN
    public static EditText username;

    Button login;
    EditText password;
    Spinner role;

    Administrator admin;

    public static Administrator adminCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        admin = new Administrator("user12345", "user12345@gmail.com", "user12345" );

        //The reference is at the root of the whole database, we can access individual childrent through it
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        login = (Button) findViewById(R.id.btn_login);

        username = (EditText) findViewById(R.id.usertxt);
        password = (EditText) findViewById(R.id.passwordtxt);

        role = (Spinner) findViewById(R.id.role_spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.roles));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        role.setAdapter(myAdapter);
    }

    public void OnLogin(View v){
        final String usernametxt = username.getText().toString();
        final String passwordtxt = password.getText().toString();
        final String roletxt = role.getSelectedItem().toString();

        String emailChars = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailChars);

        if(usernametxt.length()==0||passwordtxt.length()==0){
            String text ="Please put in user info";
            username.setText("");
            password.setText("");
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
        if(usernametxt.matches("[A-Za-z0-9]+")==false){
            String text ="Invalid";
            username.setText("");
            password.setText("");
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }

        //Validate against database here

        /**
         * This event listener lets us loop through the entries in the database to check against
         * the given inputs
         * TODO: Implement validation, e.g give error message, pass correct name to mainactivity2,
         */


        //Checks if admin, no need to go through loop as there is one admin
        if (usernametxt.equalsIgnoreCase(admin.getUsername()) && passwordtxt.equalsIgnoreCase(admin.getPassword()) && roletxt.equalsIgnoreCase(admin.getType())) {
            username.setTag(admin);
            openMainActivity2();
            Toast.makeText(MainActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
        }


        //String to tell which section of the database to search
        String subsection = "";
        if(roletxt.equalsIgnoreCase
                ("branch")){
            subsection = "branches";
        }
        else if(roletxt.equalsIgnoreCase("customer")){
            subsection = "customers";
        }

        //Loop to verify login info
        ref.child(subsection).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot entry:
                                    snapshot.getChildren()) {


                                //If it is a customer logging in, verify the login as a customer

                                if(roletxt.equalsIgnoreCase("customer")){
                                    Customer u = entry.getValue(Customer.class);
                                    if(u.getUsername().equalsIgnoreCase(usernametxt) && u.getPassword().equals(passwordtxt) && u.getType().equalsIgnoreCase(roletxt)) {


                                        //Tag the static variable username with Customer
                                        //It is split so that customer specific functionality can be accessed outside
                                        username.setTag(u);


                                        openMainActivity2();
                                        Toast.makeText(MainActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                else if(roletxt.equalsIgnoreCase("branch")){
                                    Branch u = entry.getValue(Branch.class);
                                    if(u.getUsername().equalsIgnoreCase(usernametxt) && u.getPassword().equals(passwordtxt) && u.getType().equalsIgnoreCase(roletxt)) {


                                        //Tag the static variable username with Customer
                                        //It is split so that branch specific functionality can be accessed outside
                                        username.setTag(u);


                                        openMainActivity2();
                                        Toast.makeText(MainActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }

        );

        username.setText("");

        password.setText("");
    }

    /**
     * Gets called when the create account link is pressed, moves to sign up page
     * @param v
     */
    public void Signup(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void openMainActivity2(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    //added for testing
    public static String getAdminUsername(){
        adminCopy = new Administrator("user12345", "user12345@gmail.com", "user12345");
        return adminCopy.getUsername();
    }


}
