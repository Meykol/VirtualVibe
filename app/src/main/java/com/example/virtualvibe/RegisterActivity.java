package com.example.virtualvibe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity
{

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private TextView ExistingAccount;
    private CheckBox TermsandConditions;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.create_account_btn);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        ExistingAccount = (TextView) findViewById(R.id.login_account_link);
        TermsandConditions = (CheckBox) findViewById(R.id.CBterms_and_conditions);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        //used to show terms and condition
        TermsandConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    materialAlertDialogBuilder.setTitle("Terms and Conditions");
                    materialAlertDialogBuilder.setMessage("Example muna kasi wala pa kaming terms and condition");
                    materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateAccountButton.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TermsandConditions.setChecked(false);
                        }
                    });
                    materialAlertDialogBuilder.show();
                }else {
                    CreateAccountButton.setEnabled(false);
                }
            }
        });

        //If button is click send user to the loginactivity
        ExistingAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToLoginActivity();
            }
        });

        //If button is click the account will be created
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateNewAccount();
            }
        });
    }
    //If button is click send user to the loginactivity
    private void SendUserToLoginActivity()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //If the account is already signed in previously no need to login again after opening the app
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount()
    {
        //Used to store the user input in the register page
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmpassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password field is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmpassword))
        {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating your Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToSetupActivity();

                                Toast.makeText(RegisterActivity.this, "You're account is registered", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occurred " + message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }
    //To send user to setup activity after they sign up
    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}