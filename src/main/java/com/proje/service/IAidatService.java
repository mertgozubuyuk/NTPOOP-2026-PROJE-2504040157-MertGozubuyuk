package com.proje.service;

import java.util.List;

//IAidatService: Finansal işlemler için standart metod listesi

public interface IAidatService {

    //Toplu aidat tanımlama kuralı
    void topluAidatOlustur(double miktar, String ay);

    //Borçlu sakinleri listeleme kuralı
    void borclulariGoster();


    //Finansal rapor kuralı
    void raporuHazirla();

    //Daire geçmişi sorgulama kuralı
    void daireDokumuGetir(int daireNo);
}
