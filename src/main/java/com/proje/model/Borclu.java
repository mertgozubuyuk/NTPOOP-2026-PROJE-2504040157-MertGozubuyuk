package com.proje.model;

public class Borclu {
    private int id;       // aidat id
    private int sakinId;  // bunu ekle
    private String adSoyad;
    private double miktar;
    private String ay;

    public Borclu(int id, int sakinId, String adSoyad, double miktar, String ay) {
        this.id = id;
        this.sakinId = sakinId;
        this.adSoyad = adSoyad;
        this.miktar = miktar;
        this.ay = ay;
    }

    public int getId() { return id; }
    public int getSakinId() { return sakinId; }
    public String getAdSoyad() { return adSoyad; }
    public double getMiktar() { return miktar; }
    public String getAy() { return ay; }
}