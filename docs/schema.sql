-- MediSeek schema (MySQL)
-- Run this first, then data.sql

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Branch_Medicine;
DROP TABLE IF EXISTS Pharmacy_Branch;
DROP TABLE IF EXISTS Pharmacy;
DROP TABLE IF EXISTS doctor_hospital;
DROP TABLE IF EXISTS hospital;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS medicine;
DROP TABLE IF EXISTS contact;

SET FOREIGN_KEY_CHECKS = 1;

-- Hospital
CREATE TABLE hospital (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    rating DOUBLE,
    open_hours VARCHAR(255) NOT NULL,
    map VARCHAR(255) NOT NULL,
    telephone VARCHAR(255) NOT NULL
);

-- Doctor
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialty VARCHAR(255) NOT NULL,
    experience INT NOT NULL
);

-- Doctor–Hospital (many-to-many link)
CREATE TABLE doctor_hospital (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    hospital_id BIGINT NOT NULL,
    CONSTRAINT fk_dh_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_dh_hospital FOREIGN KEY (hospital_id) REFERENCES hospital(id)
);

-- Pharmacy
CREATE TABLE Pharmacy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(255) UNIQUE,
    contact_number VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Pharmacy branch
CREATE TABLE Pharmacy_Branch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_id BIGINT NOT NULL,
    branch_name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(255),
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    maps_url TEXT,
    contact_number VARCHAR(255),
    opening_hours VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_branch_pharmacy FOREIGN KEY (pharmacy_id) REFERENCES Pharmacy(id)
);

-- Medicine
CREATE TABLE medicine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    generic_name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    dosage_form VARCHAR(255) NOT NULL,
    strength VARCHAR(255),
    category VARCHAR(255),
    description TEXT
);

-- Branch–Medicine (composite PK)
CREATE TABLE Branch_Medicine (
    branch_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    last_updated DATETIME(6) NOT NULL,
    PRIMARY KEY (branch_id, medicine_id),
    CONSTRAINT fk_bm_branch FOREIGN KEY (branch_id) REFERENCES Pharmacy_Branch(id),
    CONSTRAINT fk_bm_medicine FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

-- Contact (form submissions)
CREATE TABLE contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    subject VARCHAR(255),
    message VARCHAR(255)
);