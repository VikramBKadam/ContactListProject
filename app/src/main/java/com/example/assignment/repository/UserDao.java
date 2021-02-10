package com.example.assignment.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.assignment.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);

    @Query("SELECT * FROM User")
    DataSource.Factory<Integer,User> getAllUser();

    @Query("Delete From User")
    Completable deleteAllUsers();

    @Query("DELETE  FROM User  WHERE id = :Id")
    Completable deleteUser(int Id);

    @Query("UPDATE User SET name=:u_name,birthday=:u_bday,phoneNumber =:u_phonenumber,phoneNumber2=:u_phonenumber1,phoneNumber3=:u_phonenumber2,image=:image WHERE id = :Id ")
    Completable  updateUserById(String u_name,String u_bday,String u_phonenumber,String u_phonenumber1,String u_phonenumber2,String image,int Id);


    @Query("SELECT * FROM User WHERE id = :Id")

    Single<User> getUserById(int Id);

    

    @Query("SELECT * FROM User WHERE name LIKE :query OR phoneNumber LIKE :query")
    DataSource.Factory<Integer, User> queryAllUser(String query);


}
