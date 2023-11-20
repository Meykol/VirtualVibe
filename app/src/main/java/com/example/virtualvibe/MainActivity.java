package com.example.virtualvibe;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ims.ImsMmTelManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Lazy;


public class MainActivity extends AppCompatActivity
{

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private CircleImageView NavProfileImage;
    private TextView NavProfileName;
    private TextView NavProfileUsername;
    String currentUserID;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private FloatingActionButton Postbtn;
    private FloatingActionButton PostTextbtn;
    private FloatingActionButton PostImgbtn;
    private boolean clicked;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used for floating button
        rotateOpen = AnimationUtils.loadAnimation(this,R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_animation);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_animation);
       // toBottom = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.to_bottom_animation);

        //Onclick for the floating button
        clicked = false;
        Postbtn = (FloatingActionButton) findViewById(R.id.fltbtn_Post);
        PostTextbtn = (FloatingActionButton) findViewById(R.id.fltbtn_PostText);
        PostImgbtn = (FloatingActionButton) findViewById(R.id.fltbtn_Img);

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });
        PostTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "PostText", Toast.LENGTH_SHORT).show();
            }
        });
        PostImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "PostImg", Toast.LENGTH_SHORT).show();
            }
        });


        //Referencing User to the firebase realtimedatabase
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //for authenticating
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //for toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        //for toolbar openning
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);
        NavProfileUsername = (TextView)navView.findViewById(R.id.nav_user_name);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    if(snapshot.hasChild("username"))
                    {
                        String usernameID = snapshot.child("username").getValue().toString();
                        NavProfileName.setText("@"+usernameID);
                    }
                    if(snapshot.hasChild("firstname"))
                    {
                        String username = snapshot.child("firstname").getValue().toString();
                        NavProfileUsername.setText(username);
                    }
                    if(snapshot.hasChild("Profile Image"))
                    {
                        String image = snapshot.child("Profile Image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Account does not exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });
    }

    private void onAddButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        if (!clicked) clicked = true;
        else {
            clicked = false;
        }
    }

    private void setAnimation(boolean clicked) {
        if(!clicked) {
            Postbtn.startAnimation(rotateOpen);
            PostTextbtn.startAnimation(fromBottom);
            PostImgbtn.startAnimation(fromBottom);
        }else {
            Postbtn.startAnimation(rotateClose);
            PostTextbtn.startAnimation(toBottom);
            PostImgbtn.startAnimation(toBottom);
        }
    }
    private void setVisibility(boolean clicked) {
        if (!clicked)
        {
            PostTextbtn.setVisibility(View.VISIBLE);
            PostImgbtn.setVisibility(View.VISIBLE);
        }
        else {
            PostTextbtn.setVisibility(View.INVISIBLE);
            PostImgbtn.setVisibility(View.INVISIBLE);
        }
    }

    // run at the start of the program to check if register or not
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            SendUserTologinActivity();
        }
    //If the account is already existed proceed to main page
        else
        {
            CheckUserExistence();
        }
    }
//To check if account already exist so it can proceed to main page
    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot snapshot)
            {
                if(!snapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled( DatabaseError error)
            {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserTologinActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    //If click display the appropriate function in navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void UserMenuSelector(MenuItem item)
    {
        if (item.getItemId() == R.id.nav_profile)
        {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_home)
        {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_friends)
        {
            Toast.makeText(this, "Friends", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_find_friends)
        {
            Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_messages)
        {
            Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_settings)
        {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_logout)
        {
            mAuth.signOut();
            SendUserTologinActivity();
        }
    }
}