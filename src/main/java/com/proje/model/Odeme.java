package com.proje.model;

import java.time.LocalDateTime;

public class Odeme {
    private int id;
    private int sakinId;
    private int aidatId;
    private double tutar;
    private LocalDateTime odemeTarihi;

    //Yapıcı metot
    public Odeme(int sakinId, int aidatId, double tutar){
        this.sakinId = sakinId;
        this.aidatId = aidatId;
        this.tutar = tutar;
        this.odemeTarihi = LocalDateTime.now();
    }

    //Kapsülleme
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getSakinId(){
        return sakinId;
    }

    public void setSakinId(int sakinId){
        this.sakinId = sakinId;
    }

    public int getAidatId(){
        return aidatId;
    }

    public void setAidatId(int aidatId){
        this.aidatId = aidatId;
    }

    public double getTutar(){
        return tutar;
    }

    public void setTutar(double tutar){
        this.tutar = tutar;
    }

    public LocalDateTime getOdemeTarihi(){
        return odemeTarihi;
    }

    public void setOdemeTarihi(LocalDateTime odemeTarihi){
        this.odemeTarihi = odemeTarihi;
    }
}
