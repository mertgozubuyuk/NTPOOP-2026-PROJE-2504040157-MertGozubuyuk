# Proje Raporu — Apartman Yönetim Sistemi

**Öğrenci:** Mert Gözübüyük  
**Öğrenci No:** 2504040157  
**Ders:** Nesne Yönelimli Programlama  

---

## 1. Problem Tanımı

Apartman yöneticilerinin sakin takibi, aidat tahsilatı ve finansal raporlama işlemlerini manuel olarak yapması zaman alıcı ve hata yapmaya açık bir süreçtir. Bu proje, söz konusu işlemleri dijital ortama taşıyarak yöneticiye kullanıcı dostu bir arayüz üzerinden tüm apartman operasyonlarını yönetme imkânı sunmaktadır.

---

## 2. Gereksinimler

### Fonksiyonel Gereksinimler
- Sakin ekleme, güncelleme, silme ve listeleme
- Tüm sakinlere toplu aidat tanımlama
- Bireysel aidat ödemesi alma
- Borçlu sakinlerin listelenmesi
- Daire bazlı ödeme geçmişi sorgulama
- Finansal özet raporu görüntüleme
- CSV formatında dışa aktarma

### Teknik Gereksinimler
- Java JDK 25, JavaFX 17, PostgreSQL, JDBC, Maven
- Katmanlı mimari: model / repository / service / controller / util
- OOP prensiplerine uygunluk
- Hata yönetimi ve loglama

---

## 3. Mimari Tasarım

Proje beş katmandan oluşmaktadır:

```
com.proje
├── model/        → Veri nesneleri (Sakin, Aidat, Odeme, Borclu, User, Yonetici)
├── repository/   → JDBC ile doğrudan veritabanı işlemleri
├── service/      → İş mantığı ve validasyon kuralları
├── controller/   → JavaFX ekran kontrolcüleri
└── util/         → Yardımcı sınıflar (DatabaseManager, LogManager, ExportService)
```

Bu yapı sayesinde her katman yalnızca kendi sorumluluğunu üstlenir; veritabanı değişikliği yalnızca repository katmanını, iş kuralı değişikliği yalnızca service katmanını etkiler.

---

## 4. OOP Kullanımı

### Encapsulation (Kapsülleme)
Tüm model sınıflarında alanlar `private` olarak tanımlanmış, erişim yalnızca getter/setter metotları üzerinden sağlanmıştır.

```java
public class Sakin extends User {
    private int daireNo;
    public int getDaireNo() { return daireNo; }
    public void setDaireNo(int daireNo) { this.daireNo = daireNo; }
}
```

### Inheritance (Kalıtım)
`User` abstract sınıfı ortak alanları (`id`, `ad`, `soyad`) ve getter/setter'ları barındırmaktadır. `Sakin` ve `Yonetici` sınıfları bu sınıftan türetilmiştir.

```java
public abstract class User { ... }
public class Sakin extends User { ... }
public class Yonetici extends User { ... }
```

### Abstraction (Soyutlama)
`IAidatService`, `ISakinService` ve `IOdemeService` interface'leri iş kurallarını soyutlar; implementasyonlar bu sözleşmeleri yerine getirir.

```java
public interface ISakinService {
    void sakinKaydet(Sakin sakin);
    List<Sakin> sakinleriGetir();
    void sakinSil(int id);
    void sakinGuncelle(Sakin sakin);
}
```

### Polymorphism (Çok Biçimlilik)
`SakinService implements ISakinService` ve `AidatService implements IAidatService` yapıları ile aynı interface üzerinden farklı implementasyonlar çalıştırılabilmektedir. `YonetimPaneliController` içinde `ISakinService` tipinde referans tutulmakta, çalışma zamanında `SakinService` nesnesi kullanılmaktadır.

```java
private ISakinService sakinService = new SakinService();
private IAidatService aidatService = new AidatService();
```

---

## 5. Veritabanı Tasarımı

### Tablolar

**sakinler** — Apartman sakinlerinin kişisel bilgileri  
**aidatlar** — Sakinlere tanımlanan aylık borç kayıtları  
**odemeler** — Gerçekleştirilen ödeme işlemlerinin makbuz tablosu  

### İlişkiler
- `sakinler` → `aidatlar` : 1-N ilişkisi (bir sakin, birden fazla aidatı olabilir)
- `sakinler` → `odemeler` : 1-N ilişkisi
- `aidatlar` → `odemeler` : 1-1 ilişkisi (bir aidat ödenince bir ödeme kaydı oluşur)
- `ON DELETE CASCADE` ile bir sakin silindiğinde ilişkili tüm kayıtlar otomatik temizlenir

---

## 6. CRUD Akışları

### Sakin Ekleme
`YonetimPaneliController.handleEkle()` → `SakinService.sakinKaydet()` → validasyon → `SakinRepository.sakinEkle()` → SQL INSERT

### Sakin Güncelleme
`YonetimPaneliController.handleGuncelle()` → `SakinService.sakinGuncelle()` → validasyon → `SakinRepository.sakinGuncelle()` → SQL UPDATE

### Sakin Silme
`YonetimPaneliController.handleSil()` → `SakinService.sakinSil()` → `SakinRepository.sakinSil()` → SQL DELETE

### Aidat Ödeme
`BorcluListesiController.handleOdemeYap()` → `AidatRepository.aidatOde()` → SQL UPDATE (odendi_mi=true) → `OdemeRepository.odemeKaydet()` → SQL INSERT

### Tek Aidat Ekleme
TekAidatEkleController.handleAidatEkle() → ComboBox'tan sakin seçilir
→ AidatRepository.aidatEkle() → SQL INSERT

### Sakin Girişi ve Rol Yönetimi
AnaEkranController.handleGirisYap() → kullanicilar tablosunda
kullanici_adi ve sifre kontrolü → rol "admin" ise yönetim paneli,
rol "sakin" ise sakin paneli açılır → SakinPaneliController.sakinIdAta()
ile sakin ID aktarılır
---

## 7. Test Senaryoları

| # | Senaryo | Beklenen Sonuç | Sonuç |
|---|---------|---------------|-------|
| 1 | Geçerli bilgilerle yeni sakin ekleme | Sakin tabloya eklenir, listede görünür | ✅ Başarılı |
| 2 | Boş ad ile sakin ekleme denemesi | Hata mesajı gösterilir, kayıt yapılmaz | ✅ Başarılı |
| 3 | Tüm sakinlere toplu aidat tanımlama | Her sakin için aidatlar tablosuna kayıt eklenir | ✅ Başarılı |
| 4 | Borçlu listesinden aidat ödemesi yapma | odendi_mi=true olur, odemeler tablosuna kayıt girer, listeden kalkar | ✅ Başarılı |
| 5 | Geçersiz daire no ile geçmiş sorgulama | "Kayıt bulunamadı" uyarısı gösterilir | ✅ Başarılı |
| 6 | Finansal özet raporu görüntüleme | Tahsilat, bekleyen ve toplam doğru hesaplanır | ✅ Başarılı |
| 7 | Sakin silindiğinde ilişkili aidatlar | CASCADE ile otomatik silinir | ✅ Başarılı |
| 8 | Tek sakin için aidat ekleme | Seçilen sakine aidat borcu eklenir, borçlular listesinde görünür | ✅ Başarılı |

---

## 8. Sonuç ve Değerlendirme

Bu proje, Java ile nesne yönelimli programlama prensiplerinin gerçek bir uygulama senaryosunda nasıl kullanıldığını göstermektedir. Katmanlı mimari sayesinde kodun bakımı ve genişletilmesi kolaylaşmış; interface kullanımı ile bağımlılıklar soyutlanmıştır. JavaFX ile geliştirilen arayüz, kullanıcının tüm işlemleri grafiksel ortamda yapmasına olanak tanımaktadır.

Gelecekte eklenebilecek özellikler arasında kullanıcı giriş sistemi, otomatik gecikme faizi hesaplama ve e-posta bildirimi yer almaktadır.

---

## 9. Kaynakça

- Oracle Java Documentation: https://docs.oracle.com/en/java/
- JavaFX Documentation: https://openjfx.io/
- PostgreSQL Documentation: https://www.postgresql.org/docs/
- JDBC API Guide: https://docs.oracle.com/javase/tutorial/jdbc/
