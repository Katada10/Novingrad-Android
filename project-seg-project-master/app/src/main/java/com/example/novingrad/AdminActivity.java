package com.example.novingrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AdminActivity extends AppCompatActivity {


    //Variables that refer to service type and required document input fields
    EditText serviceType;
    EditText requiredDocs;
    //Account deletion form components
    EditText delUsername;
    Button submitAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        //Populate the services table

        //References to "Services" section of database
        DatabaseReference ref = MainActivity.ref.child("services");
        final TableLayout tableSer = (TableLayout) findViewById(R.id.tblSer);

        serviceType = (EditText) findViewById(R.id.serviceType);
        requiredDocs = (EditText) findViewById(R.id.requiredDocuments);

        delUsername = (EditText) findViewById(R.id.accToDel);
        submitAccount = (Button) findViewById(R.id.submitAccDel);
        delUsername.setVisibility(View.INVISIBLE);
        submitAccount.setVisibility(View.INVISIBLE);

        //Create a header row to label the data
        TableRow row = new TableRow(AdminActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);

        row.setLayoutParams(lp);

        //Service and documents labels
        TextView typeLabel = new TextView(AdminActivity.this);
        typeLabel.setText("Service Type");
        typeLabel.setPadding(50, 0, 0, 0);


        TextView documentsLabel = new TextView(AdminActivity.this);
        documentsLabel.setText("Required Documents");


        //Add the columns for type and requirements
        row.addView(typeLabel);
        row.addView(documentsLabel);


        //Add header row to table
        tableSer.addView(row, 0);

        //Listener is triggered once on page load
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 1;
                for (DataSnapshot entry:
                        snapshot.getChildren()) {

                    try{
                        getData(
                            entry, counter, tableSer
                        );
                    }catch(Exception e){continue;}

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Retreives data from database add inserts as a row into tableSer
     * counter is used for row id
     * entry is the entry from db
     * @param entry
     * @param counter
     * @param tableSer
     */
    private void getData(DataSnapshot entry, int counter, TableLayout tableSer){
        Service service = entry.getValue(Service.class);


        TableRow row = new TableRow(AdminActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);
        row.setLayoutParams(lp);
        row.setClickable(true);


        TextView type = new TextView(AdminActivity.this);
        type.setText(service.getServiceType());
        type.setPadding(50, 0, 0, 0);


        TextView docs = new TextView(AdminActivity.this);
        docs.setText(service.getRequiredDocuments());


        row.addView(type);
        row.addView(docs);

        tableSer.addView(row, counter);

        //Set tag service so we can retreive the data of this row when it is clicked
        row.setTag(service);


        //add listener for row click
        row.setOnClickListener(OnRowClick);
        counter++;
    }


    /**
     * Triggered when a row is clicked
     */
    private View.OnClickListener OnRowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Service service = (Service)  v.getTag();

            //The tag is the service which the row contains
            //Sets input fields text to service values so they can be edited
            serviceType.setText(service.getServiceType());
            requiredDocs.setText(service.getRequiredDocuments());

        }
    };


    /**
     * Listener for delete button removes selected row from database
     * @param v
     */
    public void OnDelete(View v){
        MainActivity.ref.child("services").child(serviceType.getText().toString()).removeValue();
    }


    /**
     * Listener for add/edit button
     * Adds or edits the data based on the input fields
     * @param v
     */
    public void OnAddEdit(View v){
        addService(serviceType.getText().toString(), requiredDocs.getText().toString());

    }


    /**
     * This method takes the input and creates a Service object then uploads it to the database
     * The key for each service is the serviceType
     *
     * @param serviceType
     * @param requiredDocuments
     */
    public void addService(String serviceType, String requiredDocuments){
       Service service = new Service(serviceType, requiredDocuments);

        MainActivity.ref.child("services").child(serviceType).setValue(service);
    }

    /**
     * Conditionally renders the account deletion form
     * associated with the DELETE AN ACCOUNT button.
     * @param v
     */
    public void accountsHandler(View v){
        Button accBtn = (Button)v;
        String buttonText = accBtn.getText().toString();

        if(buttonText.equals("CANCEL")){
            delUsername.setVisibility(View.INVISIBLE);
            submitAccount.setVisibility(View.INVISIBLE);
            accBtn.setText("DELETE AN ACCOUNT");
            return;
        }

        delUsername.setText(""); //reset username form
        delUsername.setVisibility(View.VISIBLE);
        submitAccount.setVisibility(View.VISIBLE);
        accBtn.setText("CANCEL");
    }

    public void submitDelRequest(View v){
        final String uName = delUsername.getText().toString();

        if(uName.length() == 0){
            Toast.makeText(AdminActivity.this,
                "Username required", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check branches entries
        Query query = MainActivity.ref.child("branches").orderByChild("username").equalTo(uName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
//                    MainActivity.ref.child("branches").child(uName).removeValue();
                    for(DataSnapshot user : dataSnapshot.getChildren()){
                        user.getRef().removeValue();
                    }
                    Toast.makeText(AdminActivity.this,
                            uName + " deleted", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Toast.makeText(AdminActivity.this,
                            "User not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        //check customers entries
        query = MainActivity.ref.child("customers").orderByChild("username").equalTo(uName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot user : dataSnapshot.getChildren()){
                        user.getRef().removeValue();
                    }
                    Toast.makeText(AdminActivity.this,
                            uName + " deleted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AdminActivity.this,
                            "User not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }    
}