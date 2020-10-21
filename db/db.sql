
CREATE DATABASE IF NOT EXISTS config;

use config;

CREATE TABLE IF NOT EXISTS properties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    application VARCHAR(255) NOT NULL,
    profile VARCHAR(255) NOT NULL,
    propkey VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL
);