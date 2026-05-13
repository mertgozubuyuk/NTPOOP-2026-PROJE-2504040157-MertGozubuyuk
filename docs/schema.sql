-- APARTMAN YÖNETİM SİSTEMİ - VERİ TABANI ŞEMASI

- 1. SAKİNLER TABLOSU
-- Apartman sakinlerinin temel bilgilerini tutar.
CREATE TABLE sakinler (
                          id SERIAL PRIMARY KEY,
                          ad VARCHAR(50) NOT NULL,
                          soyad VARCHAR(50) NOT NULL,
                          daire_no INT NOT NULL UNIQUE
);

-- 2. AİDATLAR TABLOSU
-- Her sakin için periyodik olarak oluşturulan borçları tutar.
CREATE TABLE aidatlar (
                          id SERIAL PRIMARY KEY,
                          sakin_id INT REFERENCES sakinler(id) ON DELETE CASCADE,
                          miktar DECIMAL(10,2) NOT NULL,
                          ay VARCHAR(20) NOT NULL,
                          odendi_mi BOOLEAN DEFAULT FALSE
);

-- 3. ÖDEMELER TABLOSU (İlişkisel Makbuz Tablosu)
-- Gerçekleşen ödeme işlemlerini tarihli olarak kayıt altına alır.
CREATE TABLE odemeler (
                          id SERIAL PRIMARY KEY,
                          sakin_id INT REFERENCES sakinler(id) ON DELETE CASCADE,
                          aidat_id INT REFERENCES aidatlar(id) ON DELETE CASCADE,
                          tutar DECIMAL(10,2) NOT NULL,
                          odeme_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);