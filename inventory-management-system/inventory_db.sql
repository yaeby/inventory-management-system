CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE `product` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `product_code` varchar(255) NOT NULL,
                           `product_name` varchar(255) NOT NULL,
                           `brand` varchar(255) DEFAULT NULL,
                           `quantity` int NOT NULL,
                           `cost_price` decimal(10,2) NOT NULL,
                           `sell_price` decimal(10,2) NOT NULL,
                           `total_cost` decimal(15,2) DEFAULT NULL,
                           `total_revenue` decimal(15,2) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `product_code` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;