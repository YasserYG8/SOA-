-- Initial Data for Brand
INSERT INTO brand (id_brand, name, country, description) 
VALUES (1, 'BMW', 'Germany', 'Bayerische Motoren Werke - High performance & luxury')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), description=VALUES(description);

INSERT INTO brand (id_brand, name, country, description) 
VALUES (2, 'Audi', 'Germany', 'Vorsprung durch Technik - Premium sporty sedans and SUVs')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), description=VALUES(description);

INSERT INTO brand (id_brand, name, country, description) 
VALUES (3, 'Mercedes-Benz', 'Germany', 'The best or nothing - Luxury and AMG engineering')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), description=VALUES(description);

INSERT INTO brand (id_brand, name, country, description) 
VALUES (4, 'Porsche', 'Germany', 'Exceptional sports cars and precision racing engineering')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), description=VALUES(description);

INSERT INTO brand (id_brand, name, country, description) 
VALUES (5, 'Ferrari', 'Italy', 'Scuderia Ferrari luxury sports cars')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), description=VALUES(description);

-- Initial Data for Car
INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (1, 'M4 Competition', 89500.0, '2023-04-12', '235-TUN-4040', 1)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (2, 'RS6 Avant', 126000.0, '2022-10-18', '238-TUN-6060', 2)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (3, 'AMG GT Coupé', 145000.0, '2023-07-25', '241-TUN-7070', 3)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (4, '911 GT3 RS', 189000.0, '2024-02-14', '245-TUN-9110', 4)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (5, 'M3 Touring', 92000.0, '2023-09-01', '242-TUN-3030', 1)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (6, 'RS3 Sportback', 68000.0, '2023-01-20', '236-TUN-3333', 2)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);

INSERT INTO car (id_car, model, price, release_date, license_plate, brand_id_brand)
VALUES (7, '296 GTB', 270000.0, '2026-10-03', 'FERRARI-296', 5)
ON DUPLICATE KEY UPDATE model=VALUES(model), price=VALUES(price), release_date=VALUES(release_date), license_plate=VALUES(license_plate), brand_id_brand=VALUES(brand_id_brand);
