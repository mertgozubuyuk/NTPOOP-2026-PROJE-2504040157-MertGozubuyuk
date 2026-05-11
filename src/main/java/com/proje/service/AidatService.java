package com.proje.service;

import com.proje.repository.AidatRepository;
import com.proje.util.LogManager;

//AidatService arayüzündeki metodları doldurur.
//Finansal iş mantığının ve kontrollerin yapıldığı yerdir

public class AidatService implements IAidatService{

    private final AidatRepository aidatRepository;

    public AidatService(){
        this.aidatRepository = new AidatRepository();
    }

    @Override
    public void topluAidatOlustur(double miktar, String ay){
        //Finansal mantık kontrolü
        if(miktar<=0){
            System.out.println("HATA: Aİdat miktarı 0'dan büyük olmalıdır!");
            return;
        }

        //Zaman(Ay) Kontrolü
        if(ay == null || ay.trim().isEmpty()){
            System.out.println("HATA: Aidat dönemi (Ay bilgisi) belirtilmelidir!");
            return;
        }

        LogManager.logYaz("Toplu aidat oluşturuluyor: " + ay + " ayı için " + miktar + " TL");
        aidatRepository.topluAidatTanımlama(miktar, ay);
    }

    @Override
    public void borclulariGoster(){
        aidatRepository.borcluSakinleriListele();
    }

    @Override
    public void aidatTahsilEt(int aidatId){
        if(aidatId<=0){
            System.out.println("HATA: Geçersiz Aidat ID!");
            return;
        }
        LogManager.logYaz("Aidat ödeme ile ilgili işlem başlatıldı. Aidat ID: " + aidatId);
        aidatRepository.aidatOde(aidatId);
    }

    @Override
    public void raporuHazirla(){
        LogManager.logYaz("Finansal rapor görüntülendi.");
        aidatRepository.finansalOzetRaporu();
    }

    @Override
    public void daireDokumuGetir(int daireNo){
        if(daireNo<=0){
            System.out.println("HATA: Geçersiz daire numarası!");
            return;
        }
        aidatRepository.daireGecmisiListele(daireNo);
    }
}
