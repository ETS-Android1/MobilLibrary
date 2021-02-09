package com.example.efmist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efmist.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =100 ;
    GoogleSignInClient mGoogleSignInClient;

    Button regBtn;
    Button logBtn;
    EditText emailEt;
    EditText passwordEt;
    TextView forgetpass;
    SignInButton googleLoginB;


    private FirebaseAuth mAuth;

    // dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        regBtn = findViewById(R.id.register_btn);
        logBtn = findViewById(R.id.login_btn);
        emailEt = findViewById(R.id.etEmailAddress);
        passwordEt = findViewById(R.id.etPassword);
        forgetpass= findViewById(R.id.forgetPassTV);
        googleLoginB=findViewById(R.id.googlelogin);


        //before mAuth
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        //In the onCreate() method, initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Giriş Yap");



        // click register button
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // register activity
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        // recover password text view click (forget password)
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
        // handle google login button click
        googleLoginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // begin google login process
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //click login button
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profile page
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // invalid email
                    emailEt.setError("Geçersiz email");
                    emailEt.setFocusable(true);
                }
                else{
                    loginUser(email,password);
                    //Home fragment gider
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();

                }


            }
        });

        progressDialog= new ProgressDialog(this);


    }
    // recover the password
    private void showRecoverPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Şifremi unuttum");

        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);

        // view to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Email Gönder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // input email
                String email= emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });

        //buttons cancel
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();

    }

    private void beginRecovery(String email) {
        // show progres dialog
        progressDialog.setMessage("Email gönderiliyor...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Email gönderildi",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Başarısız...",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //get and show proper error message
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        // show progres dialog
        progressDialog.setMessage("Giriş yapılıyor...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Giriş başarısız.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public boolean onSupportNavigateUp(){

        return super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            //if user is singing in first time then get and show user info from google account
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                //Get user email and uid from auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                //String ad = email.getText().getEmail().setSelection(0,6);
                                //String ad =user.getDisplayName();
                                // when user is register store user info in firebase realtime database too
                                // using hashmap
                                HashMap<Object , String> hashMap = new HashMap<Object, String>();
                                // put info in hashmap
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("name", ""); // user name
                                hashMap.put("image", ""); // profile images

                                // firebase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // path to store user data named "Users"
                                DatabaseReference reference = database.getReference("Users");
                                // put data within hashmap in database
                                reference.child(uid).setValue(hashMap);
                            }

                            //show user email in toast
                            Toast.makeText(LoginActivity.this,""+user.getEmail(),Toast.LENGTH_SHORT).show();
                            // go to profile activity after the logged in
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Giriş başarısız",Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // get and show error message
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}