package com.example.eli_tshirts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eli_tshirts.POJO.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Uses to change the passwords
 */
public class ChangePasswordActivity extends AppCompatActivity {
    EditText pwd,rePwd;
    String pwdNew, pwdRe;
    Button btnPwd;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Users users;

    /**
     * this method will be called first when activity is been initialized
     * @param savedInstanceState :parameter type bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        pwd = findViewById(R.id.newPassword);
        rePwd = findViewById(R.id.rePassword);
        btnPwd = findViewById(R.id.updatePassword);


        btnPwd.setOnClickListener(new View.OnClickListener() {

            /**
             * onClick function
             * @param view : view element
             */
            @Override
            public void onClick(View view) {
                pwdNew = pwd.getText().toString();
                pwdRe = rePwd.getText().toString();

                if(pwdNew.equals(pwdRe)){
                    db.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                        /**
                         * works when the event is succeeded
                         * @param documentSnapshot :Contains data read from a document in the fireStore database.
                         */
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            users = new Users(documentSnapshot.getString("firstName"),
                                    documentSnapshot.getString("lastName"),
                                    documentSnapshot.getString("email"),
                                    documentSnapshot.getString("pass"),
                                    documentSnapshot.getString("phone"));
                            users.setImage(documentSnapshot.getString("image"));
                            users.setPass(pwdNew);
                            db.collection("Users").document(auth.getCurrentUser().getUid()).set(users);
                            auth.getCurrentUser().updatePassword(pwdNew);
                            startActivity(new Intent(ChangePasswordActivity.this,ProfileActivity.class));
                        }
                    });
                }
                else {
                    rePwd.setError("Passwords doesn't match");
                    rePwd.requestFocus();
                }
            }
        });
    }
}