package com.proje.model;

// 'extends' ile User sınıfının tüm özelliklerini miras aldık (Inheritance)
public class Sakin extends User {
    private int daireNo;

    public Sakin(int id, String ad, String soyad, int daireNo) {
        super(id, ad, soyad);
        this.daireNo = daireNo;
    }

    public int getDaireNo() {
        return daireNo;
    }

    public void setDaireNo(int daireNo) {
        this.daireNo = daireNo;
    }
}
