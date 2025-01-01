CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE `category` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL,
                            `description` text,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
                           `category_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `product_code` (`product_code`),
                           KEY `idx_product_category` (`category_id`),
                           CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `customer` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL,
                            `address` varchar(255) DEFAULT NULL,
                            `email` varchar(255) DEFAULT NULL,
                            `phone` varchar(50) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `email` (`email`),
                            UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `supplier` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL,
                            `address` varchar(255) DEFAULT NULL,
                            `email` varchar(255) DEFAULT NULL,
                            `phone` varchar(50) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `email` (`email`),
                            UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `customer_id` bigint NOT NULL,
                          `product_id` bigint NOT NULL,
                          `quantity` int NOT NULL,
                          `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `customer_id` (`customer_id`),
                          KEY `product_id` (`product_id`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
                          CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `purchase` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `supplier_id` bigint NOT NULL,
                            `product_id` bigint NOT NULL,
                            `quantity` int NOT NULL,
                            `purchase_date` datetime DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `supplier_id` (`supplier_id`),
                            KEY `product_id` (`product_id`),
                            CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
                            CONSTRAINT `purchase_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `role` enum('ADMIN','USER','MODERATOR') NOT NULL DEFAULT 'USER',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert dummy data into category
INSERT INTO `category` (`name`, `description`) VALUES
('Electronics', 'Devices and gadgets such as smartphones, laptops, and tablets'),
('Audio', 'Audio devices including speakers, headphones, and microphones'),
('Furniture', 'Office and home furniture such as desks, chairs, and cabinets'),
('Accessories', 'Computer and mobile accessories such as keyboards, mice, and chargers'),
('Home Appliances', 'Household appliances such as refrigerators, washing machines, and air conditioners');

INSERT INTO `product` (`product_code`, `product_name`, `brand`, `quantity`, `cost_price`, `sell_price`, `category_id`) VALUES
('PRD001', 'Laptop XPS', 'Dell', 50, 500.00, 750.00, 1),
('PRD002', 'Galaxy S21', 'Samsung', 100, 300.00, 500.00, 1),
('PRD003', 'Office Chair', 'Ikea', 25, 40.00, 80.00, 3),
('PRD004', 'Mac Book', 'Apple', 50, 500.00, 700.00, 1),
('PRD005', 'Microwave', 'MicroY', 100, 200.00, 300.00, 5),
('PRD006', 'Headphones D45', 'AudioGear', 75, 50.00, 80.00, 2),
('PRD007', 'Fridge 200', 'LG', 30, 150.00, 250.00, 5),
('PRD008', 'Keyboard RGB', 'InputPro', 60, 20.00, 40.00, 4);

-- Insert dummy data into `customer`
INSERT INTO `customer` (`name`, `address`, `email`, `phone`) VALUES
('John Doe', '123 Main St', 'john.doe@example.com', '1234567890'),
('Jane Smith', '456 Oak St', 'jane.smith@example.com', '0987654321'),
('Alice Johnson', '789 Pine St', 'alice.johnson@example.com', '1122334455'),
('Bob Brown', '321 Elm St', 'bob.brown@example.com', '6677889900'),
('Emma Wilson', '654 Maple St', 'emma.wilson@example.com', '4433221100');

-- Insert dummy data into `supplier`
INSERT INTO `supplier` (`name`, `address`, `email`, `phone`) VALUES
('Supplier A', '100 Industrial Rd', 'contact@suppliera.com', '1234509876'),
('Supplier B', '200 Corporate Blvd', 'info@supplierb.com', '0987612345'),
('Supplier C', '300 Business Ln', 'support@supplierc.com', '2233445566'),
('Supplier D', '400 Tech Park', 'sales@supplierd.com', '6677885544'),
('Supplier E', '500 Commerce Dr', 'service@suppliere.com', '5544332211');

-- Insert dummy data into `orders`
INSERT INTO `orders` (`customer_id`, `product_id`, `quantity`, `order_date`) VALUES
(1, 1, 20, '2024-10-30 10:00:00'),
(2, 3, 10, '2024-11-15 14:30:00'),
(3, 2, 40, '2024-11-24 16:45:00'),
(4, 4, 10, '2024-12-11 11:15:00'),
(2, 5, 30, '2024-12-23 13:50:00');

-- Insert dummy data into `purchase`
INSERT INTO `purchase` (`supplier_id`, `product_id`, `quantity`, `purchase_date`) VALUES
(1, 1, 20, '2024-10-23 09:00:00'),
(2, 3, 15, '2024-10-29 12:30:00'),
(3, 2, 10, '2024-11-08 14:20:00'),
(4, 4, 25, '2024-11-18 15:10:00'),
(1, 5, 30, '2024-11-25 10:45:00');

-- Insert dummy data into user
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'ADMIN'),
('user1', 'user123', 'USER'),
('mod', 'mod123', 'MODERATOR');