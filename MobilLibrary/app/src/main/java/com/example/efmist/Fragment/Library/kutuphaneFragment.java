package com.example.efmist.Fragment.Library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.efmist.Adapter.SectionChangeAdapter;
import com.example.efmist.Fragment.Library.Book.BookFragment;
import com.example.efmist.Fragment.Library.Yazar.YazarFragment;
import com.example.efmist.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class kutuphaneFragment extends Fragment {


    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    FirebaseAuth firebaseAuth;

    public kutuphaneFragment() {

    }


    public static kutuphaneFragment getInstance() {
        return new kutuphaneFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth=FirebaseAuth.getInstance();
        myFragment = inflater.inflate(R.layout.fragment_kutuphane, container, false);

        viewPager = myFragment.findViewById(R.id.viewpager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        return myFragment;
    }

    //call onActivity create method


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
        SectionChangeAdapter adapter =new SectionChangeAdapter(getChildFragmentManager());

        adapter.addFragment(new BookFragment(),"Kitaplar");
        adapter.addFragment(new YazarFragment(),"Yazarlar");

        viewPager.setAdapter(adapter);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}