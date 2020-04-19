CREATE DATABASE social_network;

CREATE USER 'social-network-admin' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON social_network.* TO 'social-network-admin';
