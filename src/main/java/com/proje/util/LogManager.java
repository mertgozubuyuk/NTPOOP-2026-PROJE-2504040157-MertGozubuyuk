package com.proje.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Log manger:Uygulama içindeki önemli olayları ve hataları bir metin dosyasına kaydeder
public class LogManager {
    //Kayıtların tutulacağı dosya adı
    private static final String FILE_NAME = "uygulama_kayitlari.txt";

    /**
     * Mesajı tarih ve saatle birlikte dosyaya yazar.
     * @param mesaj Kaydedilecek bilgi veya hata metni
     */
    public static void logYaz(String mesaj){
        // Şu anki zamanı al ve formatla (Örn: 2024-05-20 14:30:05)
        LocalDateTime simdi = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String zamanDamgasi = simdi.format(format);

        try(FileWriter fw = new FileWriter(FILE_NAME, true);
        PrintWriter pw = new PrintWriter(fw)){

            pw.println("[" + zamanDamgasi + "] " + mesaj);
        }catch (IOException e){
            System.err.println("Log dosyasısına yazılamadı: " +e.getMessage());
        }
    }
}
