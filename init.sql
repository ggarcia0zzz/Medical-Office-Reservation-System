-- ─────────────────────────────────────────────
--  Medical Office Reservation System — Seed Data
-- ─────────────────────────────────────────────

-- Specialties
INSERT INTO specialties (name, description) VALUES
                                                ('General Medicine',   'Primary care and general health consultations'),
                                                ('Psychology',         'Mental health evaluation and therapy'),
                                                ('Physiotherapy',      'Physical rehabilitation and movement therapy'),
                                                ('Nutrition',          'Dietary planning and nutritional counseling'),
                                                ('Cardiology',         'Heart and cardiovascular system care');

-- Appointment Types
INSERT INTO appointment_types (name, duration_minutes, description) VALUES
                                                                        ('General Consultation',  30,  'Standard checkup with a general medicine doctor'),
                                                                        ('Follow-up',             20,  'Short follow-up visit after a previous appointment'),
                                                                        ('Therapy Session',       60,  'Full psychological or physiotherapy session'),
                                                                        ('Nutritional Assessment',45,  'Initial dietary evaluation and plan creation'),
                                                                        ('Cardiac Evaluation',    45,  'Heart health assessment and ECG review');

-- Offices
INSERT INTO offices (name, location, opening_hour, closing_hour, status, created_at, updated_at) VALUES
                                                                                                     ('Office A', 'Building 1 - Floor 1', '08:00:00', '17:00:00', 'AVAILABLE', NOW(), NOW()),
                                                                                                     ('Office B', 'Building 1 - Floor 2', '08:00:00', '17:00:00', 'AVAILABLE', NOW(), NOW()),
                                                                                                     ('Office C', 'Building 2 - Floor 1', '07:00:00', '16:00:00', 'AVAILABLE', NOW(), NOW()),
                                                                                                     ('Office D', 'Building 2 - Floor 3', '09:00:00', '18:00:00', 'AVAILABLE', NOW(), NOW()),
                                                                                                     ('Office E', 'Building 3 - Floor 1', '08:00:00', '17:00:00', 'MAINTENANCE', NOW(), NOW());

-- Doctors (no user_id — register through auth endpoint if needed)
INSERT INTO doctors (full_name, email, license_number, is_active, specialty_id, created_at, updated_at) VALUES
                                                                                                            ('Dr. Andrés Mora',    'andres.mora@clinic.com',    'LIC-001', true, 1, NOW(), NOW()),
                                                                                                            ('Dra. Paula Vega',    'paula.vega@clinic.com',     'LIC-002', true, 2, NOW(), NOW()),
                                                                                                            ('Dra. Clara Ríos',    'clara.rios@clinic.com',     'LIC-003', true, 3, NOW(), NOW()),
                                                                                                            ('Dr. Luis Herrera',   'luis.herrera@clinic.com',   'LIC-004', true, 4, NOW(), NOW()),
                                                                                                            ('Dra. Sofía Blanco',  'sofia.blanco@clinic.com',   'LIC-005', true, 5, NOW(), NOW());

-- Doctor Schedules
INSERT INTO doctor_schedules (day_of_week, start_time, end_time, doctor_id) VALUES
                                                                                ('MONDAY',    '08:00:00', '12:00:00', 1),
                                                                                ('WEDNESDAY', '08:00:00', '12:00:00', 1),
                                                                                ('FRIDAY',    '08:00:00', '12:00:00', 1),
                                                                                ('TUESDAY',   '09:00:00', '13:00:00', 2),
                                                                                ('THURSDAY',  '09:00:00', '13:00:00', 2),
                                                                                ('MONDAY',    '13:00:00', '17:00:00', 3),
                                                                                ('WEDNESDAY', '13:00:00', '17:00:00', 3),
                                                                                ('TUESDAY',   '08:00:00', '16:00:00', 4),
                                                                                ('THURSDAY',  '08:00:00', '16:00:00', 4),
                                                                                ('FRIDAY',    '09:00:00', '17:00:00', 5);

-- Patients (no user_id — register through auth endpoint if needed)
INSERT INTO patients (full_name, email, phone, status, created_at, updated_at) VALUES
                                                                                   ('Ana Torres',    'ana.torres@uni.edu.co',    '312 456 7890', 'ACTIVE',   NOW(), NOW()),
                                                                                   ('Carlos Ruiz',   'carlos.ruiz@uni.edu.co',   '313 987 6543', 'ACTIVE',   NOW(), NOW()),
                                                                                   ('María López',   'maria.lopez@uni.edu.co',   '314 555 5555', 'INACTIVE', NOW(), NOW()),
                                                                                   ('Juan Pérez',    'juan.perez@uni.edu.co',    '315 222 2222', 'ACTIVE',   NOW(), NOW()),
                                                                                   ('Sofía Díaz',    'sofia.diaz@uni.edu.co',    '316 333 3333', 'ACTIVE',   NOW(), NOW());

-- Appointments
INSERT INTO appointments (start_at, end_at, status, patient_id, doctor_id, office_id, "appointment_type_id", created_at, updated_at) VALUES
                                                                                                                                        ('2026-06-02 09:00:00', '2026-06-02 09:30:00', 'CONFIRMED',  1, 1, 1, 1, NOW(), NOW()),
                                                                                                                                        ('2026-06-02 10:30:00', '2026-06-02 11:00:00', 'SCHEDULED',  2, 2, 2, 3, NOW(), NOW()),
                                                                                                                                        ('2026-06-02 09:00:00', '2026-06-02 09:30:00', 'SCHEDULED',  3, 3, 3, 1, NOW(), NOW()),
                                                                                                                                        ('2026-06-03 09:00:00', '2026-06-03 09:45:00', 'SCHEDULED',  4, 4, 4, 4, NOW(), NOW()),
                                                                                                                                        ('2026-06-03 10:00:00', '2026-06-03 10:30:00', 'CANCELLED',  5, 5, 1, 1, NOW(), NOW()),
                                                                                                                                        ('2026-06-04 08:00:00', '2026-06-04 09:00:00', 'COMPLETED',  1, 3, 2, 3, NOW(), NOW()),
                                                                                                                                        ('2026-06-04 13:00:00', '2026-06-04 13:30:00', 'NO_SHOW',    2, 1, 1, 2, NOW(), NOW()),
                                                                                                                                        ('2026-06-05 09:00:00', '2026-06-05 09:45:00', 'SCHEDULED',  3, 5, 3, 5, NOW(), NOW());