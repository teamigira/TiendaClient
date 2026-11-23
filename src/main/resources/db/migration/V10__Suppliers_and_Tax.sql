-- V10: Phase 4 — master tables for Suppliers and Tax rates.
--
-- The product form has "Suppliers" and "Tax" tabs but there was nowhere to
-- store the actual suppliers or tax rates (production_products_details only
-- holds free-text per-product values). These master tables back the new
-- Suppliers and Tax manager screens and let a product reference a real
-- supplier / tax rate instead of a loose string.

CREATE TABLE IF NOT EXISTS Tienda.production_suppliers (
    supplier_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(255) NOT NULL,
    contact_name  VARCHAR(255) NULL,
    phone         VARCHAR(50)  NULL,
    email         VARCHAR(255) NULL,
    address       VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS Tienda.production_tax (
    tax_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    tax_name   VARCHAR(255)  NOT NULL,
    rate       DECIMAL(6, 3) NOT NULL DEFAULT 0,
    is_default BOOLEAN       NOT NULL DEFAULT FALSE
);

-- A sensible starting tax rate so the Tax screen is not empty on first open.
-- Tanzania VAT is 18%; mark it the default. Guarded so re-running is a no-op.
INSERT INTO Tienda.production_tax (tax_name, rate, is_default)
SELECT 'VAT', 18.000, TRUE
WHERE NOT EXISTS (SELECT 1 FROM Tienda.production_tax);
