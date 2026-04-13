package com.proje.model;

public abstract class User {
    private int id;
    private String ad;
    private String soyad;

    // Yapıcı Metot (Constructor)
    public User(int id, String ad, String soyad) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
    }

    // --- ENCAPSULATION (Kapsülleme) ---
    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public int getId() {
        return id;
    }
}
