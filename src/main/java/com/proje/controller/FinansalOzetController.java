package com.proje.controller;

import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinansalOzetController {

    @FXML private Label lblTahsilat;
    @FXML private Label lblBekleyen;
    @FXML private Label lblToplam;

    @FXML
    public void initialize() {
        String sqlOdendi = "SELECT SUM(miktar) FROM aidatlar WHERE odendi_mi = true";
        String sqlBekleyen = "SELECT SUM(miktar) FROM aidatlar WHERE odendi_mi = false";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sqlOdendi);
             PreparedStatement pstmt2 = conn.prepareStatement(sqlBekleyen)) {

            double tahsilat = 0;
            double bekleyen = 0;

            ResultSet rs1 = pstmt1.executeQuery();
            if (rs1.next()) tahsilat = rs1.getDouble(1);

            ResultSet rs2 = pstmt2.executeQuery();
            if (rs2.next()) bekleyen = rs2.getDouble(1);

            // Bunu ekle
            System.out.println("Tahsilat: " + tahsilat);
            System.out.println("Bekleyen: " + bekleyen);

            lblTahsilat.setText("Tahsil Edilen:    " + tahsilat + " TL");
            lblBekleyen.setText("Bekleyen:          " + bekleyen + " TL");
            lblToplam.setText("Genel Toplam:   " + (tahsilat + bekleyen) + " TL");

        } catch (SQLException e) {
            e.printStackTrace();
            LogManager.logYaz("HATA: Finansal özet alınamadı.");
        }
    }
}