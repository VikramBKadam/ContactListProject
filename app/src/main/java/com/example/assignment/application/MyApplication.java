package com.example.assignment.application;

import android.app.Application;

import com.example.assignment.repository.UserDatabase;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
      //  UserDatabase.getInstance(this);
    }
}
