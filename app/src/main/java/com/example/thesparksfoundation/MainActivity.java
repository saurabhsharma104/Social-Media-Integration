package com.example.thesparksfoundation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginWithFacebook(View view) {
        Intent intent = new Intent(this,FacebookProfile.class);
        startActivity(intent);

    }

    public void loginWithGoogle(View view) {
        Intent intent = new Intent(this,GoogleProfile.class);
        startActivity(intent);
    }

    public void login(View view) {
        EditText editText = findViewById(R.id.inputEmail);
        EditText password = findViewById(R.id.inputPassword);
        editText.setText("");
        password.setText("");

        Toast.makeText(this, "Please Login With Social Account....", Toast.LENGTH_SHORT).show();

    }

    public void forgotPassword(View view) {
        Toast.makeText(this, "No Forgot Password Again Login", Toast.LENGTH_SHORT).show();
    }

    public void newUser(View view) {
        Toast.makeText(this, "Please Login With Social Account....", Toast.LENGTH_SHORT).show();
    }
}
