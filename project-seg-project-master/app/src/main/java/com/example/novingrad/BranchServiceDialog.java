package com.example.novingrad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class BranchServiceDialog extends AppCompatDialogFragment {
    private BranchDialogListener listener;
    private Button ratingButton, serviceReqButton, requestSubmitButton;
    private RatingBar ratingBar;
    private Button submitRating;
    private TextView branchTitle, requestText;
    private EditText docs;
    private View tempV;


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.resource_dialog, null);

        //initialize dialog box
        builder.setView(view).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        final AlertDialog dialog = builder.create();

        //initialize layout widgets here (buttons, edittext fields, etc...)
        branchTitle = view.findViewById(R.id.branchTitle);
        ratingButton = view.findViewById(R.id.ratingButton);
        serviceReqButton = view.findViewById(R.id.requestButton);
        requestSubmitButton = view.findViewById(R.id.submitRequest);
        docs = view.findViewById(R.id.docField);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitRating = view.findViewById(R.id.submitRating);
        ratingBar.setVisibility(View.INVISIBLE);
        docs.setVisibility(View.INVISIBLE);
        requestSubmitButton.setVisibility(View.INVISIBLE);

        ratingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button b = (Button)v;
                if(b.getText().equals("Cancel")){
                    b.setText("Make Service Request");
                    ratingBar.setVisibility(View.INVISIBLE);
                    submitRating.setVisibility(View.INVISIBLE);
                    return;
                }
                b.setText("Cancel");
                ratingBar.setVisibility(View.VISIBLE);
                submitRating.setVisibility(View.VISIBLE);

            }
        });
        serviceReqButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button b = (Button)v;
                if(b.getText().equals("Cancel")){
                    b.setText("Make Service Request");
                    requestSubmitButton.setVisibility(View.INVISIBLE);
                    docs.setVisibility(View.INVISIBLE);
                    return;
                }
                b.setText("Cancel");
                requestSubmitButton.setVisibility(View.VISIBLE);
                docs.setVisibility(View.VISIBLE);
            }
        });
        submitRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        tempV = view;

        requestSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                docs.setError(null);
                if(docs.getText().length() == 0){
                    docs.setError("required");
                }
            }
        });


        dialog.show();
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (BranchServiceDialog.BranchDialogListener) context;
    }

    public interface BranchDialogListener{
        void closeRequest(String doc);
    }
}
