package com.example.assignment.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactViewRecyclerViewAdapter;

import com.example.assignment.view.activities.MainActivity;
import com.example.assignment.viewmodel.MyViewModel;

import java.util.ArrayList;


public class ViewContactDetailsFragment extends Fragment {
    RecyclerView recyclerView;
    private ContactViewRecyclerViewAdapter contactViewRecyclerViewAdapter = new ContactViewRecyclerViewAdapter (new ArrayList<>(),new ArrayList<>());

    String id;
    MyViewModel mMyViewModel;
    TextView contactName;



    Button showContactList;








    public ViewContactDetailsFragment() {
        // Required empty public constructor
    }



    public static ViewContactDetailsFragment newInstance(String id) {
        ViewContactDetailsFragment fragment = new ViewContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString("ID",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("ID");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view__contact__details_, container, false);

        recyclerView=view.findViewById(R.id.single_person_contact_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(contactViewRecyclerViewAdapter);

        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        contactName=view.findViewById(R.id.text_name_contact);

        showContactList=view.findViewById(R.id.show_contact_list);
            showContactList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG","onClick of show contact list");
                    ((MainActivity) getActivity()).switchToContactListfragment();


                }
            });

        if (getArguments() != null) {
            id = getArguments().getString("ID");
            mMyViewModel.fetchContactDetailsFromDatabaseById(id);
            mMyViewModel.getContact().observe(getViewLifecycleOwner(), contact -> {
                if(contact!=null){
                    Log.e("TAG", contact.getName());
                    contactName.setText(contact.getName());

                    //singleContactList.a

                    contactViewRecyclerViewAdapter.updateSinglePersonContactList(contact.getNumber(),contact.getNumberType());
                   // contactNumber1.setText(contact.getNumber());


                }


            });



        }


return view;
    }


}