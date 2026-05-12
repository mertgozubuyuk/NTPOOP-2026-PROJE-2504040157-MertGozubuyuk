package com.proje.repository;

import com.proje.model.Odeme;
import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OdemeRepository {
    public void odemeKaydet(Odeme odeme){
        String sql = "INSERT INTO odemeler(sakin_id, aidat_id, tutar, odeme_tarihi) VALUES (?, ?, ?, ?)";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, odeme.getSakinId());
            pstmt.setInt(2, odeme.getAidatId());
            pstmt.setDouble(3, odeme.getTutar());
            pstmt.setTimestamp(4, Timestamp.valueOf(odeme.getOdemeTarihi()));

            pstmt.executeUpdate();
            LogManager.logYaz("VERİTABANI: Ödemeler tablosuna yeni kayıt eklendi. Aidat Id: " + odeme.getAidatId());

        }catch (SQLException e){
            System.out.println("Ödeme kaydedilirken hata: " + e.getMessage());
            LogManager.logYaz("KRİTİK HATA (OdemeRepo): " + e.getMessage());
        }
    }
}
