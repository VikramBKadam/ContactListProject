package com.example.assignment.repository;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assignment.model.Contact;
import com.example.assignment.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addListOfContact(List<Contact> contactList);

    @Query("SELECT * FROM ContactDB WHERE _id = :Id")
    Single<Contact> getContactById(String Id);

    @Query("select * from contactdb order by name asc")
    DataSource.Factory<Integer, Contact> getAllContacts();

    @Query("select * from contactdb where name like :query or number like :query order by name asc")
    DataSource.Factory<Integer, Contact> getQueryContact(String query);
}
