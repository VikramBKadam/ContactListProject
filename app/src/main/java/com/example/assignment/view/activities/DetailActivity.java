package com.example.assignment.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.viewmodel.MyViewModel;

public class DetailActivity extends AppCompatActivity {
    MyViewModel myViewModel;
    Toolbar toolbar;

    String id;

    ImageView imageView;
    TextView textViewName, textViewPhone, textViewBirthday;
     EditText editTextViewName, editTextViewPhone, editTextViewBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        imageView=findViewById(R.id.image_view_user);
        textViewName =findViewById(R.id.textPersonName);
        textViewBirthday=findViewById(R.id.textBirthDate);
        textViewPhone=findViewById(R.id.textPhone);




        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        toolbar=findViewById(R.id.tool_bar);
        toolbar.setTitle("User Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        id=getIntent().getStringExtra("ID");
        myViewModel.fetchDetailsFromDatabase(Integer.parseInt(id));
        myViewModel.getUser().observe(this, user -> {
            if(user.getImage()!=null){
                Glide.with(this).load(Uri.parse(user.getImage()))
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(imageView);
            }else {imageView.setImageResource(R.drawable.ic_baseline_person_24);}

           // imageView.setImageURI(Uri.parse(user.getImage()));
            textViewName.setText("Name: "+user.getName());
            textViewPhone.setText("Mobile: "+user.getPhoneNumber());
            textViewBirthday.setText("BirthDate: "+user.getBirthday());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(DetailActivity.this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
            });


                    ImageView imageView1 = new ImageView(DetailActivity.this);
                    if(user.getImage()!=null){
                        Log.d("TAG", "onTouch: "+user.getImage());
                        Glide.with(DetailActivity.this).load(Uri.parse(user.getImage()))
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(imageView1);
                    }else {imageView1.setImageResource(R.drawable.ic_baseline_person_24);}

                    builder.addContentView(imageView1, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();


                }
            });

        });





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_delete_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case R.id.delete:

                myViewModel.deleteUserFromDatabase(Integer.parseInt(id));
                finish();
                 intent =new Intent(this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit:
               /* textViewBirthday.setVisibility(View.GONE);
                textViewName.setVisibility(View.GONE);
                textViewPhone.setVisibility(View.GONE);
                editTextViewBirthday.setVisibility(View.VISIBLE);
                editTextViewPhone.setVisibility(View.VISIBLE);
                editTextViewBirthday.setVisibility(View.VISIBLE);*/

                  intent =new Intent(this, EditActivity.class);

                id=getIntent().getStringExtra("ID");

                intent.putExtra("ID",id);
                // Log.d("abc",id);
                startActivity(intent);
                Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}