package com.proje.service;

import com.proje.model.Sakin;
import com.proje.repository.SakinRepository;
import com.proje.util.LogManager;
import java.util.List;

//Sakin service sayesinde arayüzdeki kuralları hayata geçiririz
//Aynı zamanda veritabanı ve main arasındaki iş mantığını yürütürüz

public class SakinService implements ISakinService {

    //Veritabanı işlemlerini yürütecek nesne
    private final SakinRepository sakinRepository;

    public SakinService(){
        //Servis başlatıldığında repository nesnesini de oluştururuz
        this.sakinRepository = new SakinRepository();
    }

    @Override
    public void sakinKaydet(Sakin sakin){
        LogManager.logYaz("Servis Katmanı: '" + sakin.getAd() + "' için kayıt kayıt işlemi başlatıldı.");
        sakinRepository.sakinEkle(sakin);
    }

    @Override
    public List<Sakin> sakinleriGetir(){
        return sakinRepository.tumSakinleriGetir();
    }

    @Override
    public void sakinSil(int id){
        if (id <= 0){
            System.out.println("Geçersiz Id! Silme işlemi iptal edildi.");
            LogManager.logYaz("UYARI: Geçersiz ID (" + id +") ile denemesi yapıldı.");
            return;
        }
        sakinRepository.sakinSil(id);
    }
}
