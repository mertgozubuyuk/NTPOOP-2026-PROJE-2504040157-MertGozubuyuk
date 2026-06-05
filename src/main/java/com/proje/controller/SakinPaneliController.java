package com.proje.controller;

import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Optional;

public class SakinPaneliController {

    @FXML private Label lblHosGeldiniz;
    @FXML private TableView<AidatSatir> aidatTablosu;
    @FXML private TableColumn<AidatSatir, String> colAy;
    @FXML private TableColumn<AidatSatir, String> colTutar;
    @FXML private TableColumn<AidatSatir, String> colDurum;
    @FXML private Button odemeButonu;

    private int sakinId;

    @FXML
    public void initialize() {
        colAy.setCellValueFactory(new PropertyValueFactory<>("ay"));
        colTutar.setCellValueFactory(new PropertyValueFactory<>("tutar"));
        colDurum.setCellValueFactory(new PropertyValueFactory<>("durum"));

        odemeButonu.setDisable(true);
        aidatTablosu.getSelectionModel().selectedItemProperty()
                .addListener((obs, eski, yeni) -> {
                    odemeButonu.setDisable(yeni == null || yeni.getDurum().equals("ÖDENDİ"));
                });
    }

    public void sakinIdAta(int sakinId) {
        this.sakinId = sakinId;
        sakinBilgisiYukle();
        aidatlariYukle();
    }

    private void sakinBilgisiYukle() {
        String sql = "SELECT ad, soyad, daire_no FROM sakinler WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sakinId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                lblHosGeldiniz.setText("Hoş Geldiniz, " +
                        rs.getString("ad") + " " + rs.getString("soyad") +
                        " (Daire " + rs.getInt("daire_no") + ")");
            }
        } catch (SQLException e) {
            LogManager.logYaz("HATA: Sakin bilgisi yüklenemedi.");
        }
    }

    private void aidatlariYukle() {
        ObservableList<AidatSatir> liste = FXCollections.observableArrayList();

        String sql = "SELECT id, miktar, ay, odendi_mi FROM aidatlar " +
                "WHERE sakin_id = ? ORDER BY id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sakinId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String durum = rs.getBoolean("odendi_mi") ? "ÖDENDİ" : "BORÇ";
                liste.add(new AidatSatir(
                        rs.getInt("id"),
                        rs.getString("ay"),
                        rs.getDouble("miktar") + " TL",
                        durum
                ));
            }
            aidatTablosu.setItems(liste);

        } catch (SQLException e) {
            LogManager.logYaz("HATA: Aidat listesi yüklenemedi.");
        }
    }

    @FXML
    private void handleOdemeYap() {
        AidatSatir secilen = aidatTablosu.getSelectionModel().getSelectedItem();

        if (secilen == null || secilen.getDurum().equals("ÖDENDİ")) {
            return;
        }

        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setTitle("Ödeme Onayı");
        onay.setContentText(secilen.getAy() + " dönemi " +
                secilen.getTutar() + " ödenecek. Onaylıyor musunuz?");

        Optional<ButtonType> sonuc = onay.showAndWait();

        if (sonuc.isPresent() && sonuc.get() == ButtonType.OK) {
            String sql = "UPDATE aidatlar SET odendi_mi = true WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, secilen.getAidatId());
                pstmt.executeUpdate();

                Alert basari = new Alert(Alert.AlertType.INFORMATION);
                basari.setTitle("Başarılı");
                basari.setContentText("Ödeme başarıyla kaydedildi.");
                basari.showAndWait();

                aidatlariYukle();

            } catch (SQLException e) {
                LogManager.logYaz("HATA: Ödeme yapılamadı.");
            }
        }
    }

    public static class AidatSatir {
        private final int aidatId;
        private final String ay;
        private final String tutar;
        private final String durum;

        public AidatSatir(int aidatId, String ay, String tutar, String durum) {
            this.aidatId = aidatId;
            this.ay = ay;
            this.tutar = tutar;
            this.durum = durum;
        }

        public int getAidatId() { return aidatId; }
        public String getAy() { return ay; }
        public String getTutar() { return tutar; }
        public String getDurum() { return durum; }
    }
}