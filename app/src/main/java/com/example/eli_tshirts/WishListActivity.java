package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WishListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Items> itemsList;
    Items items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.wishListRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();

        db.collection("WishList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                String input = "WishList";
                adapter = new RecycleAdapter(input,itemsList, getApplicationContext());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle b = new Bundle();
                        b.putParcelable("wish", itemsList.get(position));
                        b.putString("collection","WishList");
                        Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);

                    }

                    @Override
                    public void onImageClick(int position, ImageView imageView) {
                        db.collection("WishList").document(itemsList.get(position).getId()).delete();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}