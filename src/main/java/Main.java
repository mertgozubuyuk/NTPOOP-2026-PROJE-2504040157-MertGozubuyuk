
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // resources klasörünün altındaki ana_ekran.fxml dosyasını yüklüyoruz
            Parent root = FXMLLoader.load(getClass().getResource("/ana_ekran.fxml"));

            // Pencere başlığı ve boyutu
            primaryStage.setTitle("Apartman Yönetim Sistemi v1.0");
            primaryStage.setScene(new Scene(root, 600, 400));

            // Pencereyi ekranda göster
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("FXML dosyası yüklenirken bir hata oluştu!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // JavaFX uygulamasını başlatan komut
        launch(args);
    }
}