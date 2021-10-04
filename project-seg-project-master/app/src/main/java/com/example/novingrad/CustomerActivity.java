package com.example.novingrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CustomerActivity extends AppCompatActivity implements BranchServiceDialog.BranchDialogListener{
    EditText queryEditText;
    String searchText;
    Spinner searchParam;
    DatabaseReference ref;
    private String tempName;

    private List<Branch> searchResults;
    static List<Branch> searchResultsCopy;
    private HashMap<String, ArrayList<String>> hoursDummy;

    private  TableLayout tableSer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        searchParam = findViewById(R.id.spinnerSearch);
        queryEditText = findViewById(R.id.searchBar);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.searchParams));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        searchParam.setAdapter(myAdapter);

       tableSer = (TableLayout) findViewById(R.id.tblSer);

        searchResults = new ArrayList<>();
        hoursDummy = new HashMap<>();
        searchText = queryEditText.getText().toString();

        TableRow row = new TableRow(CustomerActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);

        row.setLayoutParams(lp);

        //Service and documents labels
        TextView branchLabel = new TextView(CustomerActivity.this);
        branchLabel.setText("Branch Name");
        branchLabel.setPadding(50, 0, 0, 0);


        //Add the columns for type and requirements
        row.addView(branchLabel);


        //Add header row to table
        tableSer.addView(row, 0);
    }

    public void onSearchClick(View v){
        //validate entered query
        queryEditText.setError(null);
        if(queryEditText.length() == 0){
            queryEditText.setError("Cannot be empty");
            return;
        }
        searchText = queryEditText.getText().toString();

        ref = MainActivity.db.getReference().child("branches");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot entry :
                        snapshot.getChildren()) {

                     Branch branch = entry.getValue(Branch.class);

                        //Find the branch that matches the given search parameter
                        //I.e switch(spinnertype)
                        //find if (spinnerSearchParam in db) == searchText

                        String selectedText = searchParam.getSelectedItem().toString();
                        if(selectedText.equalsIgnoreCase("address")){

                            if(branch.getAddress()!= null && branch.getAddress().equalsIgnoreCase(searchText) && !searchResults.contains(branch)){
                                searchResults.add(branch);

                                TableRow row = new TableRow(CustomerActivity.this);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);
                                row.setLayoutParams(lp);
                                row.setClickable(true);
                                row.setTag(branch.getName());
                                row.setOnClickListener(OnServiceClick);


                                TextView address = new TextView(CustomerActivity.this);
                                address.setText(branch.getName());
                                address.setPadding(50, 0, 0, 0);


                                row.addView(address);

                                tableSer.addView(row);
                            }

                        }else if(selectedText.equals("hours")){

                            //COMPARE HOURS
                            //TODO: ADJUST ACCORDING TO UI DESIGN
                           //NOT IMPLEMENTED DUE TO TIME CONSTRAINT


                        }else if(selectedText.equalsIgnoreCase("serviceType")){
                            //Then it equals servicetype
                                if(branch.getServices()!= null && branch.getServices().containsKey(searchText) && !searchResults.contains(branch)){
                                        searchResults.add(branch);

                                        TableRow row = new TableRow(CustomerActivity.this);
                                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, WRAP_CONTENT, 0.5f);
                                        row.setLayoutParams(lp);
                                        row.setClickable(true);
                                        row.setTag(branch.getName());
                                        row.setOnClickListener(OnServiceClick);


                                        TextView address = new TextView(CustomerActivity.this);
                                        address.setText(branch.getName());
                                        address.setPadding(50, 0, 0, 0);


                                        row.addView(address);

                                        tableSer.addView(row);
                                }
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private View.OnClickListener OnServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                String b = (String)v.getTag();
                tempName = b;
                BranchServiceDialog serviceDialog = new BranchServiceDialog();
                serviceDialog.show(getSupportFragmentManager(), "Here");
            }catch(Exception e){queryEditText.setText(e.getMessage());}
        }
    };

    @Override
    public void closeRequest(String doc){
        final String d = doc;
        Query query = MainActivity.ref.child("branches").orderByChild("name").equalTo(tempName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    dataSnapshot.getRef().setValue("Service Request docs: " + d);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    // added for testing
    public static List getList(){
        // replicating initial creation of searchResults
        searchResultsCopy = new ArrayList<>();
        return searchResultsCopy;
    }

    public static int testingServiceSearchResults1() {
        // replicating what happens when you actually search by serviceType in app
        searchResultsCopy = new ArrayList<>();
        Branch branch = new Branch("username", "email", "password", "name", "id", "address");
        branch.addService("this", "this");
        String searchText = "this";
        if (branch.getServices().containsKey(searchText)) {
            CustomerActivity.searchResultsCopy.add(branch);
        }
        return searchResultsCopy.size();
    }

    public static String testingServiceSearchResults2() {
        // replicating what happens when you actually search by serviceType in app
        searchResultsCopy = new ArrayList<>();
        Branch branch = new Branch("username", "email", "password", "name", "id", "address");
        branch.addService("this", "this");
        String searchText = "this";
        if (branch.getServices().containsKey(searchText)) {
            CustomerActivity.searchResultsCopy.add(branch);
        }
        return searchResultsCopy.get(0).getService("this").getServiceType();
    }

    public static int testingAddressSearchResults1() {
        // replicating what happens when you actually search by address in app
        searchResultsCopy = new ArrayList<>();
        Branch branch = new Branch("username", "email", "password", "name", "id", "address");
        branch.addService("this", "this");
        String searchText = "address";
        if (branch.getAddress().equals(searchText)) {
            CustomerActivity.searchResultsCopy.add(branch);
        }
        return searchResultsCopy.size();
    }

    public static String testingAddressSearchResults2() {
        // replicating what happens when you actually search by address in app
        searchResultsCopy = new ArrayList<>();
        Branch branch = new Branch("username", "email", "password", "name", "id", "address");
        branch.addService("this", "this");
        String searchText = "";
        if (branch.getAddress().equals(searchText)) {
            CustomerActivity.searchResultsCopy.add(branch);
        }
        return searchResultsCopy.get(0).getAddress();
    }
}

