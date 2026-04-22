package com.proje.model;

public class Aidat {
    private int id;
    private int sakinId; //Hangi sakine ait olduğu eşleşir
    private double miktar;
    private String ay;
    private boolean odendiMi;

    // Constructor (Yapıcı Metot) - Yeni aidat oluştururken kullanılır
    public Aidat(int sakinId , double miktar , String ay , boolean odendiMi){
        this.sakinId=sakinId;
        this.miktar=miktar;
        this.ay=ay;
        this.odendiMi=odendiMi;
    }

    //Getter ve Settter metotları
    public int getId(){
        return id;
    }
    public void setId(){
        this.id=id;
    }

    public int getSakinId(){
        return sakinId=sakinId;
    }
    public void setSakinId(){
        this.sakinId=sakinId;
    }

    public double getMiktar(){
        return miktar;
    }
    public void setMiktar(){
        this.miktar=miktar;
    }

    public String getAy(){
        return ay;
    }
    public boolean isOdendiMi(){
        return odendiMi;
    }
    public void setOdendiMi(){
        this.odendiMi=odendiMi;
    }
}

