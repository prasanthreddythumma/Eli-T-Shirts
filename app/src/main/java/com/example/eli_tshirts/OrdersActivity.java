package com.example.eli_tshirts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eli_tshirts.POJO.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Java class for activity_orders
 */
public class OrdersActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Items> itemsList;
    Items items;

    /**
     *this method will be called first when activity is been initialized
     * @param savedInstanceState :parameter type bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();

        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                String input = "Orders";
                adapter = new RecycleAdapter(input,itemsList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
        });
    }
}