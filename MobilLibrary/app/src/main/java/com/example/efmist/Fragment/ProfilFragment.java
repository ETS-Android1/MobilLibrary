package com.example.efmist.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.efmist.Activity.FollowersActivity;
import com.example.efmist.Activity.FollowingActivity;
import com.example.efmist.Activity.LoginActivity;
import com.example.efmist.Activity.Mesaj.ChatsActivity;
import com.example.efmist.Adapter.SectionChangeAdapter;
import com.example.efmist.Fragment.Library.Book.FavFragment;
import com.example.efmist.Fragment.Library.Book.MyBookFragment;
import com.example.efmist.Fragment.Library.Book.MyReadedFragment;
import com.example.efmist.Fragment.Library.Book.ReadingFragment;
import com.example.efmist.Fragment.Library.MyLibraryFragment;
import com.example.efmist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabLayout tabLayout;

    Button dictionaryBtn , favBtn;
    TextView nameTv , emailTv, takipciTv , takipTv;
    ImageView avatarIv;
    ImageButton editBtn;

    //firebase authentication
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    String storagePath = "Users_Profile_Images/";

    //dialog
    ProgressDialog pd;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

    // uri of picked image
    Uri image_uri ;

    //for checking profile photo
    String profilePhoto;

    public ProfilFragment(){
   }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          view = inflater.inflate(R.layout.fragment_profil, container, false);

          viewPager = view.findViewById(R.id.viewpagerPro);
          tabLayout= view.findViewById(R.id.tabLayoutPro);


         dictionaryBtn=view.findViewById(R.id.sozlukBtn);
         avatarIv = view.findViewById(R.id.avatarIv);
         nameTv= view.findViewById(R.id.nameTv);
         emailTv= view.findViewById(R.id.emailTv);
         takipciTv= view.findViewById(R.id.takipciTv);
         takipTv= view.findViewById(R.id.takipTv);
         editBtn = view.findViewById(R.id.editBtn);

         takipciTv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent1 = new Intent(getContext(), FollowersActivity.class);
                 startActivity(intent1);

             }
         });
         takipTv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent1 = new Intent(getContext(), FollowingActivity.class);
                 startActivity(intent1);
             }
         });


        dictionaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // go to dictionary page tdk web page
                Uri dct = Uri.parse("https://sozluk.gov.tr/");
                Intent intent = new Intent(Intent.ACTION_VIEW,dct);
                startActivity(intent);


            }
        });



        //init firebase
        firebaseAuth= FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        // init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // init progress dialog
        pd= new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //checks until required data get
                for(DataSnapshot ds: snapshot.getChildren()){
                    // get data
                    String name =""+ds.child("name").getValue();
                    String email =""+ds.child("email").getValue();
                    String image =""+ds.child("image").getValue();


                    //set value
                    nameTv.setText(name);
                    emailTv.setText(email);
                    try{
                        // if image is received then set
                        Picasso.get().load(image).into(avatarIv);

                    }
                    catch (Exception e){
                        //if there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic__default_image_white).into(avatarIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // image edit
        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditImageDialog();
            }
        });

        // user name edit
        nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditUserName();
            }
        });

        // setting
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });


        return view;
    }


    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!= null){
            nameTv.setText(user.getDisplayName());
            emailTv.setText(user.getEmail());

        }
        else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }


    private void showEditProfileDialog() {
        /*
         * Edit Profile Picture
         * Edit User Name
         * Edit */


        String options[] = {"Profil Resmini Düzenle", "Kullanıcı Adını Düzenle"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("DÜZENLE");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle dialog item clicks
                if(which == 0){
                    //Edit Profile Picture
                    pd.setMessage("Profil Resmi Güncelleniyor");
                    profilePhoto = "image";
                    showImagePicDialog();
                }
                else if (which == 1){
                    //Edit User Name
                    pd.setMessage("Updating User Name");
                    showNameUpdateDialog("name");
                }
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void ShowEditUserName() {
        String options[] = {"Kullanıcı Adını Düzenle"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("DÜZENLE");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle dialog item clicks
                if(which == 0){
                    //Edit User Name
                    pd.setMessage("Updating User Name");
                    showNameUpdateDialog("name");
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showNameUpdateDialog(final String name) {
            //custom dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Update" + name);
            //set layout of dialog
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10,10,10,10);
            //add edit text
            final EditText editText = new EditText(getActivity());
            editText.setHint("Enter"+name);
            linearLayout.addView(editText);

            builder.setView(linearLayout);

            //add buttons in dialog to update
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //input text from edit text
                    String value = editText.getText().toString().trim();
                    // validate if user has entered something or not
                    if (!TextUtils.isEmpty(value)){
                        pd.show();
                        HashMap<String, Object> result = new HashMap<String, Object>();
                        result.put(name ,value);

                        databaseReference.child(user.getUid()).updateChildren(result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        pd.dismiss();
                                        Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pd.dismiss();
                                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enter"+name , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //add buttons in dialog to cancel
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            //create and show dialog
            builder.create().show();
    }

    private void ShowEditImageDialog() {
        /*
        Edit Profile Picture
         */
        String options[] = {"Profil Resmini Düzenle"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("DÜZENLE");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle dialog item clicks
                if (which == 0) {
                    //Edit Profile Picture
                    pd.setMessage("Profil Resmi Güncelleniyor");
                    profilePhoto = "image";
                    showImagePicDialog();
                }
            }
        });
        //create and show dialog
        builder.create().show();

    }
    private void showImagePicDialog() {
        //show dialog containing options Camera and Gallery to pick the image

        String options[] = {"Camera", "Gallery"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Nereden yüklensin ");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle dialog item clicks
                if(which == 0){
                    //Camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermissions();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which == 1){
                    //Gallery clicked
                    if (!checkStoragePermission()){
                        requestStoragePermissions();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }


    private void pickFromGallery() {
        // pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        // Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        // put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        // intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;

    }
    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(getActivity(),storagePermissions,STORAGE_REQUEST_CODE);

    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }
    private void requestCameraPermissions(){
       ActivityCompat.requestPermissions(getActivity(),cameraPermissions,CAMERA_REQUEST_CODE);
      // ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        //permissions enabled
                        pickFromCamera();
                    }
                    else{
                        //permissions denied
                        Toast.makeText(getActivity(),"Please enable camera & storage permission", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{

                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        //permissions enabled
                        pickFromGallery();
                    }
                    else{
                        //permissions denied
                        Toast.makeText(getActivity(),"Please enable storage permission", Toast.LENGTH_SHORT).show();

                    }
                }

            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // This method will ve called after picking image from camera or gallary
        if (requestCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get uri of image
                image_uri= data.getData();
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON);
                uploadProfilePhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera, get uri of image
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON);
                uploadProfilePhoto(image_uri);
            }
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //set image
                avatarIv.setImageURI(resultUri);

                //get drawable bitmap for text
                BitmapDrawable bitmapDrawable = (BitmapDrawable) avatarIv.getDrawable();

                TextRecognizer recognizer = new TextRecognizer.Builder(getContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri uri) {
        //show progress
        pd.show();

        //path and name of image to be stored in firebase storage
        String filePathAndName = storagePath + ""+ profilePhoto + "_"+ user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()){
                            // image uploaded
                            // add update url in user's database
                            HashMap<String,Object> results = new HashMap<String, Object>();
                            results.put(profilePhoto,downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Error  Updating Image...", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else{
                            // error
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Some error occured",Toast. LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*************************************************************************************/
    // MENU
     @Override
    public void onCreate (Bundle savedInstanceState){
         setHasOptionsMenu(true);
         super.onCreate(savedInstanceState);
     }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout,menu);
         super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id== R.id.action_logout){
            firebaseAuth.getInstance().signOut();
            checkUserStatus();

        }
        //Intent intent = new Intent(getActivity(), LoginActivity.class);
        //startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    @Override
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
    }

    private void setUpViewPager(ViewPager viewPager) {

        SectionChangeAdapter sectionChangeAdapter = new SectionChangeAdapter(getChildFragmentManager());
        sectionChangeAdapter.addFragment(new MyBookFragment(),"Kitaplarım");
        sectionChangeAdapter.addFragment(new FavFragment(),"Favorilerim");
        sectionChangeAdapter.addFragment(new MyReadedFragment(),"Okuduklarım");
        sectionChangeAdapter.addFragment(new ReadingFragment(),"Okuyacaklarım");

        viewPager.setAdapter(sectionChangeAdapter);
    }
}