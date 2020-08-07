package com.example.eli_tshirts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CasualShirtFragment extends Fragment implements View.OnClickListener {
    CardView cardView1,cardView2,cardView3,cardView4,cardView5, cardView6;
    public CasualShirtFragment() {
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
        return inflater.inflate(R.layout.fragment_casual_shirt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardView1 = view.findViewById(R.id.cardview);
        cardView2 = view.findViewById(R.id.cardview2);
        cardView3 = view.findViewById(R.id.cardview3);
        cardView4 = view.findViewById(R.id.cardview1);
        cardView5 = view.findViewById(R.id.cardview4);
        cardView6 = view.findViewById(R.id.cardview5);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.cardview ||id == R.id.cardview1||id == R.id.cardview2||id == R.id.cardview3||id == R.id.cardview4||id == R.id.cardview5){
            startActivity(new Intent(getActivity(), ItemDetailsActivity.class));
        }
    }
}