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

import com.example.eli_tshirts.POJO.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Java Class for activity_login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    TextView register;
    EditText email, password;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String userEmail, userPassword;

    /**
     *this method will be called first when activity is been initialized
     * @param savedInstanceState :parameter type bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);
        email = findViewById(R.id.lEmail);
        password = findViewById(R.id.lPassword);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }

    /**
     * onClick call for elements which is implementation of OnClickListener
     * @param view: view parameter
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        userEmail = email.getText().toString().trim();
        userPassword = password.getText().toString().trim();

        if (id == R.id.btnLogin) {
            if(checkValidity()){
                loginUser();
            }
            else {
                return;
            }

        } else if (id == R.id.btnRegister) {
            register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    /**
     * Method to run the login user or not based on credentials
     */
    private void loginUser() {
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = auth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException ex) {
                        Toast.makeText(getApplicationContext(), "Email not exist!", Toast.LENGTH_LONG).show();
                        email.getText().clear();
                        password.getText().clear();
                        email.setError("Email not exist!");
                        email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_LONG).show();
                        password.getText().clear();
                        password.setError("Wrong Password!");
                        password.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    /**
     * Checks validity of all editText
     * @return true or false on validity check
     */
    private boolean checkValidity() {
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Email can't be blank");
            email.requestFocus();
            return false;
        } else if (!Validation.isEmailValid(userEmail)) {
            email.setError("Enter Valid EmailID");
            email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(userPassword)) {
            password.setError("Password can't be blank");
            password.requestFocus();
            return false;
        }  else {
            return true;
        }
    }

    /**
     * invokes on activity start and check if user is already logged in or not
     */
    @Override
    public void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();

        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}