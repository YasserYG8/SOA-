-- Schema definition for cars_db
CREATE TABLE IF NOT EXISTS brand (
    id_brand BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    description VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS car (
    id_car BIGINT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    release_date DATE,
    license_plate VARCHAR(255),
    brand_id_brand BIGINT,
    CONSTRAINT fk_car_brand FOREIGN KEY (brand_id_brand) REFERENCES brand(id_brand) ON DELETE CASCADE
) ENGINE=InnoDB;
