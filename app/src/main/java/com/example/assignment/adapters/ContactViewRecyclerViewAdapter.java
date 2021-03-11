package com.example.assignment.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;


import java.util.ArrayList;
import java.util.List;

public class ContactViewRecyclerViewAdapter extends RecyclerView.Adapter<ContactViewRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> singlePersonContactList;
    private ArrayList<String> singlePersonContactTypeList;

    public ContactViewRecyclerViewAdapter(ArrayList<String> singlePersonContactList,ArrayList<String> singlePersonContactTypeList) {
        this.singlePersonContactList = singlePersonContactList;
        this.singlePersonContactTypeList=singlePersonContactTypeList;

    }

    public void updateSinglePersonContactList(List<String> newsinglePersonContactList,List<String>newsinglePersonContactTypeList) {
        singlePersonContactList.clear();
        singlePersonContactTypeList.clear();
        singlePersonContactList.addAll(newsinglePersonContactList);
        singlePersonContactTypeList.addAll(newsinglePersonContactTypeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_number, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String singleContact=singlePersonContactList.get(position);
        String singleContactType=singlePersonContactTypeList.get(position);

        holder.contactNumber.setText(singleContact);
       holder.contactType.setText(singleContactType+" : ");

    }

    @Override
    public int getItemCount() {
        return singlePersonContactList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactNumber,contactType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNumber=itemView.findViewById(R.id.textview_contactnumber);
            contactType=itemView.findViewById(R.id.textview_contacttype);



        }
    }

}

