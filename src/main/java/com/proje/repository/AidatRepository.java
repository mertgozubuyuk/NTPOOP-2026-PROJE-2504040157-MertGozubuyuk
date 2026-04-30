package com.proje.repository;

import com.proje.model.Aidat;
import com.proje.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AidatRepository {

    //Aidat ekle metodu
    public void aidatEkle(Aidat aidat){
        String sql = "INSERT INTO  aidatlar (sakin_id, miktar, ay, odendi_mi) VALUES(?, ?, ?, ?)";

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
        String sql = "SELECT a.id, s.ad, s.soyad, a.miktar, a.ay " +
                "FROM aidatlar a " +
                "JOIN sakinler s ON a.sakin_id = s.id " +
                "WHERE a.odendi_mi = false";

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        var rs = pstmt.executeQuery()) {
            System.out.println("\n--- BORCU OLAN SAKİNLER LİSTESİ ---");
            while (rs.next()){
                System.out.println("ID: " + rs.getInt("id")+
                        " | Sakin: " + rs.getString("ad") + " " + rs.getString("soyad") +
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

    //Toplu aidat ekleme metodu
    public void topluAidatTanımlama(double miktar , String ay){
        //Miktar Kontrolü
        if(miktar<=0){
            System.out.println("HATA: Aidat miktarı 0 veya negatif olamaz");
            return;
        }

        if (ay == null || ay.trim().isEmpty()){
            System.out.println("HATA: Ay bilgisi boş bırakılamaz!");
            return;
        }

        //Önce veritabanındaki sakinlerin idlerini alırız
        String sakinleriGetirSql = "SELECT id FROM sakinler";

        //Her bir sakin için aidat tablosuna borç kaydedecek SQL
        String aidatEkleSql = "INSERT INTO aidatlar(sakin_id, miktar, ay, odendi_mi) VALUES(?, ?, ?, false)";

        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement sakinStmt = conn.prepareStatement(sakinleriGetirSql);
             ResultSet rs = sakinStmt.executeQuery()){
            int sayac = 0;

            //While döngüsü ile tüm sakinleri tek tek döndürüyoruz
            while (rs.next()){
                int sakinId = rs.getInt("id");

                //Sıradaki her sakin için bir aidat borcu oluşturuyoruz
                try(PreparedStatement aidatStmt = conn.prepareStatement(aidatEkleSql)){
                    aidatStmt.setInt(1, sakinId);
                    aidatStmt.setDouble(2, miktar);
                    aidatStmt.setString(3, ay);

                    aidatStmt.executeUpdate();
                    sayac++;
                }
            }
            System.out.println("Sistem: " + sayac + " adet sakine aidat borcu başarıyla eklendi.");
        }catch (SQLException e){
            System.out.println("Hata oluştu: " + e.getMessage());
        }

    }

    //Özet Raporlama Metodu
    public void finansalOzetRaporu(){
        //odendi_mi durumuna göre gruplayıp toplamları alan SQL
        String sqlOdendi = "SELECT SUM(miktar) FROM aidatlar WHERE odendi_mi = true";
        String sqlBekleyen = "SELECT SUM(miktar) FROM aidatlar WHERE odendi_mi = false";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt1 = conn.prepareStatement(sqlOdendi);
        PreparedStatement pstmt2 = conn.prepareStatement(sqlBekleyen)){
            double toplamTahsilat=0;
            double toplamBekleyen=0;

            //Tahsil edilenleri alma
            var rs1 = pstmt1.executeQuery();
            if (rs1.next()){
                toplamTahsilat=rs1.getDouble(1);
            }

            //Bekleyenleri alma
            var rs2 = pstmt2.executeQuery();
            if(rs2.next()){
                toplamBekleyen = rs2.getDouble(1);
            }

            System.out.println("\n-----FİNANSAL ÖZET RAPORU-----");
            System.out.println("Tahsil Edilen Toplam : " + toplamTahsilat + " TL");
            System.out.println("Bekleyen Toplam Alacak : " + toplamBekleyen + " TL");
            System.out.println("Genel Toplam (Bütçe) : " + (toplamTahsilat + toplamBekleyen) + " TL");
            System.out.println("----------------------------------");
        }catch (SQLException e){
            System.out.println("Rapor oluştururken hata: " + e.getMessage());
        }
    }

    //Ödenmiş ve ödenmememiş aidat listeleme metodu
    public void daireGecmisiListele(int daireNo){
        String sql = "SELECT s.ad, s.soyad, a.miktar, a.ay, a.odendi_mi " +
                     "FROM aidatlar a " +
                     "JOIN sakinler s ON a.sakin_id = s.id " +
                     "WHERE s.daire_no = ? " +
                     "ORDER BY a.id DESC";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,daireNo);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- DAİRE NO: " + daireNo + " HESAP DÖKÜMÜ ---");
            boolean veriVarMi = false;

            while (rs.next()){
                veriVarMi = true;
                String durum = rs.getBoolean("odendi_mi") ? "[ÖDENDİ]" : "[BORÇ]";
                System.out.println(durum + "Ay: " + rs.getString("ay") +
                        " | Tutar: " + rs.getDouble("miktar") + " TL");
            }

            if (!veriVarMi){
                System.out.println("---Bu daire numarasına ait kayıt bulunamadı.");
            }

        }catch (SQLException e){
            System.out.println("Sorgulama hatası: " + e.getMessage());
        }
    }

}
