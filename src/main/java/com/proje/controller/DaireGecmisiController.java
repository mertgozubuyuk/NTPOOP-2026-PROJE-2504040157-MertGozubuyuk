package com.proje.controller;

import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class DaireGecmisiController {

    @FXML private TextField txtDaireNo;
    @FXML private TableView<DaireKaydi> gecmisTablosu;
    @FXML private TableColumn<DaireKaydi, String> colAy;
    @FXML private TableColumn<DaireKaydi, String> colTutar;
    @FXML private TableColumn<DaireKaydi, String> colDurum;

    @FXML
    public void initialize() {
        colAy.setCellValueFactory(new PropertyValueFactory<>("ay"));
        colTutar.setCellValueFactory(new PropertyValueFactory<>("tutar"));
        colDurum.setCellValueFactory(new PropertyValueFactory<>("durum"));
    }

    @FXML
    private void handleSorgula() {
        String daireNoStr = txtDaireNo.getText().trim();
        if (daireNoStr.isEmpty()) {
            Alert uyari = new Alert(Alert.AlertType.WARNING);
            uyari.setTitle("Uyarı");
            uyari.setContentText("Lütfen bir daire numarası girin.");
            uyari.showAndWait();
            return;
        }

        try {
            int daireNo = Integer.parseInt(daireNoStr);
            ObservableList<DaireKaydi> liste = FXCollections.observableArrayList();

            String sql = "SELECT a.ay, a.miktar, a.odendi_mi " +
                    "FROM aidatlar a " +
                    "JOIN sakinler s ON a.sakin_id = s.id " +
                    "WHERE s.daire_no = ? ORDER BY a.id DESC";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, daireNo);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String durum = rs.getBoolean("odendi_mi") ? "ÖDENDİ" : "BORÇ";
                    liste.add(new DaireKaydi(
                            rs.getString("ay"),
                            rs.getDouble("miktar") + " TL",
                            durum
                    ));
                }
            }

            gecmisTablosu.setItems(liste);

            if (liste.isEmpty()) {
                Alert bilgi = new Alert(Alert.AlertType.INFORMATION);
                bilgi.setTitle("Sonuç");
                bilgi.setContentText("Bu daire numarasına ait kayıt bulunamadı.");
                bilgi.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert hata = new Alert(Alert.AlertType.ERROR);
            hata.setTitle("Hata");
            hata.setContentText("Daire numarası sayı olmalıdır.");
            hata.showAndWait();
        } catch (SQLException e) {
            LogManager.logYaz("HATA: Daire geçmişi alınamadı.");
        }
    }

    public static class DaireKaydi {
        private final String ay;
        private final String tutar;
        private final String durum;

        public DaireKaydi(String ay, String tutar, String durum) {
            this.ay = ay;
            this.tutar = tutar;
            this.durum = durum;
        }

        public String getAy() { return ay; }
        public String getTutar() { return tutar; }
        public String getDurum() { return durum; }
    }
}