package com.example.assignment.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase instance;
   /* static Migration migration = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'user table' ADD COLUMN 'column' TEXT NOT NULL DEFAULT''");

        }
    };*/

    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDatabase.class,
                    "userdatabase")
                  //  .addMigrations(migration)
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}