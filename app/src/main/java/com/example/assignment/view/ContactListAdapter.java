package com.example.assignment.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.model.Contact;
import com.example.assignment.model.User;

import butterknife.BindView;

public class ContactListAdapter extends PagedListAdapter<Contact, ContactListAdapter.ViewHolder> {
    ContactClickListener contactClickListener;
    MainActivity activity;

    public static DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.get_id().equals(newItem.get_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ContactListAdapter(ContactClickListener contactClickListener) {
        super(DIFF_CALLBACK);
        this.contactClickListener=contactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = getItem(position);

        holder.textViewName.setText(contact.getName());
        holder.textViewNumber.setText(contact.getNumber().get(0));

        Glide.with(holder.imageViewProfilePic.getContext())
                .load(R.drawable.ic_baseline_person_24)
                .into(holder.imageViewProfilePic);


    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName, textViewNumber;
        ImageView imageViewProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfilePic = itemView.findViewById(R.id.image_user);
            textViewName = itemView.findViewById(R.id.name_user);
            textViewNumber = itemView.findViewById(R.id.phone_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d("TAGc", "onClick in Contact adapter called: " +v.getId());
            if (contactClickListener != null)
                contactClickListener.onContactClicked(v, getItem(getAdapterPosition()));

        }
    }
}