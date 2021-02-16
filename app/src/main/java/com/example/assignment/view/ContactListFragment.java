package com.example.assignment.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactListAdapter;
import com.example.assignment.interfaces.ContactClickListener;
import com.example.assignment.model.Contact;
import com.example.assignment.view.activities.MainActivity;
import com.example.assignment.viewmodel.MyViewModel;

public class ContactListFragment extends Fragment implements ContactClickListener {

    TextView totalContacts;

    RecyclerView recyclerViewContactList;
    ContactListAdapter recyclerviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    MyViewModel fragmentMyViewModel;

    boolean isFragmentActive = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentMyViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment, container, false);

        init(view);

        fragmentMyViewModel.contactInit();
        observeContactDB();

        observeQueryString();
        return view;
    }


    private void init(View view) {
        totalContacts=view.findViewById(R.id.total_contacts);
        fragmentMyViewModel.getTotalContacts().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                totalContacts.setText("Total Contacts : "+ String.valueOf(integer));

            }
        });
       // totalContacts.setText(String.valueOf("Total Contacts : "+));
        recyclerViewContactList = view.findViewById(R.id.contacts_recycler_view_);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerviewAdapter = new ContactListAdapter(this);

        recyclerViewContactList.setLayoutManager(layoutManager);
        recyclerViewContactList.setAdapter(recyclerviewAdapter);
    }

    private void observeContactDB() {
        fragmentMyViewModel.contactList.observe(getViewLifecycleOwner(), new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                recyclerviewAdapter.submitList(contacts);
            }
        });
    }

    private void observeQueryString() {
        fragmentMyViewModel.getQueryString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                queryContactList(query);
            }
               /* if(isFragmentActive)
                    queryContactList(query);
            }*/
        });
    }

    private void queryContactList(String query) {
        query = "%" + query + "%";

        fragmentMyViewModel.queryContactInit(query);

        fragmentMyViewModel.queryContactList.observe(this, new Observer<PagedList<Contact>>() {
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