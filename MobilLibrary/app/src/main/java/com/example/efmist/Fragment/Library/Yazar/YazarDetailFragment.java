package com.example.efmist.Fragment.Library.Yazar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.efmist.R;

public class YazarDetailFragment extends Fragment {

    String hayati, yimgLink, yname, tarih;


    public YazarDetailFragment(String hayati, String yimgLink, String yname,String tarih) {
        this.yname = yname;
        this.hayati = hayati;
        this.yimgLink = yimgLink;
        this.tarih=tarih;

    }

    public YazarDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yazar_detail, container, false);


        ImageView imgholder = v.findViewById(R.id.yimgholder);
        TextView ynameholder = v.findViewById(R.id.ynameholder);
        TextView hayatiholder = v.findViewById(R.id.hayatiholder);
        TextView tarihholder=v.findViewById(R.id.tarihholder);

        Glide.with(getContext()).load(yimgLink).into(imgholder); //sıkıntı var resim kısmını çekemiyor..
        ynameholder.setText(yname);
        hayatiholder.setText(hayati);
        tarihholder.setText(tarih);


        return v;
    }


}