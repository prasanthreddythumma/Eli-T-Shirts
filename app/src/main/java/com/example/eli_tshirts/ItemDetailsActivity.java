package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

/**
 *Shows the item details
 */
public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Items items;
    ImageView image, fav;
    TextView name,price,description,cart,buy;
    LinearLayout wishList, similar;
    FirebaseFirestore db;
    String collection;

    /**
     * Runs this method when this activity has been initialized.
     * @param savedInstanceState : Bundle element.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        db = FirebaseFirestore.getInstance();
        collection = getIntent().getExtras().getString("collection");
        cart = findViewById(R.id.btnAddToCart);
        buy = findViewById(R.id.btnBuyNow);
        wishList = findViewById(R.id.layout_wishlist);
        similar = findViewById(R.id.layout_similar);
        image = findViewById(R.id.detailsImage);
        name = findViewById(R.id.detailsName);
        price = findViewById(R.id.detailsPrice);
        description = findViewById(R.id.detailsDescription);
        fav = findViewById(R.id.detailsFav);
        if(collection.equals("CasualShirts")) {
            items = getIntent().getExtras().getParcelable("casual");

        }else if(collection.equals("FormalShirts")) {
            items = getIntent().getExtras().getParcelable("formal");

        }else if(collection.equals("Hoodies")) {
            items = getIntent().getExtras().getParcelable("hoodie");

        }else if(collection.equals("Others")) {
            items = getIntent().getExtras().getParcelable("other");

        }else if(collection.equals("Polo TShirts")) {
            items = getIntent().getExtras().getParcelable("polo");

        }else if(collection.equals("TShirts")) {
            items = getIntent().getExtras().getParcelable("tShirts");

        }
        else if(collection.equals("WishList")){
            items = getIntent().getExtras().getParcelable("wish");
        }

        if (items.isFavourite()) {
            fav.setImageResource(R.drawable.fav);
        } else {
            fav.setImageResource(R.drawable.fav_white);
        }
        Picasso.get().load(items.getImage()).into(image);
        name.setText(items.getName());
        price.setText("$ "+items.getPrice());
        description.setText(items.getDescription());
        cart.setOnClickListener(this);
        buy.setOnClickListener(this);
        wishList.setOnClickListener(this);
        similar.setOnClickListener(this);

    }

    /**
     * User clicks on the button this method will run.
     * @param view : view element.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        String c = collection;
        if(id == R.id.layout_wishlist){

            Items item = items;
            final boolean f = item.isFavourite();
            if (item.isFavourite()) {
                fav.setImageResource(R.drawable.fav_white);
                item.setFavourite(false);
            } else {
                fav.setImageResource(R.drawable.fav);
                item.setFavourite(true);
            }
            final Items wishListItem = item;
            db.collection(collection).document(item.getId())
                    .set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {


                        @Override
                        public void onSuccess(Void aVoid) {
                            final Items wishlist = new Items(wishListItem.getName(),wishListItem.getDescription(),wishListItem.getImage(),wishListItem.getPrice(),wishListItem.isFavourite());
                            if(!f){
                                db.collection("WishList").add(wishlist).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            String id = task.getResult().getId();
                                            wishlist.setId(id);
                                            db.collection("WishList").document(id).set(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"Added to wishList", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"Failed to add to wishList: "+task.getException(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else {

                                db.collection("WishList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        String id="";
                                        if(task.isSuccessful()){
                                            for(DocumentSnapshot snapshot : task.getResult()){
                                                if(snapshot.getString("name").trim().equals(wishlist.getName().trim()))
                                                    id = snapshot.getId();
                                            }
                                            db.collection("WishList").document(id).delete();
                                            Toast.makeText(getApplicationContext(),"Removed from WishList", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else if(id == R.id.btnBuyNow){
            db.collection("Orders").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getId();
                        items.setId(id);
                        db.collection("Orders").document(id).set(items).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Order placed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if(id == R.id.btnAddToCart){
            db.collection("CartList").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getId();
                        items.setId(id);
                        db.collection("CartList").document(id).set(items).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Added to Cart", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if(id == R.id.layout_similar){
            if(collection.equals("CasualShirts")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }else if(collection.equals("FormalShirts")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }else if(collection.equals("Hoodies")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }else if(collection.equals("Polo TShirts")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }else if(collection.equals("Others")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }else if(collection.equals("TShirts")) {
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", collection);
                startActivity(intent);
            }
            else if(collection.equals("WishList")){
                Intent intent = new Intent(ItemDetailsActivity.this, WishListActivity.class);

                startActivity(intent);
            }
        }

    }
}