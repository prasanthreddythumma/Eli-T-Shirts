package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Users;
import com.example.eli_tshirts.POJO.Validation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditActivity extends AppCompatActivity {
    EditText firstName, lastName, phone, email;
    String sFirstName, sLastName, sPhone, sEmail;
    Button btnSave;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final String id = auth.getCurrentUser().getUid();
        firstName = findViewById(R.id.profileFirstName);
        lastName = findViewById(R.id.profileLastName);
        phone = findViewById(R.id.profilePhone);
        email = findViewById(R.id.profileEmail);
        btnSave = findViewById(R.id.saveDetails);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sFirstName = firstName.getText().toString();
                sLastName = lastName.getText().toString();
                sEmail = email.getText().toString();
                sPhone = phone.getText().toString();

                db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        users = new Users(documentSnapshot.getString("firstName"),
                                documentSnapshot.getString("lastName"),
                                documentSnapshot.getString("email"),
                                documentSnapshot.getString("pass"),
                                documentSnapshot.getString("phone"));
                        users.setImage(documentSnapshot.getString("image"));
                        if (!sFirstName.isEmpty())
                            users.setFirstName(sFirstName);
                        if (!sLastName.isEmpty())
                            users.setLastName(sLastName);
                        if (!sEmail.isEmpty()) {
                            users.setEmail(sEmail);
                        }
                        if (!sPhone.isEmpty())
                            users.setPhone(sPhone);

                        db.collection("Users").document(id).set(users);
                        auth.getCurrentUser().updateEmail(users.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
                    }
                });


            }
        });
    }
}