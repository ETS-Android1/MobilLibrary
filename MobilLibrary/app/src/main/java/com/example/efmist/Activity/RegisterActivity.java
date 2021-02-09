package com.example.efmist.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efmist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button save;
    TextView loginpage;
    EditText name , email , pass , pass2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reff;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Kayıt Ol");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        save=findViewById(R.id.save_btn);
        loginpage=findViewById(R.id.backlogintv);
        name=findViewById(R.id.nameEt);
        email=findViewById(R.id.emailAdress_ET);
        pass= findViewById(R.id.password_Et);
        pass2=findViewById(R.id.password2_Et);


        firebaseDatabase = FirebaseDatabase.getInstance();
        reff = firebaseDatabase.getReference("member");

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Kullanıcı kaydediliyor...");



        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to login page
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //input
                String ad = name.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String re_password = pass2.getText().toString().trim();


                if(ad.isEmpty()){
                    name.setError("Adınızı yazınız!");
                    name.setFocusable(true);
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    // set error
                    email.setError("Geçersiz Email !");
                    email.setFocusable(true);
                }
                else if(password.length()<8){
                    pass.setError("Şifre uzunluğu en az 8 karakter olmalıdır !");
                    pass.setFocusable(true);
                }
                else if(!password.equals(re_password)){
                    pass2.setError("Şifre tekrarı aynı değil !");
                    pass2.setFocusable(true);
                }
                else
                    {
                    registerUser(mail,password);
                    }

            }
        });


    }

    private void registerUser(String mail, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // sing in and start register
                    progressDialog.dismiss();
                    FirebaseUser user =mAuth.getCurrentUser();

                    //Get user email and uid from auth
                    String mail = user.getEmail();
                    String uid = user.getUid();
                    String ad = name.getText().toString();

                    // when user is register store user info in firebase realtime database too
                    // using hashmap
                    HashMap<Object , String> hashMap = new HashMap<Object, String>();
                    // put info in hashmap
                    hashMap.put("email", mail);
                    hashMap.put("uid", uid);
                    hashMap.put("name",ad );
                    hashMap.put("image", "");


                    // firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // path to store user data named "Users"
                    DatabaseReference reference = database.getReference("Users");
                    // put data within hashmap in database
                    reference.child(uid).setValue(hashMap);



                    Toast.makeText(RegisterActivity.this, "Kaydedildi...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                    //go to profile page


                }
                else{
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Bilgi girişi hatalı!", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); // 1 prevade activity
        return super.onSupportNavigateUp();
    }
}