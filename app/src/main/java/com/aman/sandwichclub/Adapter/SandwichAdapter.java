package com.aman.sandwichclub.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aman.sandwichclub.DetailActivity;
import com.aman.sandwichclub.R;
import com.aman.sandwichclub.model.Sandwich;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SandwichAdapter  extends RecyclerView.Adapter<SandwichAdapter.SandwichViewHolder> {

    private final List<Sandwich> mSandwichList;
    public SandwichAdapter (List<Sandwich> sandwichList){
        mSandwichList =sandwichList;
    }

    @NonNull
    @Override
    public SandwichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_view,parent,false);
        return new SandwichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SandwichViewHolder holder, int position) {
        Sandwich sandwich = mSandwichList.get(position);
        holder.sandwichName.setText(sandwich.getMainName());
        Picasso.get()
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.snadwichImage);

    }

    @Override
    public int getItemCount() {
        return mSandwichList.size();
    }

    public static class SandwichViewHolder extends RecyclerView.ViewHolder{

        TextView sandwichName;
        ImageView snadwichImage;


        public SandwichViewHolder(@NonNull View itemView) {
            super(itemView);
            sandwichName = itemView.findViewById(R.id.sandwichName);
            snadwichImage = itemView.findViewById(R.id.sandwichImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchDetailActivity(v, getAdapterPosition());
                }
            });
        }

        private void launchDetailActivity(View v, int position) {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POSITION,position);
            v.getContext().startActivity(intent);
        }
    }


}
