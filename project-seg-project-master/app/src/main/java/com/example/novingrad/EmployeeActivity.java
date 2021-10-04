package com.example.novingrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class EmployeeActivity extends AppCompatActivity implements ServiceHours.ServiceHoursListener {

    EditText serviceType;
    EditText requiredDocs;
    TextView rating;
    HashMap<String, ArrayList<String>> hours;
    //Account deletion form components

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        hours = new HashMap<>();
        Branch current = (Branch) MainActivity.username.getTag();
        //Populate the services table


        final TableLayout tableSer = (TableLayout) findViewById(R.id.tblSer);

        serviceType = (EditText) findViewById(R.id.serviceType);
        requiredDocs = (EditText) findViewById(R.id.requiredDocuments);
        rating = (TextView) findViewById(R.id.rating);

        rating.setText("Branch rating: " + current.getRating());



        //Create a header row to label the data
        TableRow row = new TableRow(EmployeeActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);

        row.setLayoutParams(lp);

        //Service and documents labels
        TextView typeLabel = new TextView(EmployeeActivity.this);
        typeLabel.setText("Service Type");
        typeLabel.setPadding(50, 0, 0, 0);


        TextView documentsLabel = new TextView(EmployeeActivity.this);
        documentsLabel.setText("Required Documents");
        documentsLabel.setPadding(50, 0, 0, 0);

        TextView stateLabel = new TextView(EmployeeActivity.this);
        stateLabel.setText("Offered By Branch");

        //Add the columns for type and requirements
        row.addView(typeLabel);
        row.addView(documentsLabel);
        row.addView(stateLabel);

        //Add header row to table
        tableSer.addView(row, 0);

        //References to "Services" section of database
        DatabaseReference ref = MainActivity.ref.child("services");

        String id = MainActivity.db.getReference("branches").push().getKey();

        //Listener is triggered once on page load
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int counter = 1;
                for (DataSnapshot entry :
                        snapshot.getChildren()) {
                    try {
                        getData(
                                entry, counter, tableSer
                        );
                    } catch (Exception e) {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Same as adminactivity
     *
     * @param entry
     * @param counter
     * @param tableSer
     */
    private void getData(DataSnapshot entry, int counter, TableLayout tableSer) {

        Service service = entry.getValue(Service.class);
        Branch current = (Branch) MainActivity.username.getTag();


        TableRow row = new TableRow(EmployeeActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);
        row.setLayoutParams(lp);
        row.setClickable(true);

        TextView type = new TextView(EmployeeActivity.this);
        type.setText(service.getServiceType());
        type.setPadding(50, 0, 0, 0);


        TextView docs = new TextView(EmployeeActivity.this);
        docs.setText(service.getRequiredDocuments());


        TextView state = new TextView(EmployeeActivity.this);

        state.setText("No");
        if (current.getServices() == null) {
            state.setText("No");
        } else {
            for (Map.Entry entr : current.getServices().entrySet()) {
                Service toCompare = (Service) entr.getValue();
                if (toCompare.getServiceType().equals(service.getServiceType())) {
                    state.setText("Yes");
                    break;
                }
            }
        }

        row.addView(type);
        row.addView(docs);
        row.addView(state);

        tableSer.addView(row, counter);


        //Set tag service so we can retreive the data of this row when it is clicked
        row.setTag(service);

        //add listener for row click
        row.setOnClickListener(OnRowClick);
        counter++;
    }


    private View.OnClickListener OnRowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Service service = (Service) v.getTag();

//The tag is the service which the row contains
//Sets input fields text to service values so they can be edited
            serviceType.setText(service.getServiceType());
            requiredDocs.setText(service.getRequiredDocuments());

        }
    };


    /**
     * Deletes specified service from branch services and updates branch object in database
     *
     * @param v
     */
    public void OnDelete(View v) {
        Branch branch = (Branch) MainActivity.username.getTag();

        branch.deleteService(serviceType.getText().toString());


        //TODO: Update database
        Toast.makeText(EmployeeActivity.this, "Successfully deleted " + serviceType.getText().toString(), Toast.LENGTH_SHORT).show();
        MainActivity.ref.child("branches").child(branch.getId()).setValue(branch);

    }


    /**
     * Adds specified service to branch and updates branch object
     *
     * @param v
     */
    public void OnAddEdit(View v) {

        boolean isValid = true;

        //intialize the error of each entery to null for next operations
        serviceType.setError(null);
        requiredDocs.setError(null);

        //Get the serviceType and requiredDocs texts to strings
        String sName = serviceType.getText().toString();
        String sDocs = requiredDocs.getText().toString();

        //If the entry is empty set isValid to false
        if (sName.length() == 0) {
            serviceType.setError("Required");
            isValid = false;
        }

        //If the entry is empty set isValid to false
        if (sDocs.length() == 0) {
            requiredDocs.setError("Required");
            isValid = false;
        }

        //If isValid is false --> do not crash
        if (!isValid) {return;}

        else {

            //Just add a service, then update accordingly
            Branch branch = (Branch) MainActivity.username.getTag();
            branch.addService(serviceType.getText().toString(), requiredDocs.getText().toString());

            Toast.makeText(EmployeeActivity.this, "Successfully added " + serviceType.getText().toString(), Toast.LENGTH_SHORT).show();
            //TODO: Update database

            MainActivity.ref.child("branches").child(branch.getId()).setValue(branch);

        }
    }

     public void setHours(View v){
         ServiceHours sHours = new ServiceHours();
         sHours.show(getSupportFragmentManager(), "Here");
     }

    @Override
    public void applyHours(ArrayList<ArrayList<EditText>> hourSets) {
         if(hours.size() > 0) hours = new HashMap<>();

         String[] daysOfWeek = new String[]{"Monday",
                 "Tuesday","Wednesday","Thursday",
                 "Friday","Saturday", "Sunday"};

         for(int i = 0;i<hourSets.size();++i){
             if(hourSets.get(i).get(0).getText().toString().length() == 0){
                 ArrayList<String> tmp = new ArrayList<>();
                 tmp.add("Closed");
                 tmp.add("Closed");
                 hours.put(daysOfWeek[i], tmp);
                 continue;
             }

             ArrayList<String> tmp = new ArrayList<>();
             String start = hourSets.get(i).get(0).getText().toString();
             String end = hourSets.get(i).get(1).getText().toString();
             tmp.add(start);
             tmp.add(end);

             hours.put(daysOfWeek[i], tmp);
         }
         //After applying hours, add them to the branch and push to db
        Branch b = (Branch)MainActivity.username.getTag();

        b.updateHours(hours);

        MainActivity.ref.child("branches").child(b.getId()).setValue(b);
    }

    public void closeDefaultHours(){
        String[] daysOfWeek = new String[]{"Monday",
                "Tuesday","Wednesday","Thursday",
                "Friday","Saturday", "Sunday"};

        for(int i = 0;i<7;++i){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add("Closed");
            tmp.add("Closed");
            hours.put(daysOfWeek[i], tmp);
        }
    }
}
