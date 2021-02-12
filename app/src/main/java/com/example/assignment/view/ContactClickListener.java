package com.example.assignment.view;

import android.view.View;

import com.example.assignment.model.Contact;


public interface ContactClickListener {
    void onContactClicked(View view, Contact contact);
}
