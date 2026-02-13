-- Fresh one-time schema creation for BookMyEvent
-- WARNING: This script DROPS and RECREATES the `bookmyevent` database. Run only on a fresh/dev instance.

DROP DATABASE IF EXISTS `bookmyevent`;
CREATE DATABASE `bookmyevent` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'dev'@'%' IDENTIFIED BY 'dev123';
GRANT ALL PRIVILEGES ON `bookmyevent`.* TO 'dev'@'%';
-- Also create local variants so connections via socket/localhost work
CREATE USER IF NOT EXISTS 'dev'@'localhost' IDENTIFIED BY 'dev123';
GRANT ALL PRIVILEGES ON `bookmyevent`.* TO 'dev'@'localhost';
CREATE USER IF NOT EXISTS 'dev'@'127.0.0.1' IDENTIFIED BY 'dev123';
GRANT ALL PRIVILEGES ON `bookmyevent`.* TO 'dev'@'127.0.0.1';
FLUSH PRIVILEGES;

USE `bookmyevent`;

-- USERS & ROLES
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(255),
  status ENUM('ACTIVE','SUSPENDED','DELETED') DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE roles (
  id SMALLINT PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id SMALLINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_userroles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_userroles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ORGANIZER PROFILE
CREATE TABLE organizers (
  id BIGINT PRIMARY KEY,
  org_name VARCHAR(255) NOT NULL,
  contact_email VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_organizers_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- VENUE & EVENTS
CREATE TABLE venues (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  address VARCHAR(512),
  capacity INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE events (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  organizer_id BIGINT NOT NULL,
  venue_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  category VARCHAR(100),
  start_time DATETIME NOT NULL,
  end_time DATETIME,
  status ENUM('DRAFT','PUBLISHED','CANCELLED') DEFAULT 'DRAFT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_events_organizer FOREIGN KEY (organizer_id) REFERENCES organizers(id) ON DELETE CASCADE,
  CONSTRAINT fk_events_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_events_start ON events (start_time);

-- SECTIONS, SEATS, PRICING
CREATE TABLE event_sections (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  capacity INT NOT NULL,
  notes TEXT,
  CONSTRAINT fk_eventsections_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE seats (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  section_id BIGINT,
  seat_row VARCHAR(10),
  seat_number VARCHAR(10),
  seat_code VARCHAR(64) NOT NULL,
  status ENUM('AVAILABLE','BLOCKED','BOOKED') DEFAULT 'AVAILABLE',
  version BIGINT DEFAULT 0,
  UNIQUE KEY ux_event_seatcode (event_id, seat_code),
  CONSTRAINT fk_seats_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  CONSTRAINT fk_seats_section FOREIGN KEY (section_id) REFERENCES event_sections(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_seats_event_section_status ON seats (event_id, section_id, status);

CREATE TABLE pricing_tiers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  section_id BIGINT,
  name VARCHAR(100),
  currency CHAR(3) DEFAULT 'INR',
  price_cents INT NOT NULL,
  price_type ENUM('REGULAR','CONCESSION','PROMO') DEFAULT 'REGULAR',
  CONSTRAINT fk_pricing_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  CONSTRAINT fk_pricing_section FOREIGN KEY (section_id) REFERENCES event_sections(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OFFERS/PROMOTIONS
CREATE TABLE offers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(64) UNIQUE,
  event_id BIGINT,
  description TEXT,
  discount_type ENUM('PERCENT','FLAT') NOT NULL,
  discount_value DECIMAL(10,2) NOT NULL,
  valid_from DATETIME,
  valid_until DATETIME,
  max_uses INT DEFAULT NULL,
  per_user_limit INT DEFAULT 1,
  active BOOLEAN DEFAULT TRUE,
  CONSTRAINT fk_offers_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_offers_code ON offers (code);

-- HOLDS, BOOKINGS, PAYMENTS
CREATE TABLE seat_holds (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  hold_token VARCHAR(128) NOT NULL,
  user_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  seat_id BIGINT NOT NULL,
  status ENUM('HOLD','RELEASED','CONSUMED') DEFAULT 'HOLD',
  expires_at DATETIME NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_seatholds_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_seatholds_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  CONSTRAINT fk_seatholds_seat FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_seatholds_holdtoken ON seat_holds (hold_token);
CREATE INDEX idx_seatholds_expires ON seat_holds (expires_at);

CREATE TABLE bookings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_ref VARCHAR(64) UNIQUE NOT NULL,
  user_id BIGINT NOT NULL,
  organizer_id BIGINT,
  event_id BIGINT NOT NULL,
  total_amount_cents INT NOT NULL,
  currency CHAR(3) DEFAULT 'INR',
  status ENUM('PENDING','CONFIRMED','CANCELLED','FAILED') DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
  CONSTRAINT fk_bookings_organizer FOREIGN KEY (organizer_id) REFERENCES organizers(id) ON DELETE SET NULL,
  CONSTRAINT fk_bookings_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_bookings_user_status ON bookings (user_id, status);

CREATE TABLE booking_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id BIGINT NOT NULL,
  seat_id BIGINT NOT NULL,
  price_cents INT NOT NULL,
  status ENUM('RESERVED','CANCELLED','REFUNDED') DEFAULT 'RESERVED',
  CONSTRAINT fk_bookingitems_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
  CONSTRAINT fk_bookingitems_seat FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id BIGINT NOT NULL,
  provider VARCHAR(100),
  provider_txn_id VARCHAR(255),
  amount_cents INT NOT NULL,
  status ENUM('INITIATED','SUCCESS','FAILED') DEFAULT 'INITIATED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance
CREATE INDEX idx_seats_event_section ON seats (event_id, section_id);
CREATE INDEX idx_venues_city ON venues (city);

-- Optionally insert default roles and a dev user (password stored as plaintext for demo only)
INSERT INTO roles (id, name) VALUES (1, 'USER'), (2, 'ORGANIZER'), (3, 'ADMIN');
INSERT INTO users (username, email, password_hash, full_name) VALUES ('dev', 'dev@example.com', 'dev123', 'Dev User');
INSERT INTO user_roles (user_id, role_id) VALUES (LAST_INSERT_ID(), 1);

-- Done
