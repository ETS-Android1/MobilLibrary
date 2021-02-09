package com.example.efmist.Fragment.Library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.efmist.Adapter.SectionChangeAdapter;
import com.example.efmist.Fragment.Library.Book.FavFragment;
import com.example.efmist.Fragment.Library.Book.MyBookFragment;
import com.example.efmist.Fragment.Library.Book.MyReadedFragment;
import com.example.efmist.Fragment.Library.Book.ReadingFragment;
import com.example.efmist.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class MyLibraryFragment extends Fragment {


    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    FirebaseAuth firebaseAuth;

    public MyLibraryFragment() {
        // Required empty public constructor
    }


    public static MyLibraryFragment getInstance() {
        return new MyLibraryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth= FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_my_library, container, false);

        viewPager = myFragment.findViewById(R.id.viewpager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        return view;
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
        SectionChangeAdapter adapter =new SectionChangeAdapter(getChildFragmentManager());

        adapter.addFragment(new MyBookFragment(),"Kitaplarım");
        adapter.addFragment(new FavFragment(),"Favorilerim");
        adapter.addFragment(new MyReadedFragment(),"Okuduklarım");
        adapter.addFragment(new ReadingFragment(),"Okuyacaklarım");

        viewPager.setAdapter(adapter);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}