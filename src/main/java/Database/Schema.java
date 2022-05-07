/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

/**
 *
 * @author Nkanabo
 */

public class Schema {
public static String SchemaQuery = "CREATE TABLE if not exists production_categories" +
"	(category_id bigint auto_increment," +
"	category_name VARCHAR (255) NOT NULL," +
"       PRIMARY KEY ( category_id )"+
");" +
        
"CREATE TABLE if not exists production_brands" +
"	(brand_id bigint auto_increment NOT NULL," +
"	brand_name VARCHAR (255) NOT NULL," +
"       PRIMARY KEY ( brand_id )"+
");" +
"" +
"CREATE TABLE if not exists production_products " +
"	(product_id bigint auto_increment," +
"	product_name VARCHAR (255) NOT NULL," +
"	brand_id VARCHAR (255) NOT NULL," +
"	category_id VARCHAR (255) NOT NULL," +
"	model_year SMALLINT NOT NULL," +
"	expiry_date VARCHAR (255) NOT NULL," +
"	list_price DECIMAL (10, 2) NOT NULL," +
"	retail_price DECIMAL (10, 2) NOT NULL" +
");" +
"" +
"CREATE TABLE if not exists sales_customers (" +
"	customer_id INT PRIMARY KEY," +
"	first_name VARCHAR (255) NOT NULL," +
"	last_name VARCHAR (255) NOT NULL," +
"	phone VARCHAR (25)," +
"	email VARCHAR (255) NOT NULL," +
"	password VARCHAR (255) NOT NULL," +
"	street VARCHAR (255)," +
"	city VARCHAR (50)," +
"	state VARCHAR (25)," +
"	zip_code VARCHAR (5)" +
");" +
"" +
"CREATE TABLE if not exists sales_stores (" +
"	store_id INT PRIMARY KEY," +
"	store_name VARCHAR (255) NOT NULL," +
"	phone VARCHAR (25)," +
"	email VARCHAR (255)," +
"	street VARCHAR (255)," +
"	city VARCHAR (255)," +
"	state VARCHAR (10)," +
"	zip_code VARCHAR (5)" +
");" +
"" +
"CREATE TABLE if not exists sales_staffs (" +
"	staff_id bigint auto_increment NOT NULL PRIMARY KEY," +
"	first_name VARCHAR (50) NOT NULL," +
"	last_name VARCHAR (50) NOT NULL," +
"	email VARCHAR (255) NOT NULL UNIQUE," +
"	password VARCHAR (255) NOT NULL," +
"	phone VARCHAR (100)," +
"	active tinyint NOT NULL," +
"	store_id INT," +
"	manager_id INT," +
"	role_id INT" +
");" +
"" +
"CREATE TABLE if not exists sales_orders (" +
"	order_id INT PRIMARY KEY," +
"	customer_id INT," +
"	order_status tinyint NOT NULL," +
"	product VARCHAR (55) NOT NULL," +
"	order_date DATE NOT NULL," +
"	required_date DATE NOT NULL," +
"	shipped_date DATE," +
"	store_id INT NOT NULL," +
"	staff_id INT NOT NULL" +
");" +
"" +
"CREATE TABLE if not exists sales_order_items (" +
"	order_id bigint auto_increment," +
"	item_id VARCHAR (55)," +
"	product_id VARCHAR (55)," +
"	quantity INT NOT NULL," +
"	list_price DECIMAL (10, 2) NOT NULL," +
"	discount DECIMAL (4, 2) NOT NULL DEFAULT 0," +
"	PRIMARY KEY (order_id), date VARCHAR(55)" +
");" +
"" +
"CREATE TABLE if not exists production_stocks (" +
"	store_id bigint auto_increment NOT NULL," +
"	product_id bigint auto_increment NOT NULL," +
"	quantity INT," +
"	PRIMARY KEY (product_id)" +
");"
 + 
 "CREATE TABLE if not exists app_key (" +
"       id INT,   " +        
"	version VARCHAR(21)," +
"	product_id VARCHAR(244)," +
"       activation_status INT," +
"       activation_date TEXT," +
"       expire_date TEXT," +
"       PRIMARY KEY (id)" + ");" +
"CREATE TABLE if not exists Accounts (" +
"       id bigint auto_increment NOT NULL,   " +        
"	amount VARCHAR(55)," +
"	date VARCHAR(55)," +
"	collected_by VARCHAR(244)," +
"       PRIMARY KEY (id)" + ");";
}
