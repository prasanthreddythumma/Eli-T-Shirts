package com.example.eli_tshirts;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eli_tshirts.POJO.Users;
import com.example.eli_tshirts.POJO.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Java class for activity_register to register user with app
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText fName,lName,email,password,phoneNumber;
    Button register;
    TextView login;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    Users users;
    String rFirstName, rLastName, rEmail, rPassword, rPhone;

    /**
     * Method is called on activity call
     * @param savedInstanceState : parameter of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.btnRegister2);
        login = findViewById(R.id.btnLogin2);
        fName = findViewById(R.id.rFirstName);
        lName = findViewById(R.id.rLastName);
        email = findViewById(R.id.rEmail);
        password = findViewById(R.id.rPassword);
        phoneNumber = findViewById(R.id.rPhoneNumber);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    /**
     * Implementation of onClick method of View.OnClickListener interface. Is triggered on click of element
     * @param view : View parameter
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        rFirstName = fName.getText().toString().trim();
        rLastName = lName.getText().toString().trim();
        rEmail = email.getText().toString().trim();
        rPassword = password.getText().toString().trim();
        rPhone = phoneNumber.getText().toString().trim();

        if(id == R.id.btnRegister2){
            if(checkValidity()){
                userRegistration();
            }
            else {
                return;
            }
        }
        else if(id == R.id.btnLogin2){
            login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }

    }

    /**
     * method is invoked to register user to Authentication and FireStore
     */
    private void userRegistration() {

        auth.createUserWithEmailAndPassword(rEmail,rPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = auth.getCurrentUser();
                    users = new Users(rFirstName, rLastName, rEmail, rPassword, rPhone);
                    db.collection("Users").document(currentUser.getUid()).set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "User details storage failed!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Registration failed!"+ task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Checks validity of all editText
     * @return true or false on validity check
     */
    private boolean checkValidity(){
        if(TextUtils.isEmpty(rFirstName)){
            fName.setError("FirstName can't be blank");
            fName.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(rLastName)){
            lName.setError("LastName can't be blank");
            lName.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(rEmail)){
            email.setError("EmailId can't be blank");
            email.requestFocus();
            return false;
        }
        else if(!Validation.isEmailValid(rEmail)){
            email.setError("Enter Valid EmailID");
            email.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(rPassword)){
            password.setError("Password can't be blank");
            password.requestFocus();
            return false;
        }

        else if(TextUtils.isEmpty(rPhone)){
            phoneNumber.setError("Phone Number can't be blank");
            phoneNumber.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }
}