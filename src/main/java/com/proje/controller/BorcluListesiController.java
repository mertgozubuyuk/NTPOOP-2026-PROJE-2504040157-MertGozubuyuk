package com.proje.controller;

import com.proje.model.Borclu;
import com.proje.repository.AidatRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class BorcluListesiController {

    @FXML private TableView<Borclu> borcluTablosu;
    @FXML private TableColumn<Borclu, String> colAdSoyad;
    @FXML private TableColumn<Borclu, Double> colMiktar;
    @FXML private TableColumn<Borclu, String> colAy;
    @FXML private Button odemeYapButonu;

    private AidatRepository aidatRepository = new AidatRepository();

    @FXML
    public void initialize() {
        colAdSoyad.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        colMiktar.setCellValueFactory(new PropertyValueFactory<>("miktar"));
        colAy.setCellValueFactory(new PropertyValueFactory<>("ay"));

        odemeYapButonu.setDisable(true);
        borcluTablosu.getSelectionModel().selectedItemProperty().addListener(
                (obs, eskiSecim, yeniSecim) -> odemeYapButonu.setDisable(yeniSecim == null)
        );

        tabloyuDoldur();
    }

    private void tabloyuDoldur() {
        List<Borclu> borclular = aidatRepository.borcluListesiniGetir();
        ObservableList<Borclu> data = FXCollections.observableArrayList(borclular);
        borcluTablosu.setItems(data);
    }

    @FXML
    private void handleOdemeYap() {
        Borclu secilen = borcluTablosu.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            Alert uyari = new Alert(Alert.AlertType.WARNING);
            uyari.setTitle("Uyarı");
            uyari.setContentText("Lütfen önce bir borçlu seçin.");
            uyari.showAndWait();
            return;
        }

        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setTitle("Ödeme Onayı");
        onay.setHeaderText("Ödeme işlemi onaylanacak");
        onay.setContentText(
                secilen.getAdSoyad() + " kişisinin " +
                        secilen.getAy() + " dönemine ait " +
                        secilen.getMiktar() + " TL borcu ödenecek.\n\nOnaylıyor musunuz?"
        );

        Optional<ButtonType> sonuc = onay.showAndWait();

        if (sonuc.isPresent() && sonuc.get() == ButtonType.OK) {
            aidatRepository.aidatOde(secilen.getId(), secilen.getSakinId(), secilen.getMiktar());
            Alert basari = new Alert(Alert.AlertType.INFORMATION);
            basari.setTitle("Başarılı");
            basari.setContentText("Ödeme başarıyla kaydedildi.");
            basari.showAndWait();

            tabloyuDoldur();
        }
    }
}