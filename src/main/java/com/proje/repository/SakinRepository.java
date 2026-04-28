package com.proje.repository;

import com.proje.model.Sakin;
import com.proje.util.DatabaseManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement; // Ekleme metodu için lazım olacak
import java.util.ArrayList;
import java.util.List;

public class SakinRepository {

    //Sakin ekle metodu
    public void sakinEkle(Sakin sakin) {
        //İsim ve Soyisim boş mu kontrolü
        if(sakin.getAd().trim().isEmpty() || sakin.getSoyad().trim().isEmpty()){
            System.out.println("HATA: Sakin adı ve soyadı boş bırakılamaz");
            return;
        }

        //Daire no kontrolü
        if (sakin.getDaireNo()<=0){
            System.out.println("HATA: Geçersiz daire numarası!");
            return;
        }

        // ID kısmını boş bırakıyoruz çünkü veritabanında 'SERIAL' (otomatik artan) yaptık
        String sql = "INSERT INTO sakinler (ad, soyad, daire_no) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Soru işaretlerini sakin nesnesinden gelen verilerle dolduruyoruz
            pstmt.setString(1, sakin.getAd());
            pstmt.setString(2, sakin.getSoyad());
            pstmt.setInt(3, sakin.getDaireNo());

            pstmt.executeUpdate(); // Sorguyu çalıştır ve kaydet
            System.out.println("✅ " + sakin.getAd() + " isimli sakin başarıyla eklendi.");

        } catch (SQLException e) {
            System.out.println("❌ Ekleme hatası: " + e.getMessage());
        }
    }

    //Listeleme
    public List<Sakin> tumSakinleriGetir() {
        List<Sakin> liste = new ArrayList<>();
        String sql = "SELECT * FROM sakinler";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sakin s = new Sakin(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getInt("daire_no")
                );
                liste.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Listeleme hatası: " + e.getMessage());
        }
        return liste;
    }

    //Sakin silme metodu
    public void sakinSil(int id) {
        String sql = "DELETE FROM sakinler WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int etkilenenSatir = pstmt.executeUpdate();

            if (etkilenenSatir > 0) {
                System.out.println("✅ ID: " + id + " olan sakin silindi.");
            } else {
                System.out.println("❓ Bu ID ile bir kayıt bulunamadı.");
            }
        } catch (SQLException e) {
            System.out.println("Silme hatası: " + e.getMessage());
        }
    }

}