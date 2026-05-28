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
        //Null KOntrölü(Program çökmesini engelleme)
        if(sakin == null){
            System.out.println("HATA: Kaydedilecek sakin bilgisi bulunamadı!");
            return;
        }

        //İsim ve Soyad Doğrulaması
        if(sakin.getAd() == null || sakin.getAd().trim().isEmpty() ||
        sakin.getSoyad() == null || sakin.getSoyad().trim().isEmpty()){
            System.out.println("HATA: Sakin adı ve soyadı boş bırakılamaz!");
            LogManager.logYaz("HATA: Eksik isim/soyad ile kayıt denemesi yapıldı.");
            return;
        }

        //Daire Numarası kontrolü
        if(sakin.getDaireNo()<=0){
            System.out.println("HATA: Daire numarası 0 veya negatif olamaz");
            return;
        }

        //Başarılı doğrulama
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

    @Override
    public void sakinGuncelle(Sakin sakin) {
        // Null Kontrolü
        if (sakin == null) {
            System.out.println("HATA: Güncellenecek sakin bilgisi bulunamadı!");
            return;
        }

        // İsim ve Soyad Doğrulaması
        if (sakin.getAd() == null || sakin.getAd().trim().isEmpty() ||
                sakin.getSoyad() == null || sakin.getSoyad().trim().isEmpty()) {
            System.out.println("HATA: Sakin adı ve soyadı boş bırakılamaz!");
            return;
        }

        // Daire Numarası kontrolü
        if (sakin.getDaireNo() <= 0) {
            System.out.println("HATA: Daire numarası 0 veya negatif olamaz!");
            return;
        }

        LogManager.logYaz("Servis Katmanı: ID " + sakin.getId() + " için güncelleme işlemi başlatıldı.");
        sakinRepository.sakinGuncelle(sakin);
    }
}
