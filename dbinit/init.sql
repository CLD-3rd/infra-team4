DROP DATABASE IF EXISTS `infra`;

CREATE DATABASE `infra`;
USE `infra`;

CREATE TABLE IF NOT EXISTS `room` (
  room_id int AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(50) NOT NULL
);

INSERT INTO room (room_number) VALUES
  ('101'),
  ('102'),
  ('103'),
  ('104'),
  ('105');
