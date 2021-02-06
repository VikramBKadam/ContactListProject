package com.example.assignment.view;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.model.User;
import com.example.assignment.viewmodel.Tab1ViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Tab1 extends Fragment implements ItemClickListener{

    private Tab1ViewModel mViewModel;
    @BindView(R.id.user_recycler_view)
    RecyclerView UserList;
    ArrayList<User> queryArrayList = new ArrayList<>();
    boolean multiSelectStatus = false;

    ArrayList<User> deleteUserList = new ArrayList<>();


    private UserListAdapter userListAdapter = new UserListAdapter(this);


    public static Tab1 newInstance() {
        return new Tab1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab1_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel= ViewModelProviders.of(getActivity()).get(Tab1ViewModel.class);
        mViewModel.fetchDataFromDatabase();

        UserList.setLayoutManager(new LinearLayoutManager(getContext()));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(UserList);

        UserList.setAdapter(userListAdapter);
        mViewModel.setIsMultiSelect(false);

        mViewModel = new ViewModelProvider(getActivity()).get(Tab1ViewModel.class);

        observeQueryString();
        observeUsersDataList();
        observeMultiSelectStatus();
    }

    private void observeMultiSelectStatus() {
        mViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                multiSelectStatus = aBoolean;
            }
        });
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

        mViewModel.queryInit(query);

        mViewModel.queriedUserList.observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                userListAdapter.submitList(users);
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
            mViewModel.userList.observe(getActivity(), users -> {
                if(users != null  && users.size() > 0 ) {
                   // userList=new ArrayList<>();
                  //  userList.addAll(users);
                    storeUser(users);}
            });

            if( userList!=null){
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



            }

        private void storeUser(List<User> users) {
            userList=new ArrayList<>();
            userList.addAll(users);

        }


    };


    private void observeUsersDataList() {
       mViewModel.userList.observe(this, users -> userListAdapter.submitList(users));

    }

    @Override
    public void onItemClicked(View view, User user) {
        Log.d("TAG",String.valueOf(multiSelectStatus));

        if (multiSelectStatus) {
            if (!deleteUserList.contains(user)) {

                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_500));
                deleteUserList.add(user);
            } else {

                deleteUserList.remove(user);
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
                if(deleteUserList.size()==0){
                    mViewModel.setIsMultiSelect(false);
                }
            }

        } else {
            Intent intent =new Intent(getActivity(),EditActivity.class);
            intent.putExtra("ID",String.valueOf(user.getId()));
            getActivity().startActivity(intent);

        }


    }

    @Override
    public void onItemLongClicked(View view, User user, int index) {
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_500));
        deleteUserList.add(user);
        Log.d("TAG", "LongItemClick: " + index);
        mViewModel.setIsMultiSelect(true);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.multi_select_delete_menu) {

            for (User user :deleteUserList) {
                mViewModel.deleteUserFromDatabase(user.getId());
            }

            deleteUserList.clear();
            mViewModel.setIsMultiSelect(false);
        }
        return super.onOptionsItemSelected(item);
    }
}