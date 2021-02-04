package com.example.assignment.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment.model.User;
import com.example.assignment.model.UserDao;
import com.example.assignment.model.UserDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Tab1ViewModel extends AndroidViewModel {


    public MutableLiveData<List<User>> users = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();

    public static MutableLiveData<String> queryString = new MutableLiveData<>();
    public static void setQueryString(String query) {
        queryString.setValue(query);
    }
    public LiveData<String> getQueryString() {
        return queryString;
    }

    UserDao dao = UserDatabase.getInstance(getApplication()).userDao();

    private CompositeDisposable disposable = new CompositeDisposable();

    public Tab1ViewModel(@NonNull Application application) {
        super(application);
    }
    
    public void refresh(){
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {

        dao.getAllUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<User> usersList) {
                        Log.e("TAG", "onSuccess: fetch all user list"+usersList.size() );
                        users.setValue(usersList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }
                });
    }


    public void saveToDatabase(User user){
        dao.insert(user).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            }
            @Override
            public void onComplete() {
                fetchDataFromDatabase();
              //  Toast.makeText(getApplication(),"User Saved",Toast.LENGTH_SHORT).show();
                Log.e("Tab1Viewmodel", "<---------- onComplete: User Saved in DB ---------->" );
            }
            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                e.printStackTrace();
                Log.e("Tab1Viewmodel", "onError: ---->"+e.getMessage() );

            }
        });

    }
    public void fetchDetailsFromDatabase(int id){
        dao.getUserById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull User user1) {
                user.setValue(user1);

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                e.printStackTrace();

            }
        });

    }

   public void deleteUserFromDatabase(int id){
        dao.deleteUser(id).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {


                    }
                    @Override
                    public void onComplete() {
                        fetchDataFromDatabase();

                    }
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    public void updateUser(String u_name,String u_bday,String u_phonenumber,int Id){
        dao.updateUserById(u_name, u_bday,u_phonenumber, Id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }
                });
    }
    public LiveData<List<User>> queryAllUser(Context context, String query) {

        return dao.queryAllUser(query);
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }
    public LiveData<User> getUser() {
        return user;
    }
}