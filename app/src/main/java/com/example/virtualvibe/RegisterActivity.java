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
                    materialAlertDialogBuilder.setMessage("1. ACCEPTANCE OF TERMS\n" +
                            "The U.S. Department of State provides Department Pages as an information service (“Service”) to you (a “user” and a “party”), subject to the following TOU and your acceptance thereof.  Use of any Department Page is voluntary.  If you do not accept any term contained in this TOU, you may not use any Department Pages.  Your use of any Department Page constitutes your acceptance of this TOU.  This TOU may be updated by the Department of State from time to time without notice to you.  It is your responsibility to check this TOU periodically for any changes.  Your continued use of any Department Page following the posting of any changes to this TOU constitutes your acceptance of the changes.  Please note that users of Department Pages are also subject to the Terms of Service (“TOS”) of each respective third-party social media platform (e.g., Facebook, Twitter, Instagram, Weibo) on which Department Pages operate.\n" +
                            "\n" +
                            "2. PLATFORM PRIVACY POLICIES\n" +
                            "By using or otherwise accessing social media platforms on which the U.S. Department of State maintains Department Pages, you are accepting the practices described in each social media platform’s privacy policy and each platform’s TOS.  The Department of State neither controls nor holds any responsibility over the privacy practices or TOS of social media platforms on which it operates Department Pages.  As such, the U.S. Department of State’s Privacy Policy does not apply to platforms themselves.  You may find the U.S. Department of State’s Privacy Policy here.\n" +
                            "When you visit a platform, you provide it with two types of information: any personal information you may knowingly choose to disclose that is collected by the platform, and information collected by the platform on your usage of the platform as you interact with a Department Page.\n" +
                            "\n" +
                            "When you register with any social media platform, you provide it with certain personal information, such as your name, your email address, your telephone number, your address, your gender, schools attended and any other personal or preference information that you provide to it. The U.S. Department of State does not store any of this information or any additional user information beyond that which may already be stored for your account by the platform.\n" +
                            "\n" +
                            "In this TOU, “Content” is defined as any information, communications, links, messages, images, photos, videos, or other data. When posting Content to Department Pages, you simultaneously post Content on the social media platforms on which the Department Pages operate, and you do so at your own risk. Although platforms may allow you to set privacy options that limit access to your account(s), please be aware that no platform security measures are perfect or impenetrable, and the U.S. Department of State cannot control the actions of any other users who may be able to view your accounts and information. Therefore, the Department of State cannot and does not guarantee that any Content you post to any Department Page on a platform will be inaccessible to unauthorized persons.\n" +
                            "\n" +
                            "3. ACCOUNT SECURITY\n" +
                            "You are responsible for maintaining the confidentiality of your account login credentials.  The U.S. Department of State will not be liable for any loss or damage arising from any activities that occur under your login and/or account. See each platform’s TOS for information regarding its own security practices.\n" +
                            "\n" +
                            "4. USER CONDUCT\n" +
                            "You understand that Content, whether publicly posted or privately transmitted, is the sole responsibility of the user or person with whom such Content originated. This means that you, and not the U.S. Department of State, are entirely responsible for any Content you post, email, transmit or otherwise make available on or to a social media platform on which the Department maintains Department Pages. The U.S. Department of State does not control any Content posted except as described below and, as such, does not guarantee the accuracy, integrity, or quality of any such Content. You understand that by visiting Department Pages, you may be exposed to Content that is offensive, indecent, or objectionable.\n" +
                            "\n" +
                            "You agree not to use any Department Page to:\n" +
                            "\n" +
                            "post, email, transmit, or otherwise make available any Content that is unlawful, harmful, threatening, abusive, harassing, tortuous, defamatory, vulgar, obscene, libelous, invasive of another’s privacy, discriminatory, hateful, or racially, ethnically or otherwise objectionable;\n" +
                            "\n" +
                            "harm minors in any way;\n" +
                            "\n" +
                            "impersonate any person or entity;\n" +
                            "\n" +
                            "forge headers or otherwise manipulate identifiers in order to disguise the origin of any Content transmitted through a Department Page;\n" +
                            "\n" +
                            "post, email, transmit, or otherwise make available any Content that you do not have a right to make available under any law or contractual or fiduciary relationships (such as, for example, classified information, inside information, proprietary, and confidential information learned or disclosed as part of employment relationships), or under nondisclosure agreements;\n" +
                            "post, email, transmit, or otherwise use any depiction or reproduction of the Department seal or of any Department logos, symbols, or insignia without authorization;\n" +
                            "post, email, transmit, or otherwise make available any Content that infringes any copyright, trademark, patent, trade secret, right of publicity, right of privacy, or other right of any party;\n" +
                            "post, email, transmit, or otherwise make available any unsolicited or unauthorized advertising, promotional materials, “junk mail,” “chain letters,” “pyramid schemes,” or any other form of solicitation;\n" +
                            "post spam, automated messages, or other comments not related to the topic discussed;\n" +
                            "post, email, transmit, or otherwise make available any material that contains software viruses or any other computer code, files, or programs designed to interrupt, destroy, or limit the functionality of any computer software or hardware or telecommunications equipment;\n" +
                            "disrupt the normal flow of dialogue or otherwise act in a manner that negatively affects other users’ ability to engage in discussions;\n" +
                            "interfere with or disrupt a Department Page, the platform on which it operates, or servers or networks connected to Our Pages, or disobey any requirements, procedures, policies or regulations of networks connected to a Department Page;\n" +
                            "intentionally or unintentionally violate any applicable local, state, national, or international law;\n" +
                            "stalk or otherwise harass another user or person; or\n" +
                            "collect or store personal data about other users.\n" +
                            "Under no circumstances will the U.S. Department of State be liable in any way for any Content post by you or another Department Page or platform user (including, but not limited to, any errors or omissions in any such Content), or for any loss or damage of any kind incurred as a result of the use of any Content posted, emailed, transmitted, or otherwise made available via a Department Page or platform.\n" +
                            "\n" +
                            "You understand that although the U.S. Department of State does not pre-screen all Content posted on Department Pages it manages, the Department may in its sole discretion refuse, decline, block, edit, delete, or move any Content that is available via Department Pages. Without limiting the foregoing, the U.S. Department of State shall have the right to remove any Content that violates this TOU or is otherwise at odds with the intended purpose(s) of a Department Page. The U.S. Department of State will not remove Content solely on the basis of viewpoint.  You agree that you must evaluate, and bear all risks associated with, the use of any Content you post, including any reliance by any party or other user on the accuracy, completeness, or usefulness of such Content.\n" +
                            "\n" +
                            "You understand and agree that the U.S. Department of State may preserve Content and may also disclose Content if required or permitted to do so by law or in the good faith belief that such preservation or disclosure is reasonably necessary to: (a) comply with legal process; (b) enforce this TOU; (c) respond to claims that any Content violates the rights of third parties, or (d) protect the rights, property, or safety of the U.S. Department of State, users of Department Pages, or members of the public.\n" +
                            "\n" +
                            "You understand that technical processing and transmissions with respect to a Department Page, including with respect to any Content you post, may involve (a) transmissions over various networks, and (b) changes to conform and adapt to technical requirements of connecting networks or devices.\n" +
                            "\n" +
                            "You understand that if you post any Content that contains statements or depictions of violence against a person, group of people, or country, the Department of State may report your activity, to include the Content, to an appropriate law enforcement agency/agencies.\n" +
                            "\n" +
                            "You understand and agree that the U.S. Department of State may track and address violations of this Terms of Use on a Department Page, to include carrying out potential blocking and other moderation actions.  The purpose of such tracking is to assist in identifying and determining patterns of recurring violations of this TOU and ensuring Department Pages remain  limited public fora for dialogue, as applicable.\n" +
                            "\n" +
                            "5. SPECIAL ADMONITIONS\n" +
                            "Recognizing the global nature of the Internet, you agree to comply with all local rules regarding online conduct and Content. Specifically, you agree to comply with any applicable laws regarding the transmission of technical data exported from the United States or the country in which you reside or from which you are using a Department Page.\n" +
                            "\n" +
                            "6. YOUR LICENSE TO THE DEPARTMENT\n" +
                            "The U.S. Department of State does not claim ownership of any Content you post to or otherwise make available for inclusion on a Department Page, unless such Content is in fact Department-owned. However, by posting or otherwise submitting Content to a Department Page, you grant the Department of State a worldwide, non-exclusive, royalty-free, perpetual license (with the right to sublicense) to use, copy, reproduce, process, adapt, modify, publish, transmit, perform, display and distribute such Content in any and all media or distribution methods now known or later developed.\n" +
                            "\n" +
                            "7. INDEMNIFICATION\n" +
                            "You agree to indemnify and hold the U.S. Department of State, and its affiliates, officers, agents, grantees, or other partners, and employees, harmless from any claim or demand, including reasonable attorneys’ fees, made by any third party due to or arising out of any Content you submit, post, transmit or make available through a Department Page, your use of the platform on which it operates, your connection to the platform, or any violation by you of this TOU or of any rights of another.\n" +
                            "\n" +
                            "8. NO RESALE OF SERVICE\n" +
                            "You agree not to reproduce, duplicate, copy, sell, resell, or exploit for any commercial purposes or activities, any portion of, use of, or access to, a Department Page.\n" +
                            "\n" +
                            "9. GENERAL PRACTICES REGARDING USE AND STORAGE\n" +
                            "You acknowledge that the U.S. Department of State may in its sole discretion establish general practices and limits concerning use of Department Pages, including, without limitation, the maximum number of days that Content will be retained by a Department Page, and the maximum number of times (and the maximum duration of each) you may access a Department Page in a given period of time. You agree that the U.S. Department of State has no responsibility or liability for the deletion of or failure to store any Content maintained or transmitted by a Department Page. You further acknowledge that the U.S. Department of State reserves the right to change these general practices and limits at any time, in its sole discretion.\n" +
                            "\n" +
                            "10. MODIFICATIONS TO SERVICE\n" +
                            "The U.S. Department of State reserves the right at any time to modify or discontinue, temporarily or permanently, in full or in part, a Department Page that it operates on a platform and the functions available to users on a Department Page, and to delete or remove any Content the Department has posted/provided on a Department Page and/or platform.  The Department of State will not be liable to you or to any third party for any such modification, suspension, or discontinuance.\n" +
                            "\n" +
                            "11. TERMINATION\n" +
                            "You understand and agree that the U.S. Department of State may terminate your access to a Department Page if the Department believes that you have violated or acted inconsistently with the letter or spirit of this TOU.  The U.S. Department of State will not terminate your access solely on the basis of viewpoint.  You understand and agree that any termination of your access to a Department Page pursuant to this TOU may be effected without prior notice and that the U.S. Department of State may immediately suspend your access to the Department Page. Further, you agree that the U.S. Department of State shall not be liable to you or to any third party for any termination of your access to a Department Page.\n" +
                            "\n" +
                            "12. LINKS\n" +
                            "Links to social media platforms, websites, or digital resources may appear on a Department Page. You acknowledge and agree that the U.S. Department of State is not responsible for the availability of any such external sites or resources, and neither endorses nor is responsible or liable for any Content, advertising, products, or other materials on or available from any such sites or resources.  Any such third-party sites operate under separate Terms of Use and Privacy statements.  You agree not to hold the U.S. Department of State liable for any breaches of information provided on any such sites.  You further acknowledge and agree that the U.S. Department of State shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any Content, goods, or services available on or through any such site or resource.\n" +
                            "\n" +
                            "14. LANGUAGE\n" +
                            "This TOU is in English but may also be translated into other languages in the Department’s sole discretion. Content posted to a Department Page in non-local languages may be subject to removal.\n" +
                            "\n" +
                            "15. UPDATES AND REVISIONS\n" +
                            "The U.S. Department of State reserves the right to change this TOU at any time. Any changes will be publicly incorporated into the TOU. We encourage you to review this TOU on an ongoing basis");
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