package com.proje.controller;

import com.proje.model.Aidat;
import com.proje.model.Sakin;
import com.proje.repository.AidatRepository;
import com.proje.repository.SakinRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.util.List;

public class TekAidatEkleController {

    @FXML private ComboBox<Sakin> cmbSakin;
    @FXML private TextField txtMiktar;
    @FXML private TextField txtAy;

    private AidatRepository aidatRepository = new AidatRepository();
    private SakinRepository sakinRepository = new SakinRepository();

    @FXML
    public void initialize() {
        // Sakinleri veritabanından çek ve ComboBox'a doldur
        List<Sakin> sakinler = sakinRepository.tumSakinleriGetir();
        cmbSakin.setItems(FXCollections.observableArrayList(sakinler));

        // ComboBox'ta isim göster
        cmbSakin.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Sakin sakin, boolean empty) {
                super.updateItem(sakin, empty);
                setText(empty || sakin == null ? null : sakin.getAdSoyad());
            }
        });

        // Seçili olan da isim göstersin
        cmbSakin.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Sakin sakin, boolean empty) {
                super.updateItem(sakin, empty);
                setText(empty || sakin == null ? null : sakin.getAdSoyad());
            }
        });
    }

    @FXML
    private void handleAidatEkle() {
        Sakin seciliSakin = cmbSakin.getValue();
        String miktarStr = txtMiktar.getText().trim();
        String ay = txtAy.getText().trim();

        // Boş alan kontrolü
        if (seciliSakin == null || miktarStr.isEmpty() || ay.isEmpty()) {
            uyariGoster("Uyarı", "Lütfen tüm alanları doldurun.");
            return;
        }

        try {
            double miktar = Double.parseDouble(miktarStr);

            if (miktar <= 0) {
                uyariGoster("Uyarı", "Miktar 0'dan büyük olmalıdır.");
                return;
            }

            Aidat yeniAidat = new Aidat(seciliSakin.getId(), miktar, ay, false);
            aidatRepository.aidatEkle(yeniAidat);
            basariGoster("Başarılı", seciliSakin.getAdSoyad() + " için aidat başarıyla eklendi.");

            // Alanları temizle
            cmbSakin.setValue(null);
            txtMiktar.clear();
            txtAy.clear();

        } catch (NumberFormatException e) {
            uyariGoster("Hata", "Miktar sayısal olmalıdır.");
        }
    }

    private void uyariGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(baslik);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    private void basariGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}