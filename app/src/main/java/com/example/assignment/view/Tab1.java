package com.example.assignment.view;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment.R;
import com.example.assignment.model.User;
import com.example.assignment.viewmodel.Tab1ViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Tab1 extends Fragment {

    private Tab1ViewModel mViewModel;
    @BindView(R.id.user_recycler_view)
    RecyclerView UserList;
    ArrayList<User> queryArrayList = new ArrayList<>();

    private UserListAdapter userListAdapter = new UserListAdapter();


    public static Tab1 newInstance() {
        return new Tab1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab1_fragment, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel= ViewModelProviders.of(getActivity()).get(Tab1ViewModel.class);
        mViewModel.refresh();

        UserList.setLayoutManager(new LinearLayoutManager(getContext()));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(UserList);

        UserList.setAdapter(userListAdapter);

        mViewModel = new ViewModelProvider(getActivity()).get(Tab1ViewModel.class);
        observeQueryString();
        observeUsersDataList();
    }
    private void observeQueryString() {
        mViewModel.getQueryString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String query) {
                Log.d("TAG", "Inside Tab1Fragment: " + query);
                queryChatList(query);
            }
        });
    }

    private void queryChatList(String query) {
        query = "%" + query + "%";


        mViewModel.queryAllUser(getContext(), query).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                queryArrayList.clear();
                queryArrayList = (ArrayList<User>) users;
                userListAdapter.updateUserList(queryArrayList);
            }
        });
    }

    @Override
    public void onResume() {
      // observeUsersDataList();
        super.onResume();

        Log.e("TAG", "onResume: " );
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        ArrayList<User> userList;
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            userListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

            mViewModel.getUsers().observe(getActivity(), users -> {
                if(users != null  && users.size() > 0 ) {
                    storeUser(users);
                }
            });
            User user=userList.get(viewHolder.getAdapterPosition());


                final CharSequence[] options = { "View Details", "Edit","Delete","Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("View Details")) {
                            Intent intent =new Intent(getActivity(),DetailActivity.class);
                            intent.putExtra("ID",String.valueOf(user.getId()));
                            getActivity().startActivity(intent);


                        } else if (options[item].equals("Edit")) {
                            Intent intent =new Intent(getActivity(),EditActivity.class);
                            intent.putExtra("ID",String.valueOf(user.getId()));
                            getActivity().startActivity(intent);

                        } else if(options[item].equals("Delete")){
                            mViewModel.deleteUserFromDatabase(user.getId());

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }

        private void storeUser(List<User> users) {
            userList=new ArrayList<>();
            userList.addAll(users);

        }


    };


    private void observeUsersDataList() {
        mViewModel.getUsers().observe(this, users -> {
            if(users != null  && users.size() > 0 ) {
                Log.e("TAG", "observeUsersDataList:  users size"+users.size() );
                userListAdapter.updateUserList(users);

            }
        });

    }
}