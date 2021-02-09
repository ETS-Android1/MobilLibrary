package com.example.efmist.Fragment.Library.Book;

public class Book {


    String name;
    String imgLink;
    String yazar;
    String aciklama;

    public Book() {
    }

    public Book(String aciklama, String imgLink, String name, String yazar) {
        this.imgLink = imgLink;
        this.yazar = yazar;
        this.aciklama = aciklama;
        this.name=name;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
