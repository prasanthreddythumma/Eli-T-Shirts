package com.example.eli_tshirts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eli_tshirts.POJO.Items;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Recycle Adapter for loading recycler item into recycler view
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    ArrayList<Items> itemsList;
    OnItemClickListener listener;
    Context context;
    String input;
    FirebaseFirestore db;

    /**
     * Constructor to call from the source
     * @param input - String to determine source
     * @param itemsList - list of items to be displayed in recycler view
     * @param context - context of the class
     */
    public RecycleAdapter(String input, ArrayList<Items> itemsList, Context context) {
        this.input = input;
        this.itemsList = itemsList;
        this.context = context;
    }

    /**
     * interface for Click listener
     */
    public interface OnItemClickListener {
        /**
         * abstract method for item click
         * @param position : position of item in itemlist
         */
        void onItemClick(int position);

        /**
         * abstract method of image click
         * @param position : position of item in itemlist
         * @param imageView : parameter of type ImageView
         */
        void onImageClick(int position, ImageView imageView);
    }

    /**
     * click listener method
     * @param listener : parameter for the source
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Method to load the appropriate layout
     * @param parent : destination for the recycle item
     * @param viewType : view type
     * @return viewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (input.equals("WishList"))
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_layout, parent, false);
        else if (input.equals("CartList"))
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
        else if (input.equals("Orders"))
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * method to display data in specific recycle item
     * @param holder : viewHolder type
     * @param position : position of the item to be loaded
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        boolean fav = true;
        final ViewHolder dataHolder = holder;
        holder.itemName.setText(itemsList.get(position).getName());
        holder.itemDesc.setText(itemsList.get(position).getDescription());
        holder.itemPrice.setText("$ " + itemsList.get(position).getPrice());
        Picasso.get().load(itemsList.get(position).getImage()).into(holder.itemImage);
        if (input.equals("CasualShirts")) {
            boolean x = itemsList.get(position).isFavourite();
            if (!itemsList.get(position).isFavourite()) {
                holder.itemFav.setImageResource(R.drawable.fav_white);
            } else {
                holder.itemFav.setImageResource(R.drawable.fav);
            }
        }
    }

    /**
     * get item count in itemList
     * @return : size of itemList
     */
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    /**
     * InnerClass ViewHolder to get xml objects
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage, itemFav;
        TextView itemName, itemDesc, itemPrice;

        /**
         * parameterised constructor
         * @param itemView : where the data is displayed.
         */
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemDesc = itemView.findViewById(R.id.itemDescription);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);


            itemView.setTag(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onItemClick(pos);
                        }

                    }
                }
            });
            if (input.equals("CasualShirts")||input.equals("FormalShirts")||input.equals("Hoodies")||input.equals("Others")||input.equals("Polo TShirts")||input.equals("TShirts")) {
                itemFav = itemView.findViewById(R.id.wishList);
                itemFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                listener.onImageClick(pos, itemFav);
                            }
                        }
                    }
                });
            } else if (input.equals("WishList") || input.equals("CartList")) {
                itemFav = itemView.findViewById(R.id.itemDelete);
                itemFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                listener.onImageClick(pos, itemFav);
                            }
                        }
                    }
                });
            }
        }
    }
}