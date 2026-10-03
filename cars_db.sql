-- =====================================================================
-- Full Database Migration Script for CarHub (Spring Boot 3 + Angular)
-- Database: mydatabase
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `mydatabase` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `mydatabase`;

-- 1. Drop existing tables if they exist
DROP TABLE IF EXISTS `car`;
DROP TABLE IF EXISTS `brand`;

-- 2. Create Brand Table
CREATE TABLE `brand` (
    `id_brand` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `country` VARCHAR(255),
    `description` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Create Car Table (with Foreign Key to brand)
CREATE TABLE `car` (
    `id_car` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `model` VARCHAR(255) NOT NULL,
    `price` DOUBLE NOT NULL,
    `release_date` DATE,
    `license_plate` VARCHAR(255),
    `brand_id_brand` BIGINT,
    INDEX `idx_brand_id` (`brand_id_brand`),
    CONSTRAINT `fk_car_brand` FOREIGN KEY (`brand_id_brand`) REFERENCES `brand` (`id_brand`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Insert Initial Brands
INSERT INTO `brand` (`id_brand`, `name`, `country`, `description`) VALUES
(1, 'BMW', 'Germany', 'Bayerische Motoren Werke - High performance & luxury'),
(2, 'Audi', 'Germany', 'Vorsprung durch Technik - Premium sporty sedans and SUVs'),
(3, 'Mercedes-Benz', 'Germany', 'The best or nothing - Luxury and AMG engineering'),
(4, 'Porsche', 'Germany', 'Exceptional sports cars and precision racing engineering'),
(5, 'Ferrari', 'Italy', 'Scuderia Ferrari luxury sports cars');

-- 5. Insert Initial Cars
INSERT INTO `car` (`id_car`, `model`, `price`, `release_date`, `license_plate`, `brand_id_brand`) VALUES
(1, 'M4 Competition', 89500.0, '2023-04-12', '235-TUN-4040', 1),
(2, 'RS6 Avant', 126000.0, '2022-10-18', '238-TUN-6060', 2),
(3, 'AMG GT Coupé', 145000.0, '2023-07-25', '241-TUN-7070', 3),
(4, '911 GT3 RS', 189000.0, '2024-02-14', '245-TUN-9110', 4),
(5, 'M3 Touring', 92000.0, '2023-09-01', '242-TUN-3030', 1),
(6, 'RS3 Sportback', 68000.0, '2023-01-20', '236-TUN-3333', 2),
(7, '296 GTB', 270000.0, '2026-10-03', 'FERRARI-296', 5);

COMMIT;
