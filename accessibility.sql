show databases;
CREATE DATABASE IF NOT EXISTS campus_access_db;
USE campus_access_db;

-- Users table to store student information
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_student_number (student_number),
	device_type VARCHAR(50) DEFAULT 'None',
    avoid_stairs BOOLEAN DEFAULT FALSE,
    prefer_ramps BOOLEAN DEFAULT FALSE,
    min_path_width_cm INT DEFAULT 90,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
);

-- User login sessions
CREATE TABLE sessions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_session_token (session_token)
);

-- Campus locations table
CREATE TABLE locations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    coordinates VARCHAR(50), -- Could store as "x,y" for simplicity
    type ENUM('Building', 'Lab', 'Toilet', 'Facility', 'Other') NOT NULL,
    INDEX idx_location_name (name)
);

-- Routes between locations
CREATE TABLE routes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    from_location_id INT NOT NULL,
    to_location_id INT NOT NULL,
    directions TEXT NOT NULL,
    estimated_time INT, -- in minutes
    accessibility_notes TEXT,
    FOREIGN KEY (from_location_id) REFERENCES locations(id) ON DELETE CASCADE,
    FOREIGN KEY (to_location_id) REFERENCES locations(id) ON DELETE CASCADE,
    INDEX idx_route (from_location_id, to_location_id)
);

-- User favorites/saved locations
CREATE TABLE user_favorites (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    location_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE,
    UNIQUE(user_id, location_id),
    INDEX idx_user_favorites (user_id)
);
CREATE TABLE IF NOT EXISTS events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    building VARCHAR(100) NOT NULL,
    room VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    accessibility VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS event_locations (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    building_code VARCHAR(10),
    room_number VARCHAR(10),
    map_x INT,
    map_y INT,
    accessibility_notes TEXT,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);
-- Insert sample locations
INSERT INTO locations (name, description, coordinates, type) VALUES
('Engineering Building', 'Main engineering faculty building', '50,50', 'Building'),
('Health Sciences Building', 'Health sciences faculty', '200,50', 'Building'),
('Business Building', 'Business and management studies', '350,50', 'Building'),
('Library', 'Main campus library', '50,200', 'Facility'),
('Food Court', 'Campus dining area', '250,200', 'Facility'),
('Computer Lab', 'General computer lab', '450,200', 'Lab'),
('Toilets', 'Restroom facilities', '300,350', 'Toilet'),
('Exit', 'Campus exit', '500,400', 'Facility');

-- Insert sample routes
INSERT INTO routes (from_location_id, to_location_id, directions, estimated_time, accessibility_notes) VALUES
(1, 4, 'Walk straight from Engineering Building, turn right at the hallway, Library is on your left', 5, 'Wheelchair accessible route available'),
(4, 5, 'Exit Library, walk straight for 100m, Food Court is on your right', 3, 'Elevator available near stairs'),
(2, 6, 'From Health Sciences, take the stairs to 2nd floor, Computer Lab is at the end of corridor', 7, 'Ramp available as alternative to stairs');
