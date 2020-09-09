package com.example.eli_tshirts;

import android.content.Intent;import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eli_tshirts.POJO.Items;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hoodie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.hoodieRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();
        db.collection("Hoodies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            public void onImageClick(int position, ImageView imageView ) {

                                Items item = itemsList.get(position);
                                final boolean fav = item.isFavourite();
                                if (item.isFavourite()) {
                                    imageView.setImageResource(R.drawable.fav_white);
                                    item.setFavourite(false);
                                } else {
                                    imageView.setImageResource(R.drawable.fav);
                                    item.setFavourite(true);
                                }
                                final Items wishListItem = item;
                                db.collection("Hoodies").document(item.getId())
                                        .set(item)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                final Items wishlist = new Items(wishListItem.getName(),wishListItem.getDescription(),wishListItem.getImage(),wishListItem.getPrice(),wishListItem.isFavourite());
                                                if(!fav){
                                                    db.collection("WishList").add(wishlist).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if(task.isSuccessful()){
                                                                String id = task.getResult().getId();
                                                                wishlist.setId(id);
                                                                db.collection("WishList").document(id).set(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getActivity().getApplicationContext(),"Added to wishList", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                            }
                                                            else {
                                                                Toast.makeText(getActivity().getApplicationContext(),"Failed to add to wishList: "+task.getException(), Toast.LENGTH_LONG).show();
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
                                                                Toast.makeText(getActivity().getApplicationContext(),"Removed from WishList", Toast.LENGTH_LONG).show();

                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}