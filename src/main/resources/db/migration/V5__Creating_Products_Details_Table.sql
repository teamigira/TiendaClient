/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql
 to edit this template
 */
/**
 * Author:  Nkanabo
 * Created: Sep 28, 2022
 */

CREATE TABLE IF NOT EXISTS production_products_details(
  product_id bigint  NOT NULL, 
  product_image VARCHAR(255) NULL, 
  product_expiry_date VARCHAR(255) NULL,
  product_expiry_date_control VARCHAR(255) NULL, 
  product_price_changes VARCHAR(255) NULL, 
  product_price_control VARCHAR(255) NULL, 
  product_promotional_price VARCHAR(255) NULL, 
  product_promotional_start VARCHAR(255) NULL, 
  product_promotional_end  VARCHAR(255) NULL, 
  product_tax VARCHAR(255) NULL, 
  product_tax_use VARCHAR(255) NULL,  
  product_supplier DECIMAL(10, 2) NULL
);