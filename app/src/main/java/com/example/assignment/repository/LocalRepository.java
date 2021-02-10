package com.example.assignment.repository;

import android.content.Context;

import androidx.paging.DataSource;

import com.example.assignment.model.Contact;
import com.example.assignment.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalRepository {
    private Context ctx;
    private UserDao userDao;
    private ContactDao contactDao;

    public LocalRepository(Context ctx){
        this.ctx = ctx;
        userDao = UserDatabase.getInstance(ctx).userDao();
        contactDao=UserDatabase.getInstance(ctx).contactDAO();
    }


    public Completable insert(User user){
        return userDao.insert(user);
    }

    public Completable deleteUser(int id){
        return userDao.deleteUser(id);
    }

    public Completable updateUserById(String u_name,String u_bday,String u_phonenumber,String u_phonenumber1,String u_phonenumber2,String image,int Id){
        return userDao.updateUserById( u_name,u_bday,u_phonenumber,u_phonenumber1,u_phonenumber2,image, Id);

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



    public Completable addContact(Contact contact) {
        return contactDao.addContact(contact);
    }

    public Completable addListOfContact(List<Contact> contactList) {
        return contactDao.addListOfContact(contactList);
    }

    public DataSource.Factory<Integer, Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public DataSource.Factory<Integer, Contact> getQueryContact(String query) {
        return contactDao.getQueryContact(query);
    }

}
