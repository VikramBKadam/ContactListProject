package com.example.assignment.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends PagedListAdapter<User,UserListAdapter.MyViewHolder>  {
    private ArrayList<User> userList;
    ItemClickListener itemClickListener;
    static String id;


    public static ItemCallback<User> DIFF__CALLBACK = new ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    public UserListAdapter(ItemClickListener itemClickListener){
        super(DIFF__CALLBACK);
        this.itemClickListener = itemClickListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = getItem(position);
        if(user != null){
            holder.userName.setText(user.getName());
        }
       /* Log.d("image r",String.valueOf(user.getImage()));
        Log.d("image o",String.valueOf(R.drawable.ic_baseline_person_24));*/
        if (user.getImage()!= null){
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(user.getImage()))
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.userImage);}
        /*if (user.getImage()!= null){
            holder.userImage.setImageURI(Uri.parse(user.getImage()));*/

        else holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);


    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name_user)
        TextView userName;
        @BindView(R.id.image_user)
        ImageView userImage;
        @BindView(R.id.checkbox)
        ImageView checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick in adapter called: " + v.getId());
            if (itemClickListener != null)
                itemClickListener.onItemClicked(v, getItem(getAdapterPosition()));

        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("TAG", "onLongClick boolean called: " + v.getId());
            if (itemClickListener != null)
                itemClickListener.onItemLongClicked(v, getItem(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }

}

