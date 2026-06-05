package com.proje.repository;

import com.proje.model.Sakin;
import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;

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
        if(sakin.getAd().trim().isEmpty() || sakin.getSoyad().trim().isEmpty()){
            System.out.println("HATA: Sakin adı ve soyadı boş bırakılamaz");
            return;
        }
        if (sakin.getDaireNo()<=0){
            System.out.println("HATA: Geçersiz daire numarası!");
            return;
        }

        String sql = "INSERT INTO sakinler (ad, soyad, daire_no) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, sakin.getAd());
            pstmt.setString(2, sakin.getSoyad());
            pstmt.setInt(3, sakin.getDaireNo());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int yeniSakinId = rs.getInt(1);

                // Kullanicilar tablosuna otomatik ekle
                String kullaniciSql = "INSERT INTO kullanicilar (kullanici_adi, sifre, rol, sakin_id) " +
                        "VALUES (?, '1234', 'sakin', ?)";
                try (PreparedStatement kPstmt = conn.prepareStatement(kullaniciSql)) {

                    kPstmt.setString(1, "daire" + sakin.getDaireNo());
                    kPstmt.setInt(2, yeniSakinId);
                    kPstmt.executeUpdate();
                    System.out.println("Kullanıcı hesabı otomatik oluşturuldu: daire" + sakin.getDaireNo());
                }
            }

            System.out.println("✅ " + sakin.getAd() + " isimli sakin başarıyla eklendi.");

        } catch (SQLException e) {
            System.out.println("❌ Ekleme hatası: " + e.getMessage());
            LogManager.logYaz("KRİTİK HATA: " + e.getMessage());
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
            LogManager.logYaz("KRİTİK HATA: " + e.getMessage());
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
            LogManager.logYaz("KRİTİK HATA: " + e.getMessage());
        }
    }

    // Sakin güncelleme metodu
    public void sakinGuncelle(Sakin sakin) {
        // ID kontrolü (Gelen nesnenin veritabanında bir karşılığı olmalı)
        if (sakin.getId() <= 0) {
            System.out.println("❌ Hata: Geçersiz ID ile güncelleme yapılamaz!");
            return;
        }

        String sql = "UPDATE sakinler SET ad = ?, soyad = ?, daire_no = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sakin.getAd());
            pstmt.setString(2, sakin.getSoyad());
            pstmt.setInt(3, sakin.getDaireNo());
            pstmt.setInt(4, sakin.getId()); // Hangi ID'ye sahip satır değişecek?

            int etkilenenSatir = pstmt.executeUpdate();

            if (etkilenenSatir > 0) {
                System.out.println("✅ ID: " + sakin.getId() + " olan sakin bilgileri veritabanında güncellendi.");
            } else {
                System.out.println("❓ Güncellenecek kayıt bulunamadı.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Güncelleme hatası: " + e.getMessage());
            LogManager.logYaz("KRİTİK HATA: " + e.getMessage());
        }
    }

}