package com.example.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void register(View view) {
        Toast.makeText(this, "Registering...", Toast.LENGTH_LONG).show();
        EditText usernameEdit = (EditText) findViewById(R.id.registerUsernameTextInput);
        EditText passwordEdit = (EditText) findViewById(R.id.registerPasswordTextInput);
        EditText phoneEdit = (EditText) findViewById(R.id.registerPhoneTextInput);
        EditText nameEdit = (EditText) findViewById(R.id.registerNameTextInput);
        String username =  usernameEdit.getText().toString();
        String password= passwordEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String name = nameEdit.getText().toString();

        username = username.trim();
        boolean valid = true;

        if (username.equals("")) {
            usernameEdit.setError("Email can't be empty");
            valid = false;
        }
        else if (!isEmailValid(username)) {
            usernameEdit.setError("Email address is invalid.");
            valid = false;
        }
        else if (username.contains(" ")) {
            usernameEdit.setError("Email can't contain space");
            valid = false;
        }

        if (password.equals("")) {
            passwordEdit.setError("Password can't be empty");
            valid=false;
        }
        else if (password.length() < 5) {
            passwordEdit.setError("Password must be larger than five characters");
            valid=false;
        }

        if (phone.isEmpty()) {
            phoneEdit.setError("Phone number cannot be empty");
            valid = false;
        }

        if (name.isEmpty()) {
            nameEdit.setError("Name field cannot be empty");
            valid = false;
        }

        if (valid) {
            Authorization auth = new Authorization(this);
            auth.createUser(name, username, phone, password);

            usernameEdit.setText("");
            passwordEdit.setText("");
        }
    }

    public void login(View view) {
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        EditText usernameEdit = (EditText) findViewById(R.id.loginUsernameTextInput);
        EditText passwordEdit = (EditText) findViewById(R.id.loginPasswordTextInput);
        String username =  usernameEdit.getText().toString();
        String password= passwordEdit.getText().toString();

        boolean valid = true;

        if (username.equals("")) {
            usernameEdit.setError("Username can't be empty");
            valid = false;
        }

        if (password.equals("")) {
            passwordEdit.setError("Password can't be empty");
            valid=false;
        }

        if (valid) {
            Toast.makeText(this, "Starting Auth...", Toast.LENGTH_SHORT).show();
            Authorization auth = new Authorization(this);
            auth.login(username, password);
            usernameEdit.setText("");
            passwordEdit.setText("");
        }
    }

}