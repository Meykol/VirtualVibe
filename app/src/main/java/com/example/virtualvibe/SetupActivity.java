package com.example.virtualvibe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity
{

    private EditText UserName, FirstName, LastName, Phone;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImageRef;
    private DatabaseReference UsersRef;
    String currentUserId;

    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        UserName = (EditText) findViewById(R.id.setup_username);
        FirstName = (EditText) findViewById(R.id.setup_firstname);
        LastName = (EditText) findViewById(R.id.setup_lastname);
        Phone = (EditText) findViewById(R.id.setup_phone);
        SaveInformationButton = (Button) findViewById(R.id.setup_btn);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        //saving image to firebase
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images");


        //Used for saving information to the database using Firebase
        SaveInformationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveAccountInformation();
            }
        });

        //To edit image on setup using mobile gallery
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    if(snapshot.hasChild("Profile Image"))
                    {
                        String image = snapshot.child("Profile Image").getValue().toString();

                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Select Profile Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
//Use for cropping image in a circular form for profile and save to firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we are updating your Profile Image");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                final Uri resultUri = result.getUri();
                final StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                final String downloadUrl = uri.toString();
                                UsersRef.child("Profile Image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful()) {
                                                Toast.makeText(SetupActivity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            }
                                        });
                            }
                        });
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Error Occurred: Image can not be cropped", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }

    }

    private void SaveAccountInformation()
    {
        String Username = UserName.getText().toString();
        String Firstname = FirstName.getText().toString();
        String Lastname = LastName.getText().toString();
        String Phonenum = Phone.getText().toString().trim();
        
        if (TextUtils.isEmpty(Username))
        {
            Toast.makeText(this, "Please write your username ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Firstname))
        {
            Toast.makeText(this, "Please write your firstname ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Lastname))
        {
            Toast.makeText(this, "Please write your lastname", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Phonenum))
        {
            Toast.makeText(this, "Please write your phone", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Setting up Account");
            loadingBar.setMessage("Please wait, while we are setting up your Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
                userMap.put("username",Username);
                userMap.put("firstname",Firstname);
                userMap.put("lastname",Lastname);
                userMap.put("phone",Phonenum);
                userMap.put("status","Empty for now");
                userMap.put("gender","...");
                userMap.put("birthday","none");
                userMap.put("relationshipstatus","none");
                UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            SendUserToMainActivity();
                            Toast.makeText(SetupActivity.this, "Your Account is created Successfully.", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "Error occurred: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
        }

    }
//After saving the information send the user to the mainpage
    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}