package com.example.assignment.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.helper.SyncNativeContacts;
import com.example.assignment.model.Contact;
import com.example.assignment.viewmodel.Tab1ViewModel;

public class Tab3 extends Fragment implements ContactClickListener{

    TextView textView;

    RecyclerView recyclerViewContactList;
    ContactListAdapter recyclerviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    Tab1ViewModel fragmentViewModel;

    boolean isFragmentActive = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentViewModel = new ViewModelProvider(getActivity()).get(Tab1ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment, container, false);

        init(view);

        fragmentViewModel.contactInit();
        observeContactDB();

        observeQueryString();
        return view;
    }


    private void init(View view) {
        textView=view.findViewById(R.id.total_contacts);
        textView.setText(String.valueOf("Total Contacts : "+SyncNativeContacts.count));
        recyclerViewContactList = view.findViewById(R.id.contacts_recycler_view_);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerviewAdapter = new ContactListAdapter(this);

        recyclerViewContactList.setLayoutManager(layoutManager);
        recyclerViewContactList.setAdapter(recyclerviewAdapter);
    }

    private void observeContactDB() {
        fragmentViewModel.contactList.observe(getViewLifecycleOwner(), new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                recyclerviewAdapter.submitList(contacts);
            }
        });
    }

    private void observeQueryString() {
        fragmentViewModel.getQueryString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                if(isFragmentActive)
                    queryContactList(query);
            }
        });
    }

    private void queryContactList(String query) {
        query = "%" + query + "%";

        fragmentViewModel.queryContactInit(query);

        fragmentViewModel.queryContactList.observe(this, new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                recyclerviewAdapter.submitList(contacts);
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        isFragmentActive = menuVisible;
    }

    @Override
    public void onContactClicked(View view, Contact contact) {
        Log.d("TAG","ContactClicked in Contact list fragment");
        ((MainActivity) getActivity()).switchToViewContactFragment(contact.get_id());
    }
}