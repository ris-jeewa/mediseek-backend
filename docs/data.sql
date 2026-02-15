-- MediSeek sample data (MySQL)
-- Run after schema.sql

-- Hospitals
INSERT INTO hospital (name, type, rating, open_hours, map, telephone) VALUES
('Colombo General Hospital', 'Government', 4.2, '08:00 - 20:00', 'https://maps.google.com/?q=6.9271,79.8612', '+94112345678'),
('Nawaloka Hospital', 'Private', 4.5, '00:00 - 23:59', 'https://maps.google.com/?q=6.9285,79.8618', '+94112345679');

-- Doctors
INSERT INTO doctor (name, specialty, experience) VALUES
('Dr. Anura Perera', 'General Physician', 15),
('Dr. Nimal Silva', 'Cardiologist', 20),
('Dr. Kamala Fernando', 'Pediatrician', 10);

-- Doctor–Hospital links
INSERT INTO doctor_hospital (doctor_id, hospital_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2),
(3, 2);

-- Pharmacies
INSERT INTO Pharmacy (name, registration_number, contact_number, is_active) VALUES
('HealthPlus Pharmacy', 'REG-HP001', '+94112345001', TRUE),
('City Pharmacy', 'REG-CP002', '+94112345002', TRUE);

-- Pharmacy branches (pharmacy_id 1 and 2)
INSERT INTO Pharmacy_Branch (pharmacy_id, branch_name, address, city, latitude, longitude, maps_url, contact_number, opening_hours, is_active) VALUES
(1, 'HealthPlus Colombo', '123 Galle Rd, Colombo 3', 'Colombo', 6.9271, 79.8612, 'https://maps.google.com/', '+94112345001', '08:00 - 22:00', TRUE),
(1, 'HealthPlus Kandy', '45 Temple St, Kandy', 'Kandy', 7.2931, 80.6340, 'https://maps.google.com/', '+94812345002', '08:00 - 20:00', TRUE),
(2, 'City Pharmacy Main', '78 Main St, Colombo 2', 'Colombo', 6.9285, 79.8618, 'https://maps.google.com/', '+94112345002', '09:00 - 21:00', TRUE);

-- Medicines
INSERT INTO medicine (generic_name, brand, dosage_form, strength, category, description) VALUES
('Paracetamol', 'Panadol', 'Tablet', '500mg', 'Analgesic', 'Pain relief and fever reduction'),
('Ibuprofen', 'Brufen', 'Tablet', '400mg', 'NSAID', 'Anti-inflammatory and pain relief'),
('Amoxicillin', 'Amoxil', 'Capsule', '500mg', 'Antibiotic', 'Broad-spectrum antibiotic');

-- Branch–Medicine (branch_id, medicine_id, price, stock_quantity, last_updated)
INSERT INTO Branch_Medicine (branch_id, medicine_id, price, stock_quantity, last_updated) VALUES
(1, 1, 150.00, 100, NOW()),
(1, 2, 85.00, 50, NOW()),
(1, 3, 320.00, 30, NOW()),
(2, 1, 155.00, 80, NOW()),
(2, 2, 90.00, 40, NOW()),
(3, 1, 148.00, 120, NOW()),
(3, 3, 315.00, 25, NOW());

-- Contacts (optional)
INSERT INTO contact (name, email, subject, message) VALUES
('John Doe', 'john@example.com', 'Inquiry', 'I would like to know about your services.');