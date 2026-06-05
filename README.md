# Apartman Yönetim Sistemi

Nesne Yönelimli Programlama dersi kapsamında Java ile geliştirilmiş, PostgreSQL veritabanı destekli apartman yönetim uygulaması.

**Geliştirici:** Mert Gözübüyük  
**Öğrenci No:** 2504040157

---

## Kullanılan Teknolojiler

- **Java:** JDK 25
- **Veritabanı:** PostgreSQL
- **Veritabanı Erişimi:** JDBC
- **Arayüz:** JavaFX 17.0.2
- **Build Aracı:** Maven

---

## Kurulum

### 1. Veritabanı Oluşturma

PostgreSQL kurulu olmalıdır. Varsayılan port: `5432`

```sql
-- pgAdmin veya psql üzerinden çalıştırın
\i docs/schema.sql
```

### 2. Bağlantı Ayarları

`src/main/java/com/proje/util/DatabaseManager.java` dosyasında kendi bilgilerinizi girin:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
private static final String USER = "postgres";
private static final String PASSWORD = "sifreniz";
```

### 3. Projeyi Derleme

```bash
mvn clean compile
```

### 4. Projeyi Çalıştırma

```bash
IntelliJ IDEA üzerinden `Launcher.java` sınıfını çalıştırın.
```

---

## Özellikler

### Sakin Yönetimi (CRUD)
- Yeni sakin ekleme
- Sakin bilgilerini güncelleme
- Sakin silme
- Sakin listeleme

### Aidat İşlemleri
- Toplu aidat tanımlama (tüm sakinlere)
- Tek sakin için aidat ekleme
- Borçlu sakinleri görüntüleme
- Aidat ödemesi alma

### Raporlar
- Finansal özet raporu (tahsilat / bekleyen / toplam)
- Daire geçmişi sorgulama
- Tüm tahsilat geçmişi

### Dışa Aktarma
- Sakin listesi CSV
- Ödeme raporu CSV

### Giriş Sistemi
- Admin girişi → Yönetim paneline yönlendirir
- Sakin girişi → Kendi aidat ekranına yönlendirir

| Kullanıcı | Şifre | Rol |
|---|---|---|
| admin | 1234 | Admin |
| daire1 | 1234 | Sakin |

---

## ER Diyagramı

```
sakinler
├── id (PK, SERIAL)
├── ad (VARCHAR)
├── soyad (VARCHAR)
└── daire_no (INT, UNIQUE)

aidatlar
├── id (PK, SERIAL)
├── sakin_id (FK → sakinler.id)
├── miktar (DECIMAL)
├── ay (VARCHAR)
└── odendi_mi (BOOLEAN)

odemeler
├── id (PK, SERIAL)
├── sakin_id (FK → sakinler.id)
├── aidat_id (FK → aidatlar.id)
├── tutar (DECIMAL)
└── odeme_tarihi (TIMESTAMP)
```

**İlişkiler:**
- `sakinler` → `aidatlar` : 1-N (bir sakin, birden fazla aidatı olabilir)
- `sakinler` → `odemeler` : 1-N (bir sakin, birden fazla ödeme yapabilir)
- `aidatlar` → `odemeler` : 1-1 (bir aidat, bir ödemeye karşılık gelir)

---

## Bilinen Sorunlar / Gelecek Geliştirmeler

- Veritabanı bağlantı bilgileri şu an kod içinde sabit; `.env` veya `db.properties` dosyasına taşınabilir
- Aidat gecikmesi için otomatik hatırlatma sistemi geliştirilebilir
- E-posta bilgilendirme sistemi eklenebilir.
- Şuan için tüm sakinler aynı şifre ile giriş yapmakta bunun için telefon numarasına gelen şifre ile giriş yapma seçeneği oluşturulabilir.