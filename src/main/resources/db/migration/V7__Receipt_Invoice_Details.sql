CREATE TABLE receipts_details (
    receipt_id INT PRIMARY KEY AUTO_INCREMENT, -- For MySQL, use appropriate syntax for other databases
    sales_id INT NOT NULL,
    company_name VARCHAR(50),
    footer_message VARCHAR(55),
    address VARCHAR(255),
    created_by VARCHAR(55),
    sold_to VARCHAR(55),
    sales_date DATE, -- Changed to DATE data type
    order_date DATE, -- Changed to DATE data type
    created_date TIMESTAMP, -- Changed to TIMESTAMP data type
    barcode VARCHAR(255)
);



CREATE TABLE receipt_settings (
    settings_id INT PRIMARY KEY AUTO_INCREMENT, -- For MySQL, use appropriate syntax for other databases
    created_by INT NOT NULL,
    printer_name VARCHAR(50) NOT NULL,
    automatically BOOLEAN, -- Changed to BOOLEAN data type
    template VARCHAR(55)
);
