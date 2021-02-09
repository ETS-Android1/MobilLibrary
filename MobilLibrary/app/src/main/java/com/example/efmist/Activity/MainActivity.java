package com.example.efmist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.efmist.Activity.Mesaj.ChatsActivity;
import com.example.efmist.Fragment.HomeFragment;
import com.example.efmist.Fragment.Library.kutuphaneFragment;
import com.example.efmist.Fragment.ProfilFragment;
import com.example.efmist.Fragment.Search.SearchFragment;
import com.example.efmist.Not.NoteActivity;
import com.example.efmist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //fab menu
    FloatingActionButton fabMenu, fabNote, fabMsg, fabCam;

    float translationY = 100f;
    boolean isMenuOpen = false;
    private static final String TAG = "MainActivity";
    OvershootInterpolator interpolator = new OvershootInterpolator();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.name);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //fab menu func.
        initFabMenu();

        //Bottom menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //başlangıç sayfası belirleme
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new HomeFragment()).commit();

    }





    //Alt menu yönlendirmesi
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.miHome:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.miSearch:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.miProfile:
                            selectedFragment = new ProfilFragment();
                            break;
                        case R.id.miLibrary:
                            selectedFragment = new kutuphaneFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                    return true;
                }
            };

    //menu animasyonu
    private void initFabMenu() {

        //fab menu
        fabMenu = findViewById(R.id.fabMenu);
        fabMsg = findViewById(R.id.fabMsg);
        fabCam = findViewById(R.id.fabCam);
        fabNote = findViewById(R.id.fabNot);

        fabCam.setAlpha(0f);
        fabMsg.setAlpha(0f);
        fabNote.setAlpha(0f);

        fabCam.setTranslationY(translationY);
        fabMsg.setTranslationY(translationY);
        fabNote.setTranslationY(translationY);

        fabMenu.setOnClickListener(this);
        fabMsg.setOnClickListener(this);
        fabCam.setOnClickListener(this);
        fabNote.setOnClickListener(this);

    }

    //menu butonun basıp açılıcak tekrar basıp kapanack
    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMenu.setImageResource(R.drawable.ic_arrow_down);
        // fabMenu.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();
        fabCam.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabMsg.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabNote.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fabMenu.setImageResource(R.drawable.ic_arrow);
        //fabMenu.animate().setInterpolator(interpolator).rotationBy(0f).setDuration(300).start();
        fabCam.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabMsg.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabNote.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    //fab menu listener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabMenu:
                Log.i(TAG, "menuye açılıyor.");
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabCam:
                Log.i(TAG, "kütüphaye gidiyorsun.");
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                closeMenu();
                break;
            case R.id.fabMsg:
                Log.i(TAG, "Mesajlarına bakabilirsin");
                Intent intent1 = new Intent(this, ChatsActivity.class);
                startActivity(intent1);
                closeMenu();
                break;
            case R.id.fabNot:
                Log.i(TAG, "Not ekleyebilirsin");
                Intent intent2 = new Intent(this, NoteActivity.class);
                startActivity(intent2);
                closeMenu();
                break;
        }


    }


}
