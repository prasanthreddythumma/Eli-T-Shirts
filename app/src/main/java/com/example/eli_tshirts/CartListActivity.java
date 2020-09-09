package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Java Class for cart_list_activity
 */
public class CartListActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore db;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Items> itemsList;
    TextView buyNow;
    Items items;

    /**
     * onCreate method to run when activity is called
     * @param savedInstanceState: parameter of Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.cartListRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();
        buyNow = findViewById(R.id.btnBuyNow);
        db.collection("CartList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot snapshot:task.getResult()){
                    items = new Items(snapshot.getString("name"),
                            snapshot.getString("description"),
                            snapshot.getString("image"),
                            snapshot.getDouble("price"),
                            snapshot.getBoolean("favourite"));
                    items.setId(snapshot.getId());
                    itemsList.add(items);
                }
                String input = "CartList";
                adapter = new RecycleAdapter(input,itemsList, getApplicationContext());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onImageClick(int position, ImageView imageView) {
                        db.collection("CartList").document(itemsList.get(position).getId()).delete();
                        db.collection("CartList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        count++;
                                    }
                                    if(count == 0){
                                        startActivity(new Intent(CartListActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                    }
                });

            }
        });
        buyNow.setOnClickListener(this);
    }

    /**
     * onClick call for elements which is implementation of OnClickListener
     * @param view : view parameter
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btnBuyNow){
            for(int i=0;i<itemsList.size();i++){
                db.collection("Orders").document().set(items);
            }

            for(int i=0;i<itemsList.size();i++){
                db.collection("CartList").document(itemsList.get(i).getId()).delete();
            }

            startActivity(new Intent(CartListActivity.this,OrdersActivity.class));
        }
    }
}