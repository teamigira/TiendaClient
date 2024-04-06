/**
 * Author:  ashiraf
 * Created: 20 Aug 2020
 */
-- -- V1__initialise_database.sql
-- DROP SCHEMA IF EXISTS Tienda;

-- CREATE SCHEMA Tienda;
CREATE TABLE IF NOT EXISTS production_products(
  product_id bigint  NOT NULL AUTO_INCREMENT, 
  code VARCHAR(255) NULL, 
  ean_gtin VARCHAR(255) NULL, 
  location VARCHAR(255) NULL, 
  unit_of_measure VARCHAR(255) NULL, 
  product_name VARCHAR(255) NULL, 
  brand_id VARCHAR(255) NULL, 
  category_id VARCHAR(255) NULL, 
  expiry_date VARCHAR(255) NULL, 
  list_price DECIMAL(10, 2) NOT NULL, 
  retail_price DECIMAL(10, 2) NULL, 
  comments VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS sales_customers(
  customer_id INT PRIMARY KEY, 
  first_name VARCHAR(255) NULL, 
  last_name VARCHAR(255) NULL, 
  phone VARCHAR(25), 
  email VARCHAR(255) NULL, 
  password VARCHAR(255) NULL, 
  street VARCHAR(255), 
  city VARCHAR(50), 
  state VARCHAR(25), 
  zip_code VARCHAR(5)
);

CREATE TABLE IF NOT EXISTS sales_stores(
  store_id INT PRIMARY KEY, 
  store_name VARCHAR(255)   NOT NULL, 
  phone VARCHAR(25), 
  email VARCHAR(255), 
  street VARCHAR(255), 
  city VARCHAR(255), 
  state VARCHAR(10), 
  zip_code VARCHAR(5)
);

CREATE TABLE IF NOT EXISTS sales_staffs(
  staff_id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
  first_name VARCHAR(50) NULL, 
  last_name VARCHAR(50) NULL, 
  email VARCHAR(255) NULL UNIQUE, 
  password VARCHAR(255) NULL, 
  phone VARCHAR(100), 
  active tinyint NOT NULL, 
  store_id INT, 
  manager_id INT, 
  role_id INT
);

CREATE TABLE IF NOT EXISTS sales_orders(
  order_id INT PRIMARY KEY, 
  customer_id INT, 
  order_status tinyint NULL, 
  product VARCHAR(55) NULL, 
  order_date DATE NULL, 
  required_date DATE NULL, 
  shipped_date DATE, 
  store_id  INT NULL, 
  staff_id  INT NULL
);

CREATE TABLE IF NOT EXISTS sales_order_items(
  order_id bigint AUTO_INCREMENT, 
  item_id VARCHAR(55), 
  product_id VARCHAR(55), 
  quantity VARCHAR(55) NOT NULL, 
  discount  DECIMAL(4, 2) NULL DEFAULT 0, 
  PRIMARY KEY (order_id), 
  date VARCHAR(55)
);

CREATE TABLE IF NOT EXISTS production_stocks(
  store_id bigint AUTO_INCREMENT  NOT NULL, 
  product_id bigint AUTO_INCREMENT  NOT NULL, 
  quantity VARCHAR(55), 
  minimum_quantity VARCHAR(55), 
  PRIMARY  KEY(product_id)
);

CREATE TABLE IF NOT EXISTS system_app_key(
  id bigint AUTO_INCREMENT NOT NULL, 
  VERSION VARCHAR(21), 
  product_id VARCHAR(244), 
  activation_status  INT, 
  activation_date TEXT, 
  expire_date TEXT, 
  PRIMARY  KEY(id)
);

CREATE TABLE IF NOT EXISTS system_ccounts(
  id bigint AUTO_INCREMENT  NOT NULL, 
  amount VARCHAR(55), 
  date VARCHAR(55), 
  collected_by VARCHAR(244), 
  PRIMARY  KEY(id)
);

CREATE TABLE IF NOT EXISTS production_stocks_report(
  store_id bigint NULL, 
  product_id bigint AUTO_INCREMENT, 
  quantity VARCHAR(55), 
  date01 VARCHAR(244), 
  user01 VARCHAR(244), 
  PRIMARY  KEY(product_id)
);

CREATE TABLE IF NOT EXISTS system_notifications(
  notice_id bigint AUTO_INCREMENT  NOT NULL, 
  date VARCHAR(244), 
  title VARCHAR(244), 
  message VARCHAR(244), 
  viewed VARCHAR(244), 
  code VARCHAR(244), 
  PRIMARY KEY(notice_id)
);

CREATE TABLE IF NOT EXISTS production_categories(
  category_id bigint AUTO_INCREMENT  NOT NULL, 
  category_name VARCHAR (255) NOT NULL,
  PRIMARY KEY ( category_id )
);

CREATE TABLE IF NOT EXISTS production_brands(
  brand_id bigint AUTO_INCREMENT NOT NULL,
  brand_name VARCHAR (255) NOT NULL,
  PRIMARY KEY ( brand_id )
);
