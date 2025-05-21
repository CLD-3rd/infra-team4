CREATE DATABASE IF NOT EXISTS infra;
USE infra;

CREATE TABLE IF NOT EXISTS room (
  room_id INT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(50) NOT NULL
);

INSERT INTO room (room_number) VALUES
  ('101'),
  ('102'),
  ('103');
