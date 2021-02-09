package com.example.efmist.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.efmist.Activity.MainActivity;
import com.example.efmist.Adapter.PostAdapter;
import com.example.efmist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FloatingActionButton fabBlog;
    Dialog popAddPost;
    ImageView popupPostImage, popupAddBtn,popupUserImage;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<Post> postList;

    Uri uri_img = null;

    public HomeFragment() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


     //   viewPager = view.findViewById(R.id.viewpagerHome);
     //   tabLayout = view.findViewById(R.id.tabLayoutHome);
        fabBlog = view.findViewById(R.id.fabBlog);
        // ini

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // ini popup
        iniPopup();
        setupPopupImageClick();
        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewpost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        postList = new ArrayList<Post>();
        loadPosts();

        fabBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    private void loadPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(getContext(), postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void iniPopup() {

        popAddPost = new Dialog(getContext());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();

        Glide.with(getContext()).load(currentUser.getPhotoUrl()).into(popupUserImage);
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                String title = popupTitle.getText().toString();
                String description = popupDescription.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    popupTitle.setError("Başlığı doldurunuz");
                } else if (TextUtils.isEmpty(description)) {
                    popupDescription.setError("Açıklamayı doldurunuz");
                } else {
                    uploadData(title, description);
                }
            }
        });
    }

    private void uploadData(final String title, final String description) {
        progressDialog.setMessage("Biraz bekleyiniz..");
        progressDialog.dismiss();
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filepath = "Posts/" + "post_" + timeStamp;

        if (popupPostImage.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) popupPostImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepath);
            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                            while (!uriTask.isSuccessful()) ;
                            String dowloandUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("uid", user.getUid());
                                hashMap.put("pTitle", title);
                                hashMap.put("pDescription", description);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pImg", dowloandUri);
                                hashMap.put("pTime", timeStamp);
                                hashMap.put("pEmail", user.getEmail());

                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
                                reference.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(),"Postunuz yayınlandı",Toast.LENGTH_SHORT).show();
                                                popupTitle.setText("");
                                                popupDescription.setText("");
                                                uri_img=null;

                                                startActivity(new Intent(getContext(), MainActivity.class));

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


        }
    }

  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }*/

    private void setupPopupImageClick() {


        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...

                checkAndRequestForPermission();


            }
        });


    }


    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            // everything goes well : we have permission to access user gallery
            openGallery();

    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    // when user picked an image ...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            uri_img = data.getData();
            popupPostImage.setImageURI(uri_img);

        }


    }


   /* private void setUpViewPager(ViewPager viewPager) {
        SectionChangeAdapter adapter = new SectionChangeAdapter(getChildFragmentManager());

        adapter.addFragment(new BlogFragment(), "Blog");
        adapter.addFragment(new MainFragment(), "Main");
        viewPager.setAdapter(adapter);


    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}