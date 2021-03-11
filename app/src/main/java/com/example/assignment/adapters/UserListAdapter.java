package com.example.assignment.adapters;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.utils.DateUtils;
import com.example.assignment.model.User;
import com.example.assignment.interfaces.ItemClickListener;

import java.util.ArrayList;
import java.util.Date;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = getItem(position);
        setUpHeaderData(user, holder.txtHeader,position);
        if(user != null){
            holder.userName.setText(user.getName());
            holder.phone.setText(user.getPhoneNumber());
        }

        if (user.getImage()!= null){
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(user.getImage()))
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.userImage);}
        else holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);

       


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpHeaderData(User user, TextView dateTextView, int position) {
        if (user == null) {
            return;
        }
        Pair<String, String> timeDateForCurrentUser = DateUtils.getHeaderDateAndTime(new Date(user.getCreationTime()));
        if(position > 0){
            User prevUser = getItem(position -1);
            if(prevUser != null){
                Pair<String, String> timeDateForPrevUser = DateUtils.getHeaderDateAndTime(new Date(prevUser.getCreationTime()));
                if(timeDateForCurrentUser.first.toLowerCase().trim().equals(timeDateForPrevUser.first.toLowerCase().trim())){
                    dateTextView.setVisibility(View.GONE);
                } else  {
                    setHeaderDate(timeDateForCurrentUser.first,dateTextView);
                }
            } else {
                setHeaderDate(timeDateForCurrentUser.first,dateTextView);
            }
        } else {
            setHeaderDate(timeDateForCurrentUser.first,dateTextView);
        }
    }

    private void setHeaderDate(String date, TextView dateTextView) {
        dateTextView.setVisibility(View.VISIBLE);
        dateTextView.setText(date);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

       // @BindView(R.id.name_user)
        TextView userName;
      //  @BindView(R.id.image_user)
        ImageView userImage;
      //  @BindView(R.id.phone_number)
        TextView phone;
     //   @BindView(R.id.txtHeader)
        TextView txtHeader;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            userName=itemView.findViewById(R.id.name_user);
            userImage=itemView.findViewById(R.id.image_user);
            phone=itemView.findViewById(R.id.phone_number);
            txtHeader=itemView.findViewById(R.id.txtHeader);

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

