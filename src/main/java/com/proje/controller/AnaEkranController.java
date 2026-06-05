package com.proje.controller;

import com.proje.util.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnaEkranController {

    @FXML private TextField txtKullaniciAdi;
    @FXML private PasswordField txtSifre;

    @FXML
    private void handleGirisYap(ActionEvent event) {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = txtSifre.getText().trim();

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            hataMesaji("Kullanıcı adı ve şifre boş bırakılamaz.");
            return;
        }

        String sql = "SELECT rol, sakin_id FROM kullanicilar " +
                "WHERE kullanici_adi = ? AND sifre = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                int sakinId = rs.getInt("sakin_id");

                Node source = (Node) event.getSource();
                Stage oldStage = (Stage) source.getScene().getWindow();
                oldStage.close();

                if (rol.equals("admin")) {
                    Parent root = FXMLLoader.load(
                            getClass().getResource("/yonetim_paneli.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Admin Paneli");
                    stage.setScene(new Scene(root));
                    stage.show();

                } else if (rol.equals("sakin")) {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/sakin_paneli.fxml"));
                    Parent root = loader.load();

                    SakinPaneliController controller = loader.getController();
                    controller.sakinIdAta(sakinId);

                    Stage stage = new Stage();
                    stage.setTitle("Sakin Paneli");
                    stage.setScene(new Scene(root));
                    stage.show();
                }

            } else {
                hataMesaji("Kullanıcı adı veya şifre hatalı.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            hataMesaji("Giriş yapılırken hata oluştu.");
        }
    }

    private void hataMesaji(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}