package com.example.assignment.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.example.assignment.model.User;
import com.example.assignment.model.UserDao;
import com.example.assignment.model.UserDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalRepository {
    private Context ctx;
    private UserDao userDao;

    public LocalRepository(Context ctx){
        this.ctx = ctx;
        userDao = UserDatabase.getInstance(ctx).userDao();
    }
    public Completable insert(User user){
        return userDao.insert(user);
    }

    public Completable deleteUser(int id){
        return userDao.deleteUser(id);
    }

    public Completable updateUserById(String u_name,String u_bday,String u_phonenumber,int Id){
        return userDao.updateUserById( u_name,u_bday,u_phonenumber, Id);
    }



    public Single<User> getUserById(int id) {
        return userDao.getUserById(id);

    }

    public DataSource.Factory<Integer, User> queryAllUser(String query) {
        return userDao.queryAllUser(query);
    }

    public DataSource.Factory<Integer, User> getAllUsers() {
        return userDao.getAllUser();
    }

}
