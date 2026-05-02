import com.proje.model.Sakin;
import com.proje.repository.AidatRepository;
import com.proje.repository.SakinRepository;
import com.proje.util.LogManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LogManager.logYaz("Uygulama kullanıcı tarafından başlatıldı.");
        SakinRepository repo = new SakinRepository();
        Scanner scanner = new Scanner(System.in);
        AidatRepository aidatRepo = new AidatRepository();
        int secim = -1;

        System.out.println("--- Apartman Yönetim Sistemine Hoş Geldiniz ---");

        while (secim != 0) {
            System.out.println("\n1- Sakin Ekle");
            System.out.println("2- Sakinleri Listele");
            System.out.println("3- Sakin Sil (ID ile)");
            System.out.println("4- Toplu Aidat Tanımla (OTOMATİK)");
            System.out.println("5- Borçlu sakinleri listele");
            System.out.println("6- Aidat ödemesi yap (ID ile)");
            System.out.println("7- Finansal Özet Raporu Göster");
            System.out.println("8- Daire No ile Geçmiş Sorgula");
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

                case 4:
                    System.out.println("Aidat Miktarı : ");
                    double miktar = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Ay : ");
                    String ay = scanner.nextLine();

                    aidatRepo.topluAidatTanımlama(miktar, ay);
                    break;

                case 5:
                    aidatRepo.borcluSakinleriListele();
                    break;

                case 6:
                    System.out.print("Ödemesi yapılan Aidat ID'sini giriniz: ");
                    int aidatId = scanner.nextInt();
                    aidatRepo.aidatOde(aidatId);
                    break;

                case 7:
                    aidatRepo.finansalOzetRaporu();
                    break;

                case 8:
                    try {
                        System.out.println("Sorgulamak istediğiniz daire numarasını giriniz: ");
                        int dNo = scanner.nextInt();
                        aidatRepo.daireGecmisiListele(dNo);
                    }catch (Exception e){
                        System.out.println("HATA: Lütfen sadece sayısal bir daire numarası giriniz!");
                        scanner.nextLine();//Scanner içindeki hatalı veriyi temizler
                    }
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