package com.example.assignment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.assignment.helper.SyncNativeContacts;
import com.example.assignment.model.Contact;
import com.example.assignment.model.User;
import com.example.assignment.repository.LocalRepository;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Tab1ViewModel extends AndroidViewModel {
    public LiveData<PagedList<Contact>> contactList;
    public LiveData<PagedList<Contact>> queryContactList;
    SyncNativeContacts syncNativeContacts;
    public final static String TAG = "TAG";


    public LiveData<PagedList<User>> userList ;
    public MutableLiveData<List<User>> users = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<Contact> contact = new MutableLiveData<>();
    private LocalRepository repository = new LocalRepository(getApplication());
    public LiveData<PagedList<User>> queriedUserList;

    public static MutableLiveData<String> queryString = new MutableLiveData<>();
    public static void setQueryString(String query) {
        queryString.setValue(query);
    }
    public LiveData<String> getQueryString() {
        return queryString;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public Tab1ViewModel(@NonNull Application application) {
        super(application);
    }


    public void fetchDataFromDatabase() {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        userList = new LivePagedListBuilder<>(repository.getAllUsers(),config).build();

    }


    public void saveToDatabase(User user){
        repository.insert(user).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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

    public void fetchContactDetailsFromDatabaseById(String id){
        repository.getContactById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Contact>() {

                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Contact contact1) {
                        contact.setValue(contact1);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }
                });

    }

    public void fetchDetailsFromDatabase(int id){
        repository.getUserById(id).subscribeOn(Schedulers.io())
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
        repository.deleteUser(id).subscribeOn(Schedulers.io())
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

    public void updateUser(String u_name,String u_bday,String u_phonenumber,String u_phonenumbe1,String u_phonenumber2,String image,int Id){
        repository.updateUserById(u_name, u_bday,u_phonenumber,u_phonenumbe1,u_phonenumber2,image, Id).subscribeOn(Schedulers.io())
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
    private static MutableLiveData<Boolean> isMultiSelectOn = new MutableLiveData<>();


    public void setIsMultiSelect(boolean isMultiSelect) {
        isMultiSelectOn.setValue(isMultiSelect);
    }

    public static LiveData<Boolean> getIsMultiSelectOn() {
        return isMultiSelectOn;
    }



    public LiveData<List<User>> getUsers() {
        return users;
    }
    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<Contact>getContact(){return contact;}

    public void queryInit(String query) {



        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();
        queriedUserList = new LivePagedListBuilder<>(repository.queryAllUser(query), config).build();
    }

    public void contactInit() {
        repository = new LocalRepository(getApplication());

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();

        contactList = new LivePagedListBuilder<>(repository.getAllContacts(), config).build();
    }

    public void queryContactInit(String query) {
        repository = new LocalRepository(getApplication());

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();

        queryContactList = new LivePagedListBuilder<>(repository.getQueryContact(query), config).build();
    }

    public void completeContactSync() {
        syncNativeContacts = new SyncNativeContacts(getApplication());
        syncNativeContacts.getContactArrayList().doAfterSuccess(newlist -> addContactListToDB(newlist))
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Contact>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: Inside complete sync  "   );
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Contact> contactList) {
                        Log.e(TAG, "onSuccess: Inside complete sync   -->>  "+contactList.size()   );
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: Inside complete sync error ->> "+e.getMessage()   );
                    }
                });
}


    private void addContactListToDB(List<Contact> contactList) {

        repository.addListOfContact(contactList)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addContactListDB in SyncNativeContacts.: " + e.getMessage());
                    }
                });
    }
}