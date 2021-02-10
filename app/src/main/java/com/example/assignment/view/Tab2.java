package com.example.assignment.view;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.TypeConverters;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.helper.SaveBitmap;
import com.example.assignment.helper.UriFromBitmap;
import com.example.assignment.model.User;
import com.example.assignment.repository.DateConverter;
import com.example.assignment.viewmodel.Tab1ViewModel;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;

public class Tab2 extends Fragment {
    MyFragmentAdapter myFragmentAdapter;


    private Tab1ViewModel mViewModel;
    @BindView(R.id.image_view_user)
    ImageView userImage;

    @BindView(R.id.editTextDate)
    TextView userBirthDay;

    @BindView(R.id.editTextPersonName)
    EditText userName;

    @BindView(R.id.editTextPhone)
    EditText userPhoneNumber;
    @BindView(R.id.editTextPhone1)
    EditText userPhoneNumber1;
    @BindView(R.id.editTextPhone2)
    EditText userPhoneNumber2;
    @BindView(R.id.phone_linear_layout1)
    LinearLayout phone_linear_layout1;
    @BindView(R.id.phone_linear_layout2)
    LinearLayout phone_linear_layout2;

    @BindView(R.id.pincode)
    EditText pincode;
    @BindView(R.id.pincode1)
    EditText pincode1;
    @BindView(R.id.pincode2)
    EditText pincode2;

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.date_picker)
    TextView datePick;
    @BindView(R.id.buttonAddProfilePic)
    Button addProfilePic;

    String ProfilePicPath;
    String ProfilePicUri;

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    UriFromBitmap uriFromBitmap;
    SaveBitmap saveBitmap;

    public static Tab2 newInstance() {
        return new Tab2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        ButterKnife.bind(this, view);
        uriFromBitmap = new UriFromBitmap();

        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        userBirthDay.setText(date);
                    }
                },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        userPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phone_linear_layout1.setVisibility(View.VISIBLE);
                return false;
            }
        });
        userPhoneNumber1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phone_linear_layout2.setVisibility(View.VISIBLE);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = userName.getText().toString();
                String phoneNumber;
                String phoneNumber1;
                String phoneNumber2;

                if(pincode.getText().toString().equals("")){
                     phoneNumber = "+91"+userPhoneNumber.getText().toString();
                }else {
                    phoneNumber=pincode.getText().toString()+userPhoneNumber.getText().toString();
                }
                if(pincode1.getText().toString().equals("")){
                    phoneNumber1 = "+91"+userPhoneNumber.getText().toString();
                }else {
                    phoneNumber1=pincode1.getText().toString()+userPhoneNumber.getText().toString();
                }
                if(pincode2.getText().toString().equals("")){
                    phoneNumber2 = "+91"+userPhoneNumber.getText().toString();
                }else {
                    phoneNumber2=pincode2.getText().toString()+userPhoneNumber.getText().toString();
                }


                String birthday = userBirthDay.getText().toString();
                if (name.equals("") || phoneNumber.equals("")) {
                    Toast.makeText(getContext(), "Enter Your Details", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(name, phoneNumber,phoneNumber1,phoneNumber2, birthday, ProfilePicUri, new Date());

                    Log.d("Phone", phoneNumber1);
                    Log.d("Phone", phoneNumber2);
                    Log.d("DATE1", String.valueOf(new Date()));
                    mViewModel.saveToDatabase(user);
                    Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    clearFields();

                    changeTab();
                }


            }
        });

        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());

            }
        });

        return view;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {

                    checkPermissionAndStartCamera();

                } else if (options[item].equals("Choose from Gallery")) {

                    checkPermissionAndOpenGallery();

                } else
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of Tab2Fragment");

                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
                        //   cameraImageUri = uriFromBitmap.getImageUri(getContext(),bitmapCameraImage);
                        Log.d("TAG", "cameraUri: " + cameraImageUri.toString());
                        //  Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());
                        userImage.setImageURI(cameraImageUri);
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
                    userImage.setImageURI(selectedImageUri);
                    break;
            }
        }
    }


    private void changeTab() {
        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setCurrentItem(0, true);
    }

    private void clearFields() {
        userImage.setImageResource(R.drawable.ic_baseline_person_24);
        phone_linear_layout1.setVisibility(View.GONE);
        phone_linear_layout2.setVisibility(View.GONE);
        userName.setText("");
        userPhoneNumber.setText("");
        userPhoneNumber1.setText("");
        userPhoneNumber2.setText("");
        pincode1.setText("");
        pincode.setText("");
        pincode2.setText("");

        userBirthDay.setText("");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(Tab1ViewModel.class);
    }
}