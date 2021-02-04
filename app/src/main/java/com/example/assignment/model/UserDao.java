package com.example.assignment.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);


    @Query("SELECT * FROM User")
    Single<List<User>> getAllUser();

    @Query("Delete From User")
    Completable deleteAllUsers();

    @Query("DELETE  FROM User  WHERE id = :Id")
    Completable deleteUser(int Id);

    @Query("UPDATE User SET name=:u_name,birthday=:u_bday,phoneNumber =:u_phonenumber WHERE id = :Id ")
    Completable  updateUserById(String u_name,String u_bday,String u_phonenumber,int Id);


    @Query("SELECT * FROM User WHERE id = :Id")

    Single<User> getUserById(int Id);

    @Query("SELECT * From User where name like :query ")
    LiveData<List<User>> queryAllUser(String query);
}
