package com.proje.controller;

import com.proje.util.DatabaseManager;
import com.proje.util.LogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class TahsilatGecmisiController {

    @FXML private TableView<TahsilatKaydi> tahsilatTablosu;
    @FXML private TableColumn<TahsilatKaydi, String> colMakbuz;
    @FXML private TableColumn<TahsilatKaydi, String> colSakin;
    @FXML private TableColumn<TahsilatKaydi, String> colDonem;
    @FXML private TableColumn<TahsilatKaydi, String> colTutar;
    @FXML private TableColumn<TahsilatKaydi, String> colTarih;

    @FXML
    public void initialize() {
        colMakbuz.setCellValueFactory(new PropertyValueFactory<>("makbuz"));
        colSakin.setCellValueFactory(new PropertyValueFactory<>("sakin"));
        colDonem.setCellValueFactory(new PropertyValueFactory<>("donem"));
        colTutar.setCellValueFactory(new PropertyValueFactory<>("tutar"));
        colTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));

        tabloyuDoldur();
    }

    private void tabloyuDoldur() {
        ObservableList<TahsilatKaydi> liste = FXCollections.observableArrayList();

        String sql = "SELECT o.id, s.ad, s.soyad, a.ay, o.tutar, o.odeme_tarihi " +
                "FROM odemeler o " +
                "JOIN sakinler s ON o.sakin_id = s.id " +
                "JOIN aidatlar a ON o.aidat_id = a.id " +
                "ORDER BY o.odeme_tarihi DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(new TahsilatKaydi(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("ad") + " " + rs.getString("soyad"),
                        rs.getString("ay"),
                        rs.getDouble("tutar") + " TL",
                        rs.getTimestamp("odeme_tarihi").toString()
                ));
            }

            tahsilatTablosu.setItems(liste);

        } catch (SQLException e) {
            LogManager.logYaz("HATA: Tahsilat geçmişi alınamadı.");
        }
    }

    public static class TahsilatKaydi {
        private final String makbuz;
        private final String sakin;
        private final String donem;
        private final String tutar;
        private final String tarih;

        public TahsilatKaydi(String makbuz, String sakin, String donem, String tutar, String tarih) {
            this.makbuz = makbuz;
            this.sakin = sakin;
            this.donem = donem;
            this.tutar = tutar;
            this.tarih = tarih;
        }

        public String getMakbuz() { return makbuz; }
        public String getSakin() { return sakin; }
        public String getDonem() { return donem; }
        public String getTutar() { return tutar; }
        public String getTarih() { return tarih; }
    }
}
