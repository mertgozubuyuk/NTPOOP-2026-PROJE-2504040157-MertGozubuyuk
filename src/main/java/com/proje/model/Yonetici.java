package com.proje.model;

// Inheritance: Yönetici de bir User'dır
public class Yonetici extends User {
    private String yetkiLeveli; // Örn: "Başkan", "Denetçi"

    public Yonetici(int id, String ad, String soyad, String yetkiLeveli) {
        super(id, ad, soyad);
        this.yetkiLeveli = yetkiLeveli;
    }

    // Encapsulation
    public String getYetkiLeveli() {
        return yetkiLeveli;
    }

    public void setYetkiLeveli(String yetkiLeveli) {
        this.yetkiLeveli = yetkiLeveli;
    }
}