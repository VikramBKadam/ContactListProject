package com.example.assignment.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.assignment.repository.ContactDao;
import com.example.assignment.repository.LocalRepository;
import com.example.assignment.repository.UserDao;
import com.example.assignment.repository.UserDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public static UserDatabase provideDBInstance(@ApplicationContext Context context){
        Migration migration = new Migration(1,2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {


                database.execSQL("alter table 'user' add 'date' integer ");


            }
        };
        return Room.databaseBuilder(
                context.getApplicationContext(),
                UserDatabase.class,
                "userdatabase")
                .addMigrations(migration)
                .build();

    }



    @Provides
    @Singleton
    public static UserDao provideUserDao (UserDatabase database){return database.userDao();}


    @Provides
    @Singleton
    public static ContactDao provideContactDao (UserDatabase database){return database.contactDAO();}

    @Provides
    @Singleton
    public static LocalRepository provideLocalRepository (UserDao userDao,ContactDao contactDao){
        return new LocalRepository(userDao,contactDao);
    }






}
