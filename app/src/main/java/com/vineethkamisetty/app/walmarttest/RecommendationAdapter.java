package com.vineethkamisetty.app.walmarttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private static final String TAG = "ItemAdapter";
    private Context context;
    private List<Item> list;

    RecommendationAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);

        if (item.getItemThumbnailImage() != null)
            Picasso.get().load(item.getItemThumbnailImage()).into(holder.itemImage);
        holder.itemTitle.setText(item.getItemName());
        holder.itemRating.setText(item.getItemRating());
        String price = "Price: $" + String.valueOf(item.getItemPrice());
        holder.itemPrice.setText(price);
        holder.itemDescription.setText(item.getItemShortDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemRating, itemPrice, itemDescription;
        ImageView itemImage;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            itemTitle = itemView.findViewById(R.id.itemName);
            itemRating = itemView.findViewById(R.id.itemRating);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.item_thumbnail);
            itemDescription = itemView.findViewById(R.id.itemDescription);
        }
    }

}
