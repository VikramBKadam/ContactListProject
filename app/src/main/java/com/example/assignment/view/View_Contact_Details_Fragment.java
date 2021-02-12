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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactViewRecyclerViewAdapter;
import com.example.assignment.model.SingleContact;
import com.example.assignment.viewmodel.Tab1ViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class View_Contact_Details_Fragment extends Fragment {
    RecyclerView recyclerView;
    private ContactViewRecyclerViewAdapter contactViewRecyclerViewAdapter = new ContactViewRecyclerViewAdapter (new ArrayList<>());

    String id;
    Tab1ViewModel mViewModel;
    TextView contactName;
    /*TextView contactName,contactNumber1,contactNumber2,contactNumber3,contactnumber4,contactNumber5,contactNumber6,contactNumber7
            ,contactNumber8,contactNumber9,contactNumber10;*/
    Button showContactList;
  /*  LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6,linearLayout7,
    linearLayout8,linearLayout9,linearLayout10;*/







    public View_Contact_Details_Fragment() {
        // Required empty public constructor
    }



    public static View_Contact_Details_Fragment newInstance(String id) {
        View_Contact_Details_Fragment fragment = new View_Contact_Details_Fragment();
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

        mViewModel= ViewModelProviders.of(getActivity()).get(Tab1ViewModel.class);
        contactName=view.findViewById(R.id.text_name_contact);
       // contactNumber1=view.findViewById(R.id.text_phone_contact1);
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
            mViewModel.fetchContactDetailsFromDatabaseById(id);
            mViewModel.getContact().observe(this,contact -> {
                if(contact!=null){
                    Log.e("TAG", contact.getName());
                    contactName.setText(contact.getName());
                    ArrayList<SingleContact>singleContactList=new ArrayList<>();
                    //singleContactList.a

                    contactViewRecyclerViewAdapter.updateSinglePersonContactList(contact.getNumber());
                   // contactNumber1.setText(contact.getNumber());


                }


            });



        }


return view;
    }


}