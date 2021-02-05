package com.example.assignment.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.model.User;
import com.example.assignment.viewmodel.Tab1ViewModel;

public class EditActivity extends AppCompatActivity {

    Toolbar toolbar;
    Tab1ViewModel viewModel;

    String id;

    ImageView imageView;
    Button button;
    EditText textViewName, textViewPhone,textViewBirthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();

    }

    private void init() {
        imageView=findViewById(R.id.image_view_user);
        textViewName =findViewById(R.id.textPersonName);
        textViewBirthday=findViewById(R.id.textBirthDate);
        textViewPhone=findViewById(R.id.textPhone);
        button=findViewById(R.id.editButton);

        viewModel= ViewModelProviders.of(this).get(Tab1ViewModel.class);

        toolbar=findViewById(R.id.tool_bar);
        toolbar.setTitle("Edit User Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id=getIntent().getStringExtra("ID");
        viewModel.fetchDetailsFromDatabase(Integer.parseInt(id));
        viewModel.getUser().observe(this,user-> {
            textViewName.setText(user.getName());
            textViewPhone.setText(user.getPhoneNumber());
            textViewBirthday.setText(user.getBirthday());
            imageView.setImageURI(Uri.parse(user.getImage()));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name =textViewName.getText().toString();
                    String phoneNumber =textViewPhone.getText().toString();
                    String birthday =textViewBirthday.getText().toString();
                    User user =new User(name,phoneNumber,birthday,"uri");
                    viewModel.updateUser(name,birthday,phoneNumber,Integer.parseInt(id));
                   Toast.makeText(EditActivity.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });



        });


    }
}