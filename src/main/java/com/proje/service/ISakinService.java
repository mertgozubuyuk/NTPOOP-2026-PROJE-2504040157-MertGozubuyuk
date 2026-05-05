package com.proje.service;

import com.proje.model.Sakin;
import java.util.List;

/**
 * ISakinService: Sakin işlemleri için standart kuralların listesi.
 * Bu bir arayüzdür; metodların gövdesi ({}) burada bulunmaz.
 */

public interface  ISakinService {

    //Sakin eklemek için kural
    void sakinKaydet(Sakin sakin);

    //Tüm sakinleri listelemek için kullanılacak kural;
    List<Sakin> sakinleriGetir();

    //ID üzerinden sakin silmel için kullanılacak kural
    void sakinSil(int id);
}
