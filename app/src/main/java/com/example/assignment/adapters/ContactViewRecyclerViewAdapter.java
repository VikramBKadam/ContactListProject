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
import com.example.assignment.model.SingleContact;

import java.util.ArrayList;
import java.util.List;

public class ContactViewRecyclerViewAdapter extends RecyclerView.Adapter<ContactViewRecyclerViewAdapter.MyViewHolder> {
  //  private ArrayList<SingleContact> singlePersonContactList;
    private ArrayList<String> singlePersonContactList;

    public ContactViewRecyclerViewAdapter(ArrayList<String> singlePersonContactList) {
        this.singlePersonContactList = singlePersonContactList;
    }

    public void updateSinglePersonContactList(List<String> newsinglePersonContactList) {
        singlePersonContactList.clear();
        singlePersonContactList.addAll(newsinglePersonContactList);
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

        holder.contactNumber.setText(singleContact);

    }

    @Override
    public int getItemCount() {
        return singlePersonContactList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNumber=itemView.findViewById(R.id.textview_contactnumber);



        }
    }

}

