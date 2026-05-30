package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AnaEkranController {

    @FXML
    private void handleGirisYap(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/yonetim_paneli.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Apartman Yönetim Sistemi - Ana Panel");
            stage.setScene(new Scene(root));

            Node source = (Node) event.getSource();
            Stage oldStage = (Stage) source.getScene().getWindow();
            oldStage.close();

            stage.show();

            System.out.println("🚀 Giriş başarılı! Yönetim paneline geçildi.");
        } catch (IOException e) {
            System.err.println("Hata: FXML dosyası yüklenemedi! " + e.getMessage());
        }
    }
}
