package com.proje.service;

import com.proje.repository.AidatRepository;
import com.proje.repository.OdemeRepository;
import com.proje.util.LogManager;

public class OdemeService implements  IOdemeService{
    private final  OdemeRepository odemeRepo = new OdemeRepository();
    private final AidatRepository aidatRepo = new AidatRepository();

    @Override
    public void tahsilatGerceklestir(int aidatId, int sakinId, double miktar){
        if(miktar<=0){
            System.out.println("HATA: Ödeme tutarı sıfırdan büyük olmalıdır.");
            return;
        }

        aidatRepo.aidatOde(aidatId, sakinId, miktar);
        LogManager.logYaz("Tahsilat işlemi Service katmanında tamamlandı. Aidat ID: " + aidatId);
    }
}
