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

    public SyncNativeContacts(Context context) {
        this.context = context;
        localRepository = new LocalRepository(context);
    }

    public Single<List<Contact>> getContactArrayList() {
        return Single.fromCallable(() -> {

            List<Contact> contactList = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    List<String> numberList = new ArrayList<>();
                    List<String> typeList = new ArrayList<>();

                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                new String[]{id}, null);
                        while (phoneCursor.moveToNext()) {

                            String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) .replaceAll("\\s", "");;
                            String type = ContactType.getContactType(phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
                            if (number.startsWith("0"))
                                number = "+91"+number.substring(1);
                            else if (!number.startsWith("+"))
                                number = "+91"+number;

                            if (!numberList.contains(number))
                                numberList.add(number);

                          //  numberList.add(number);
                            typeList.add(type);

                        }
                        phoneCursor.close();
                    }

                    Contact contact = new Contact(id, name, numberList, typeList);
                    contactList.add(contact);
                }
            }

            if (cursor != null)
                cursor.close();

            return contactList;
        });
    }


}

