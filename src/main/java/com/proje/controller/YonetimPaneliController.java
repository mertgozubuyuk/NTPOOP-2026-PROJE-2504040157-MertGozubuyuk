package com.proje.controller;

import com.proje.model.Sakin;
import com.proje.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class YonetimPaneliController {

    private ExportService exportService = new ExportService();
    private IAidatService aidatService = new AidatService();

    @FXML private TableView<Sakin> sakinTablosu;
    @FXML private TableColumn<Sakin, Integer> colDaire;
    @FXML private TableColumn<Sakin, String> colAdSoyad;

    @FXML private TextField txtDaireNo;
    @FXML private TextField txtAd;
    @FXML private TextField txtSoyad;


    private ISakinService sakinService = new SakinService();



    private void tabloyuGuncelle() {
        List<Sakin> sakins = sakinService.sakinleriGetir();
        if (sakins != null) {
            ObservableList<Sakin> liste = FXCollections.observableArrayList(sakins);
            sakinTablosu.setItems(liste);
        }
    }


    @FXML
    private void handleEkle() {
        try {
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            int daireNo = Integer.parseInt(txtDaireNo.getText());

            Sakin yeniSakin = new Sakin(0, ad, soyad, daireNo);

            sakinService.sakinKaydet(yeniSakin);

            txtAd.clear();
            txtSoyad.clear();
            txtDaireNo.clear();
            tabloyuGuncelle();

        } catch (NumberFormatException e) {
            System.err.println("Hata: Daire numarası sayı olmalıdır!");
        }
    }

    @FXML
    public void initialize() {
        colDaire.setCellValueFactory(new PropertyValueFactory<>("daireNo"));
        colAdSoyad.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));

        // TABLODAN SATIR SEÇİLİNCE KUTULARI DOLDURMA (Kullanıcı dostu arayüz)
        sakinTablosu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDaireNo.setText(String.valueOf(newSelection.getDaireNo()));
                txtAd.setText(newSelection.getAd());
                txtSoyad.setText(newSelection.getSoyad());
            }
        });

        tabloyuGuncelle();
    }


    @FXML
    private void handleSil() {
        Sakin seciliSakin = sakinTablosu.getSelectionModel().getSelectedItem();

        if (seciliSakin != null) {
            // Eski kodundaki gibi ID üzerinden servisi tetikliyoruz
            sakinService.sakinSil(seciliSakin.getId());

            System.out.println("🗑️ Sakin silindi: " + seciliSakin.getAdSoyad());

            // Kutuları temizle ve tabloyu yenile
            txtDaireNo.clear();
            txtAd.clear();
            txtSoyad.clear();
            tabloyuGuncelle();
        } else {
            System.err.println("❌ Lütfen silmek için tablodan bir sakin seçin!");
        }
    }

    // Güncelleme İşlemi (CRUD - Update)
    @FXML
    private void handleGuncelle() {
        Sakin seciliSakin = sakinTablosu.getSelectionModel().getSelectedItem();

        if (seciliSakin != null) {
            try {
                // 1. Kutulardan yeni verileri alıp doğrudan seçili nesneye basıyoruz (ID korunuyor)
                seciliSakin.setDaireNo(Integer.parseInt(txtDaireNo.getText()));
                seciliSakin.setAd(txtAd.getText());
                seciliSakin.setSoyad(txtSoyad.getText());

                // 2. Yeni yazdığımız servis metodunu çağırıyoruz
                sakinService.sakinGuncelle(seciliSakin);

                // 3. Kutuları temizle ve tabloyu yenile
                txtDaireNo.clear();
                txtAd.clear();
                txtSoyad.clear();

                tabloyuGuncelle();
                System.out.println("🔄 Güncelleme arayüze başarıyla yansıtıldı!");

            } catch (NumberFormatException e) {
                System.err.println("❌ Hata: Daire numarası sayı olmalıdır!");
            }
        } else {
            System.err.println("❌ Lütfen güncellemek için tablodan bir sakin seçin!");
        }
    }

    @FXML
    private void handleExportSakinler() {
        try {
            exportService.sakinleriCsvYap();
            bilgiMesajiGoster("Başarılı", "Sakin listesi başarıyla CSV olarak kaydedildi.");
        } catch (Exception e) {
            hataMesajiGoster("Hata", "Dosya oluşturulurken bir hata oluştu!");
        }
    }

    @FXML
    private void handleExportOdemeler() {
        try {
            exportService.odemeleriCsvYap();
            bilgiMesajiGoster("Başarılı", "Ödeme raporu başarıyla CSV olarak kaydedildi.");
        } catch (Exception e) {
            hataMesajiGoster("Hata", "Rapor oluşturulamadı!");
        }
    }
    

    private void bilgiMesajiGoster(String baslik, String icerik) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(icerik);
        alert.showAndWait();
    }

    private void hataMesajiGoster(String baslik, String icerik) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(icerik);
        alert.showAndWait();
    }

    @FXML
    private void handleAidatOlustur() {
        javafx.scene.control.TextInputDialog miktarDialog = new javafx.scene.control.TextInputDialog("500");
        miktarDialog.setTitle("Toplu Aidat Tanımla");
        miktarDialog.setHeaderText("Tüm sakinler için aidat miktarı giriniz.");
        miktarDialog.setContentText("Miktar (TL):");

        miktarDialog.showAndWait().ifPresent(miktarStr -> {
            try {
                double miktar = Double.parseDouble(miktarStr);

                javafx.scene.control.TextInputDialog ayDialog = new javafx.scene.control.TextInputDialog("Mayıs");
                ayDialog.setTitle("Ay Bilgisi");
                ayDialog.setHeaderText("Hangi ay için aidat tanımlanıyor?");
                ayDialog.setContentText("Ay:");

                ayDialog.showAndWait().ifPresent(ay -> {
                    aidatService.topluAidatOlustur(miktar, ay);
                    bilgiMesajiGoster("İşlem Başarılı", ay + " ayı için herkese " + miktar + " TL aidat tanımlandı.");
                });

            } catch (NumberFormatException e) {
                hataMesajiGoster("Hata", "Lütfen geçerli bir sayısal miktar giriniz!");
            }
        });
    }

    @FXML
    private void handleBorclulariGoster() {

        try {
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(getClass().getResource("/borclu_listesi.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Gecikmiş Aidat Borçları");
            stage.setScene(scene);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Ana pencereyi kilitle
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Pencere açılırken hata: " + e.getMessage());
        }
    }

    @FXML
    private void handleAidatOdemesiYap() {
        try {
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/borclu_listesi.fxml")
            );
            javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Aidat Ödemesi Yap");
            stage.setScene(scene);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            hataMesajiGoster("Hata", "Aidat ödeme penceresi açılamadı!");
        }
    }

    @FXML
    private void handleFinansalOzet() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/finansal_ozet.fxml"));
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Finansal Özet Raporu");
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            hataMesajiGoster("Hata", "Finansal özet açılamadı!");
        }
    }

    @FXML
    private void handleDaireGecmisi() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/daire_gecmisi.fxml"));
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Daire Geçmişi Sorgula");
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            hataMesajiGoster("Hata", "Daire geçmişi açılamadı!");
        }
    }

    @FXML
    private void handleTahsilatGecmisi() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/tahsilat_gecmisi.fxml"));
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Tüm Tahsilat Geçmişi");
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);


            
            stage.show();
        } catch (Exception e) {
            hataMesajiGoster("Hata", "Tahsilat geçmişi açılamadı!");
        }
    }

    @FXML
    private void handleTekAidatEkle() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/tek_aidat_ekle.fxml"));
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Tek Aidat Ekle");
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            hataMesajiGoster("Hata", "Aidat ekleme penceresi açılamadı!");
        }
    }
}