import com.proje.model.Sakin;
import com.proje.repository.SakinRepository;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SakinRepository repo = new SakinRepository();
        Scanner scanner = new Scanner(System.in);
        int secim = -1;

        System.out.println("--- Apartman Yönetim Sistemine Hoş Geldiniz ---");

        while (secim != 0) {
            System.out.println("\n1- Sakin Ekle");
            System.out.println("2- Sakinleri Listele");
            System.out.println("3- Sakin Sil");
            System.out.println("0- Çıkış");
            System.out.print("Seçiminiz: ");
            secim = scanner.nextInt();
            scanner.nextLine(); // Sayıdan sonra enter karakterini temizlemek için

            switch (secim) {
                case 1:
                    System.out.print("Ad: ");
                    String ad = scanner.nextLine();
                    System.out.print("Soyad: ");
                    String soyad = scanner.nextLine();
                    System.out.print("Daire No: ");
                    int daireNo = scanner.nextInt();

                    Sakin yeniSakin = new Sakin(0, ad, soyad, daireNo);
                    repo.sakinEkle(yeniSakin);
                    break;

                case 2:
                    System.out.println("\n--- Mevcut Sakinler ---");
                    List<Sakin> sakinler = repo.tumSakinleriGetir();
                    for (Sakin s : sakinler) {
                        System.out.println("ID: " + s.getId() + " | " + s.getAd() + " " + s.getSoyad() + " | Daire: " + s.getDaireNo());
                    }
                    break;

                case 3:
                    System.out.print("Silinecek Sakin ID: ");
                    int silinecekId = scanner.nextInt();
                    repo.sakinSil(silinecekId);
                    break;

                case 0:
                    System.out.println("Sistemden çıkılıyor...");
                    break;

                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
        scanner.close();
    }
}