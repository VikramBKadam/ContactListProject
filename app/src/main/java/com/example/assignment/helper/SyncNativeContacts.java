package com.example.assignment.helper;



import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


import com.example.assignment.model.Contact;
import com.example.assignment.repository.LocalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class SyncNativeContacts {

    Context context;
    LocalRepository localRepository;
    public static int count;

    public SyncNativeContacts(Context context) {
        this.context = context;
        localRepository = new LocalRepository(context);
    }


    public Single<List<Contact>> getContactArrayList() {
       /* return Single.fromCallable(() ->{


            int count = 0;
            List<Contact> contactList = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    List<String> numberList = new ArrayList<>();

                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                new String[]{id}, null);
                        while (phoneCursor.moveToNext()) {
                            String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            numberList.add(number);
                            Log.d("TAG", "Name is: " + name);
                            Log.d("TAG", "Number is: " + number);
                            count++;
                        }
                        phoneCursor.close();
                    }

                    Contact contact = new Contact(id, name, numberList);
                    contactList.add(contact);
                }
            }

            if (cursor != null)
                cursor.close();
            Log.d("TAG", "Total Count: " + count);

            return contactList;
        });*/

        return Single.fromCallable(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                 count = 0;

                ArrayList<Contact> contactList = new ArrayList<>();
                StringBuilder mBuilder = new StringBuilder();
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

                if (mCursor != null && mCursor.getCount() > 0 ) {
                    while (mCursor.moveToNext()) {
                        count++;
                        Integer id = mCursor.getInt(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String photo = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                        ArrayList<String> phoneList = new ArrayList<>();
                        int hasPhoneNumber = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                        if (hasPhoneNumber > 0) {
                            mBuilder.append("\"").append(name).append("\"");
                            Cursor phoneCursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                                    new String[]{""+id}, null);

                            assert phoneCursor != null;
                            while (phoneCursor.moveToNext()) {
                                String typeLabel = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        .replaceAll("\\s", "");
                                Log.e("TAG!",phoneNumber);
                                if (phoneNumber.startsWith("0"))
                                    phoneNumber = "+91"+phoneNumber.substring(1);
                                else if (!phoneNumber.startsWith("+"))
                                    phoneNumber = "+91"+phoneNumber;

                                if (!phoneList.contains(phoneNumber))
                                    phoneList.add( typeLabel +" : "+phoneNumber);
                                Log.d("ContactListFragment", "id: "+id);
                                Log.d("ContactListFragment", "Name: "+name);
                                Log.d("ContactListFragment", "Phone: "+phoneNumber);
                            }
                            phoneCursor.close();
                        }
                        if (name != null && !name.equals("") && phoneList.size() != 0) {
                            Contact contact = new Contact(String.valueOf(id), name, phoneList);
                            contactList.add(contact);
                        }
                    }
                }
                mCursor.close();

                Log.d("TAGcount", "Total Count: " + count);

                return contactList;
            }
        });
    }
}

