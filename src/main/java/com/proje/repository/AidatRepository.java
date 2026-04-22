package com.proje.repository;

import com.proje.model.Aidat;
import com.proje.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AidatRepository {

    //Aidat ekle metodu
    public void aidatEkle(Aidat aidat){
        String sql = "INSERT TO aidatlar (sakin_id, miktar, ay, odendi_mi) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,aidat.getSakinId());
            pstmt.setDouble(2,aidat.getMiktar());
            pstmt.setString(3,aidat.getAy());
            pstmt.setBoolean(4,aidat.isOdendiMi());

            pstmt.executeUpdate();
            System.out.println("Aidat başarıyla tamamlandı!");
        }catch (SQLException e){
            System.out.println("Aidat eklenirken hata oluştu: " + e.getMessage());
        }

    }

    //Borçlu sakinleri listeleme metodu
    public void borcluSakinleriListele(){

        // Burada SQL JOIN kullanıyoruz: Aidatlar tablosu ile Sakinler tablosunu birleştirme işlemi gerçekleştiriyoruz
        String sql = "SELECT s.ad, s.soyad, a.miktar, a.ay " +
                "FROM aidatlar a " +
                "JOIN sakinler s ON a.sakin_id = s.id " +
                "WHERE a.odendi_mi = false";

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        var rs = pstmt.executeQuery()) {
            System.out.println("\n--- BORCU OLAN SAKİNLER LİSTESİ ---");
            while (rs.next()){
                System.out.println("Sakin: " + rs.getString("ad") + " " + rs.getString("soyad") +
                        " | Borç: " + rs.getDouble("miktar") + " TL" +
                        " | Ay:  " + rs.getString("ay"));
            }
        }catch (SQLException e){
            System.out.println("Borçlu listesi çekilirken hata: " + e.getMessage());
        }
    }

    //Aidat ödeme metodu
    public  void aidatOde(int aidatId){
        String sql = "UPDATE aidatlar SET odendi_mi = true WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,aidatId);
            int etkilenenSatir = pstmt.executeUpdate();

            if(etkilenenSatir>0){
                System.out.println(aidatId + " umaralı aidat başarıyla ödendi olarak işaretlendi.");
            }else{
                System.out.println("Hata: Bu ID ile bir aidat kaydı bulunamadı");
            }

        }catch (SQLException e){
            System.out.println("Ödeme işlemi sırasında hata: "+e.getMessage());
        }
    }

}
