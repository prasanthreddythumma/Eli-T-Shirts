package com.example.eli_tshirts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eli_tshirts.POJO.Items;

import java.util.ArrayList;

public class HoodieFragment extends Fragment {
    private FirebaseFirestore db;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Items> itemsList;
    Items items;

    public HoodieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        for (DocumentSnapshot snapshot : task.getResult()) {
            items = new Items(snapshot.getString("name"),
                    snapshot.getString("description"),
                    snapshot.getString("image"),
                    snapshot.getDouble("price"),
                    snapshot.getBoolean("favourite"));
            items.setId(snapshot.getId());
            itemsList.add(items);

        }
        String input = "Hoodies";
        adapter = new RecycleAdapter(input,itemsList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Bundle b = new Bundle();
                b.putParcelable("hoodie", itemsList.get(position));
                b.putString("collection","Hoodies");
                Intent intent = new Intent(getActivity().getApplicationContext(), ItemDetailsActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.cardview ||id == R.id.cardview1||id == R.id.cardview2||id == R.id.cardview3||id == R.id.cardview4||id == R.id.cardview5){
            startActivity(new Intent(getActivity(), ItemDetailsActivity.class));
        }
    }
}