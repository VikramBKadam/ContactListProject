package com.example.assignment.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.viewmodel.Tab1ViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    MyFragmentAdapter adapter;
    Tab1ViewModel tab1ViewModel;
   // private UserListAdapter userListAdapter = new UserListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {



        toolbar=findViewById(R.id.tool_bar);
        toolbar.setTitle("User Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater =getMenuInflater();

        Tab1ViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                menu.clear();
                if (aBoolean)
                    inflater.inflate(R.menu.multi_select_delete, menu);
                else
                    inflater.inflate(R.menu.menu_search, menu);
            }
        });

    //    setMenu(menu,inflater);


      //  inflater.inflate(R.menu.menu_search,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.search){
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
            SearchView searchView =(SearchView)item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    Tab1ViewModel.setQueryString(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Tab1ViewModel.setQueryString(newText);


                    return false;
                }
            });
        }

       /* Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
        SearchView searchView =(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
               Tab1ViewModel.setQueryString(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               Tab1ViewModel.setQueryString(newText);


                return false;
            }
        });*/

        /*private void setMenu(MenuInflater inflater, Menu menu) {
            Tab1ViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    menu.clear();
                    if (aBoolean)
                        inflater.inflate(R.menu.multi_select_menu, menu);
                    else
                        inflater.inflate(R.menu.menu, menu);
                }
            });
        }*/

        return super.onOptionsItemSelected(item);
    }
}