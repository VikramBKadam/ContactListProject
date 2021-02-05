package com.example.assignment.view;

import android.view.View;

import com.example.assignment.model.User;

public interface ItemClickListener {
    void onItemClicked(View view, User user);

    void onItemLongClicked(View view, User user, int index);
}
