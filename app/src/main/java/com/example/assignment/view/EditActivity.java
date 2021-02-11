package com.example.assignment.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.helper.SaveBitmap;
import com.example.assignment.viewmodel.Tab1ViewModel;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    String ProfilePicUri ;
    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;



    Toolbar toolbar;
    Tab1ViewModel viewModel;

    String id;

    ImageView imageView;
    Button button,changeProfilePic;
    EditText textViewName, textViewPhone;
    TextView datePicker,textViewBirthday;


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
        datePicker=findViewById(R.id.date_picker);
        button=findViewById(R.id.editButton);
        changeProfilePic=findViewById(R.id.buttonChangeProfilePic);

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
            if(user.getImage()!=null){
                Glide.with(this).load(Uri.parse(user.getImage()))
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(imageView);
            }else {imageView.setImageResource(R.drawable.ic_baseline_person_24);}

            /*if(user.getImage()!=null){
                imageView.setImageURI(Uri.parse(user.getImage()));

            }*/

        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(EditActivity.this);

            }
        });

            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            String date = dayOfMonth + "/" + month + "/" + year;
                            textViewBirthday.setText(date);
                        }
                    },
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name =textViewName.getText().toString();
                    String phoneNumber =textViewPhone.getText().toString();
                    String birthday =textViewBirthday.getText().toString();
                   // User user =new User(name,phoneNumber,birthday,"uri");
                    if (ProfilePicUri==null){
                        viewModel.getUser().observe(EditActivity.this,user -> {
                            ProfilePicUri=user.getImage();
                        });
                    }
                  //  viewModel.updateUser(name,birthday,phoneNumber,ProfilePicUri,Integer.parseInt(id));
                   Toast.makeText(EditActivity.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });






    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {
                   Log.d("abcd","inside of take photo");

                    checkPermissionAndStartCamera();

                } else if (options[item].equals("Choose from Gallery")) {
                    Log.d("abcd","Choose from Gallary");

                    checkPermissionAndOpenGallery();

                } else
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        else
                Log.d("abcd","Inside else of checkpermissionAndStartCamera");
            startOpenGalleryIntent();
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        else
            Log.d("abcd","Inside else of checkpermissionAndStartCamera");
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(this, "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(this, "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri=null;
                    try {
                        Log.d("abcd", "Inside try of onActivity result of EditActivity");
                        cameraImageUri= SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);


                       // cameraImageUri = uriFromBitmap.getImageUri(this, bitmapCameraImage);
                        Log.d("TAG", "cameraUri: " + cameraImageUri.toString());
                        //  Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());

                        Glide.with(this).load(cameraImageUri).placeholder(R.drawable.ic_baseline_person_24)
                    .into(imageView);
                        /*imageView.setImageURI(cameraImageUri);*/
                        //  ProfilePicPath = cameraImageUri.getPath();

                        ProfilePicUri = cameraImageUri.toString();

                    } catch (Exception e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }

                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    // Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    //   ProfilePicPath = selectedImageUri.getPath();
                    ProfilePicUri = selectedImageUri.toString();
                    Glide.with(this).load(selectedImageUri).placeholder(R.drawable.ic_baseline_person_24)
                            .into(imageView);
                   // imageView.setImageURI(selectedImageUri);
                    break;
            }
        }
    }
}