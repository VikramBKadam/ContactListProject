package com.example.assignment.view;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.assignment.R;
import com.example.assignment.adapters.UserListAdapter;
import com.example.assignment.interfaces.ItemClickListener;
import com.example.assignment.model.User;
import com.example.assignment.view.activities.MainActivity;
import com.example.assignment.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class UserListFragment extends Fragment implements ItemClickListener {


    private MyViewModel mMyViewModel;
   // @BindView(R.id.user_recycler_view)
    RecyclerView UserList;
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
    ArrayList<User> queryArrayList = new ArrayList<>();
    boolean multiSelectStatus = false;

    ArrayList<User> deleteUserList = new ArrayList<>();


    private UserListAdapter userListAdapter = new UserListAdapter(this);


    public static UserListFragment newInstance() {
        return new UserListFragment();
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
        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        mMyViewModel.fetchDataFromDatabase();
       // linearLayoutManager.setReverseLayout(true);
       // linearLayoutManager.setStackFromEnd(true);
        UserList =(RecyclerView)view.findViewById(R.id.user_recycler_view);

        UserList.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(UserList);

        UserList.setAdapter(userListAdapter);
        mMyViewModel.setIsMultiSelect(false);

        mMyViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);

        observeQueryString();
        observeUsersDataList();
        observeMultiSelectStatus();
        observeRecyclerViewPosition();
    }

    private void observeMultiSelectStatus() {
        mMyViewModel.getIsMultiSelectOn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                multiSelectStatus = aBoolean;
            }
        });
    }
    private void observeQueryString() {
        mMyViewModel.getQueryString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                Log.d("TAG", "Inside Tab1Fragment: " + query);
                queryChatList(query);
            }
        });
    }
    private void observeRecyclerViewPosition() {
        userListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                UserList.scrollToPosition(positionStart);
                super.onItemRangeInserted(positionStart, itemCount);
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                userListAdapter.notifyDataSetChanged();
            }
        });
    }


    private void queryChatList(String query) {
        query = "%" + query + "%";

        mMyViewModel.queryInit(query);

        mMyViewModel.queriedUserList.observe(this, new Observer<PagedList<User>>() {
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
            mMyViewModel.userList.observe(getActivity(), users -> {
                if(users != null  && users.size() > 0 ) {
                   // userList=new ArrayList<>();
                  //  userList.addAll(users);
                    storeUser(users);}
            });

            if( userList!=null){
                User user=userList.get(viewHolder.getAdapterPosition());
                final CharSequence[] options = { "View Details","Delete","Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("View Details")) {
                         /*   Intent intent =new Intent(getActivity(), DetailActivity.class);*/
                            ((MainActivity) getActivity()).switchTodetailfragment(user.getId());

                           /* intent.putExtra("ID",String.valueOf(user.getId()));
                            getActivity().startActivity(intent);*/


                        }  else if(options[item].equals("Delete")){
                            mMyViewModel.deleteUserFromDatabase(user.getId());

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
       mMyViewModel.userList.observe(getViewLifecycleOwner(), users ->

               userListAdapter.submitList(users));

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
                    mMyViewModel.setIsMultiSelect(false);
                }
            }

        } else {
            /*ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.getCurrentItem();*/
           /* CreateEntryFragment tab2=CreateEntryFragment.newInstance();*/
            RelativeLayout relativeLayout=view.findViewById(R.id.tab1fragment);
            if (relativeLayout!=null){
                relativeLayout.setVisibility(View.GONE);
            }




            ((MainActivity) getActivity()).switchTodetailfragment(user.getId());


            /*Intent intent =new Intent(getActivity(),DetailActivity.class);
            intent.putExtra("ID",String.valueOf(user.getId()));
            getActivity().startActivity(intent);*/

        }


    }

    @Override
    public void onItemLongClicked(View view, User user, int index) {
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_500));
        deleteUserList.add(user);
        Log.d("TAG", "LongItemClick: " + index);
        mMyViewModel.setIsMultiSelect(true);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.multi_select_delete_menu) {

            for (User user :deleteUserList) {
                mMyViewModel.deleteUserFromDatabase(user.getId());
            }

            deleteUserList.clear();
            mMyViewModel.setIsMultiSelect(false);
        }
        return super.onOptionsItemSelected(item);
    }
}