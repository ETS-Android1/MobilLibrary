package com.example.efmist.Fragment.Library.Yazar;

public class Yazar {

   String hayati,yimgLink,yname,tarih;

    public Yazar() {
    }

    public Yazar(String hayati,String tarih, String yimgLink, String yname) {
        this.hayati = hayati;
        this.yimgLink = yimgLink;
        this.yname = yname;
        this.tarih=tarih;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getHayati() {
        return hayati;
    }

    public void setHayati(String hayati) {
        this.hayati = hayati;
    }

    public String getYimgLink() {
        return yimgLink;
    }

    public void setYimgLink(String yimgLink) {
        this.yimgLink = yimgLink;
    }

    public String getYname() {
        return yname;
    }

    public void setYname(String yname) {
        this.yname = yname;
    }
}
