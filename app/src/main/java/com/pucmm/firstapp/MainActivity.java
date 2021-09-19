package com.pucmm.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePicker;
    private Button dateButton;
    private RadioButton noLikeRadioButton;
    private RadioButton yesLikeRadioButton;
    private ArrayList<CheckBox> checkBoxes;
    private TextView inputName;
    private TextView inputLastname;
    private Spinner orientationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up spinner data
        orientationSpinner = (Spinner) findViewById(R.id.spinnerOrientation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.orientation_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientationSpinner.setAdapter(adapter);

        // Initializing datepicker and setting up initial value for the button
        dateButton = findViewById(R.id.buttonDate);
        datePickerInit();
        Calendar cal = Calendar.getInstance();
        dateButton.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

        // Radio buttons
        noLikeRadioButton = findViewById(R.id.radioButtonNo);
        yesLikeRadioButton = findViewById(R.id.radioButtonYes);

        // Getting checkboxes
        checkBoxes = new ArrayList<CheckBox>();
        checkBoxes.add(findViewById(R.id.checkBoxCSharp));
        checkBoxes.add(findViewById(R.id.checkBoxCpp));
        checkBoxes.add(findViewById(R.id.checkBoxGo));
        checkBoxes.add(findViewById(R.id.checkBoxJS));
        checkBoxes.add(findViewById(R.id.checkBoxJava));
        checkBoxes.add(findViewById(R.id.checkBoxPython));

        // Getting other inputs
        inputName = (TextView) findViewById(R.id.inputPersonName);
        inputLastname = (TextView) findViewById(R.id.inputPersonLastname);
    }

    private void datePickerInit() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                dateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void openDatePickerDialog(View view) {
        datePicker.show();
    }

    public void checkLikeOption(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonYes:
                if (checked) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setEnabled(true);
                    }
                }
                break;
            case R.id.radioButtonNo:
                if (checked) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setEnabled(false);
                    }
                }
                break;
        }
    }

    public void resetInputs(View view) {
        // Clean DOB
        Calendar cal = Calendar.getInstance();
        dateButton.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

        // Clean Name and Lastname
        inputName.setText("");
        inputLastname.setText("");

        // Like Radio Buttons
        noLikeRadioButton.setChecked(false);
        yesLikeRadioButton.setChecked(true);

        // Checkboxes
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
            checkBox.setEnabled(true);
        }

        // Spinner
        orientationSpinner.setSelection(0);
    }

    private boolean verifyInformation() {
        Toast toast;
        // Verifying name and lastName inputs, orientation is not necessary as it is always checked by default
        if (inputName.getText().toString().isEmpty() || inputLastname.getText().toString().isEmpty()) {
            toast = Toast.makeText(getApplicationContext(), "Debe introducir nombre y apellido.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        // Verifying that the user chose at least 1 programming language option if he/she likes programming
        if (yesLikeRadioButton.isChecked()) {
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    return true;
                }
            }
            toast = Toast.makeText(getApplicationContext(), "Debe escoger al menos 1 lenguaje.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }


        return true;
    }

    public void sendInformation(View view) {

        // If verification fails the rest will not run
        if (!verifyInformation()) return;

        String checkboxesSelected = "";
        String message = "";

        if (noLikeRadioButton.isChecked()) {
            message = String.format(
                    "Hola! Mi nombre es: %s %s.\n\n Soy %s. Nací en fecha %s.\n\n No me gusta programar.",
                    inputName.getText().toString(),
                    inputLastname.getText().toString(),
                    orientationSpinner.getSelectedItem().toString(),
                    dateButton.getText().toString()
            );
        } else {
            // Checking for selected checkboxes and adding their text value
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    checkboxesSelected += checkBox.getText().toString() + ", ";
                }
            }

            // Removing last characters from the last element (, )
            checkboxesSelected = checkboxesSelected.substring(0, checkboxesSelected.length() - 2);

            message = String.format(
                    "Hola! Mi nombre es: %s %s.\n\n Soy %s, y nací en fecha %s.\n\n Me gusta programar. Mis lenguajes favoritos son: %s.",
                    inputName.getText().toString(),
                    inputLastname.getText().toString(),
                    orientationSpinner.getSelectedItem().toString(),
                    dateButton.getText().toString(),
                    checkboxesSelected
            );
        }

        Intent intent = new Intent(this, DisplayInformationActivity.class);
        intent.putExtra("INFO", message);
        startActivity(intent);
    }
}