package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.UUID;

/**
 * java class for activity_profile to display profile details
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView image;
    TextView phone, fullName, email;
    Button btnPwd,btnEdit,btnLogout;
    Uri imageUri;
    FirebaseFirestore db;
    StorageReference reference;
    FirebaseStorage storage;
    FirebaseAuth auth;
    /**
     *this method will be called first when activity is been initialized
     * @param savedInstanceState :parameter type bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        image = findViewById(R.id.profileImage);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileFullName);
        email = findViewById(R.id.profileEmail);
        btnPwd = findViewById(R.id.changePassword);
        btnEdit = findViewById(R.id.changeDetails);
        btnLogout = findViewById(R.id.logout);

        loadDetails();

        image.setOnClickListener(this);
        btnPwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    /**
     * onClick call for elements which is implementation of OnClickListener
     * @param view: view parameter
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.profileImage){
            uploadProfilePicture();
        }
        else if(id == R.id.changePassword){
            startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
        }
        else if(id == R.id.changeDetails){
            startActivity(new Intent(ProfileActivity.this,ProfileEditActivity.class));

        }
        else if(id == R.id.logout){
            auth.signOut();
            startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
        }

    }

    /**
     * method to load details on pae load
     */
    private void loadDetails(){
        String id = auth.getCurrentUser().getUid();
        db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = new Users(documentSnapshot.getString("firstName"),
                        documentSnapshot.getString("lastName"),
                        documentSnapshot.getString("email"),
                        documentSnapshot.getString("pass"),
                        documentSnapshot.getString("phone"));
                user.setImage(documentSnapshot.getString("image"));
                fullName.setText(user.getFirstName()+" "+user.getLastName());
                email.setText(user.getEmail());
                phone.setText("+1 "+user.getPhone());
                if(user.getImage() == null|| user.getImage().isEmpty()){

                }
                else {
                    Picasso.get().load(user.getImage()).into(image);
                }
            }
        });
    }

    /**
     * method invokes to upload image and initiates gallery app for images
     */
    private void uploadProfilePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    /**
     * Method invoked on image uploaded from gallery app
     * @param requestCode : request code from where the app is left
     * @param resultCode : to check if image is present
     * @param data : the image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(image);
            uploadPicture();
        }
    }

    /**
     * method stores image in firebase store and update firestore with downloadUri
     */
    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference storageReference = reference.child("images/"+randomKey);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String imageUrl= uri.toString();
                                final String id = auth.getCurrentUser().getUid();
                                db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Users users = new Users(documentSnapshot.getString("firstName"),
                                                documentSnapshot.getString("lastName"),
                                                documentSnapshot.getString("email"),
                                                documentSnapshot.getString("pass"),
                                                documentSnapshot.getString("phone"));
                                        users.setImage(imageUrl);
                                        db.collection("Users").document(id).set(users);
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}