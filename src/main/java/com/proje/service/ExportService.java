package com.proje.service;

import com.proje.util.DatabaseManager;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportService {
    public void sakinleriCsvYap() {
        String csvDosyaYolu = "sakinler_listesi.csv";
        String sql = "SELECT * FROM sakinler";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             FileWriter writer = new FileWriter(csvDosyaYolu)) {

            // CSV Başlıklarını Yazalım
            writer.append("ID,Ad,Soyad,DaireNo\n");

            while (rs.next()) {
                writer.append(String.valueOf(rs.getInt("id"))).append(",");
                writer.append(rs.getString("ad")).append(",");
                writer.append(rs.getString("soyad")).append(",");
                writer.append(String.valueOf(rs.getInt("daire_no"))).append("\n");
            }

            System.out.println("Sakin listesi başarıyla oluşturuldu: " + csvDosyaYolu);

        } catch (SQLException | IOException e) {
            System.out.println("CSV dışa aktarma hatası: " + e.getMessage());
        }
    }

    public void odemeleriCsvYap() {
        String csvDosyaYolu = "odeme_raporu.csv";
        // 3'lü JOIN kullanarak detaylı rapor alıyoruz
        String sql = "SELECT o.id, s.ad, s.soyad, a.ay, o.tutar, o.odeme_tarihi " +
                "FROM odemeler o " +
                "JOIN sakinler s ON o.sakin_id = s.id " +
                "JOIN aidatlar a ON o.aidat_id = a.id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             FileWriter writer = new FileWriter(csvDosyaYolu)) {

            writer.append("MakbuzNo,Sakin,Donem,Tutar,Tarih\n");

            while (rs.next()) {
                writer.append(String.valueOf(rs.getInt("id"))).append(",");
                writer.append(rs.getString("ad") + " " + rs.getString("soyad")).append(",");
                writer.append(rs.getString("ay")).append(",");
                writer.append(String.valueOf(rs.getDouble("tutar"))).append(",");
                writer.append(rs.getTimestamp("odeme_tarihi").toString()).append("\n");
            }

            System.out.println("Ödeme raporu başarıyla oluşturuldu: " + csvDosyaYolu);

        } catch (SQLException | IOException e) {
            System.out.println("Rapor hatası: " + e.getMessage());
        }
    }
}
