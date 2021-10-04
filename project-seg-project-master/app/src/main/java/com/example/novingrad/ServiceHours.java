package com.example.novingrad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class ServiceHours extends AppCompatDialogFragment {
    private ArrayList<ArrayList<EditText>> hourSets; //start and end times for each day of the week
    private ServiceHoursListener listener;

    /**
    /**
     * constructor to initialize and display service hour dialog box, where
     * the user can specify operational hours of a service
     * @param savedInstanceState
     * @return dialog object
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.hours_dialog, null);
        //initialize array
        hourSets = new ArrayList<>();
        for(int i = 0;i<7;++i){
            hourSets.add(new ArrayList<EditText>());
        }
        hourSets.get(0).add((EditText)view.findViewById(R.id.mStart));
        hourSets.get(1).add((EditText)view.findViewById(R.id.tuStart));
        hourSets.get(2).add((EditText)view.findViewById(R.id.wStart));
        hourSets.get(3).add((EditText)view.findViewById(R.id.thStart));
        hourSets.get(4).add((EditText)view.findViewById(R.id.fStart));
        hourSets.get(5).add((EditText)view.findViewById(R.id.saStart));
        hourSets.get(6).add((EditText)view.findViewById(R.id.suStart));

        hourSets.get(0).add((EditText)view.findViewById(R.id.mEnd));
        hourSets.get(1).add((EditText)view.findViewById(R.id.tuEnd));
        hourSets.get(2).add((EditText)view.findViewById(R.id.wEnd));
        hourSets.get(3).add((EditText)view.findViewById(R.id.thEnd));
        hourSets.get(4).add((EditText)view.findViewById(R.id.fEnd));
        hourSets.get(5).add((EditText)view.findViewById(R.id.saEnd));
        hourSets.get(6).add((EditText)view.findViewById(R.id.suEnd));
//        hourSets[1][0] = (EditText)view.findViewById(R.id.tuStart);
//        hourSets[2][0] = (EditText)view.findViewById(R.id.wStart);
//        hourSets[3][0] = (EditText)view.findViewById(R.id.thStart);
//        hourSets[4][0] = (EditText)view.findViewById(R.id.fStart);
//        hourSets[5][0] = (EditText)view.findViewById(R.id.saStart);
//        hourSets[6][0] = (EditText)view.findViewById(R.id.suStart);

//        hourSets[0][1] = (EditText)view.findViewById(R.id.mEnd);
//        hourSets[1][1] = (EditText)view.findViewById(R.id.tuEnd);
//        hourSets[2][1] = (EditText)view.findViewById(R.id.wEnd);
//        hourSets[3][1] = (EditText)view.findViewById(R.id.thEnd);
//        hourSets[4][1] = (EditText)view.findViewById(R.id.fEnd);
//        hourSets[5][1] = (EditText)view.findViewById(R.id.saEnd);
//        hourSets[6][1] = (EditText)view.findViewById(R.id.suEnd);

        //initialize dialog box
        builder.setView(view).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("Done", null);

        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean isValidForm = validateFields();
                if(isValidForm){
                    listener.applyHours(hourSets);
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ServiceHoursListener) context;
    }

    public interface ServiceHoursListener{
        void applyHours(ArrayList<ArrayList<EditText>> hourSets);
    }

    /**
     * Runs the required validation methods to ensure all fields are populated with either
     * no data, or valid time spans.
     * @return true if form is valid, false otherwise
     */
    private boolean validateFields(){
        clearErrors();
        boolean isValid = true;

        for(int i = 0;i<hourSets.size();++i){
            //if missing data, handle cases
            if(hourSets.get(i).get(0).length() == 0 || hourSets.get(i).get(1).length() == 0){
                if(emptyFields(hourSets.get(i).get(0), hourSets.get(i).get(1))) isValid = false;
                continue;
            }

            //check for illegal characters
            if(!legalFormat(hourSets.get(i).get(0), hourSets.get(i).get(1))){
                isValid = false;
                continue;
            }

            //validate format
            if(!validFormat(hourSets.get(i).get(0), hourSets.get(i).get(1))){
                isValid = false;
                continue;
            }

            //ensure time spans are valid
            if(!validTimeSpans(hourSets.get(i).get(0), hourSets.get(i).get(1))) isValid = false;
        }
        return isValid;
    }

    /**
     * Checks if fields are left empty in a valid way. If both start and end times
     * are undefined, the working hours for that day will default to closed.
     * The entry is considered invalid if only one of the two entries are filled
     *
     * @param start start time EditText field
     * @param end end time EditText field
     * @return true if valid, else false
     */
    private boolean emptyFields(EditText start, EditText end){
        if(start.length() == 0 && end.length() == 0)
            return false;

        if(start.length() != 0)
            end.setError("Missing end time");
        else
            start.setError("Missing start time");

        return true;
    }

    /**
     * Checks if any illegal characters are present in an hour set
     * (i.e. non digit characters - excluding colons).
     *
     * @param startTime start time EditText field
     * @param endTime end time EditText field
     * @return true if valid, else false
     */
    private boolean legalFormat(EditText startTime, EditText endTime){
        boolean isValid = true;
        String start = startTime.getText().toString();
        String end = endTime.getText().toString();

        if(!start.trim().replaceAll(":","").matches("[0-9]+")){
            isValid = false;
            startTime.setError("Invalid time");
        }

        if(!end.trim().replaceAll(":","").matches("[0-9]+")){
            isValid = false;
            endTime.setError("Invalid time");
        }

        return isValid;
    }

    /**
     * Ensures time is valid format - one or no colons
     *
     * @param start start time EditText field
     * @param end end time EditText field
     * @return true if valid, else false
     */
    private boolean validFormat(EditText start, EditText end){
        boolean isValid = true;

        String temp = start.getText().toString();
        for(int i = 0, colCount = 0;i<temp.length();++i){
            if(temp.charAt(i) == ':') colCount++;
            if(colCount > 1){
                start.setError("Invalid format");
                isValid = false;
                break;
            }
        }

        temp = end.getText().toString();
        for(int i = 0, colCount = 0;i<temp.length();++i){
            if(temp.charAt(i) == ':') colCount++;
            if(colCount > 1){
                end.setError("Invalid format");
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    /**
     * Ensures time spans are possible and fall within 24-hour clock constraints
     *
     * @param start start time EditText field
     * @param end end time EditText field
     * @return true if valid, else false
     */
    private boolean validTimeSpans(EditText start, EditText end){
        boolean isValid = true;
        String[] sTime = start.getText().toString().split(":");
        String[] eTime = end.getText().toString().split(":");

        if(sTime.length == 2 && sTime[1].length() != 2){
            start.setError("2 digits required for minutes");
            isValid = false;
        }
        if(eTime.length == 2 && eTime[1].length() != 2){
            end.setError("2 digits required for minutes");
            isValid = false;
        }
        if(!isValid) return false;

        if(Integer.parseInt(sTime[0]) > 23 || Integer.parseInt(sTime[0]) < 0){
            start.setError("Hours must be 0-23");
            isValid = false;
        }
        if(Integer.parseInt(sTime[0]) > 23 || Integer.parseInt(sTime[0]) < 0){
            end.setError("Hours must be 0-23");
            isValid = false;
        }
        if(!isValid) return false;

        int startTimeSum = sTime.length == 1 ? Integer.parseInt(sTime[0]) * 100 :
                Integer.parseInt(sTime[0]) * 100 + Integer.parseInt(sTime[1]);
        int endTimeSum = eTime.length == 1 ? Integer.parseInt(eTime[0]) * 100 :
                Integer.parseInt(eTime[0]) * 100 + Integer.parseInt(eTime[1]);

        if(endTimeSum - startTimeSum <= 0){
            end.setError("Must be later than start");
            return false;
        }
        return true;
    }

    /**
     * Clear EditText field errors from previous
     * validation attempts.
     */
    private void clearErrors(){
        for(ArrayList<EditText> set : hourSets){
            set.get(0).setError(null);
            set.get(1).setError(null);
        }
    }
}