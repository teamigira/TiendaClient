-- V9: add the list_price column to sales_order_items.
--
-- Orders.addOrder() inserts into sales_order_items(product_id, quantity,
-- list_price, discount, date) but the table never had a list_price column, so
-- every attempt to record a sale failed. Storing the price at the time of sale
-- is also the correct behaviour for a POS (historical pricing is preserved even
-- if the product's price later changes), and lets the Reports page total revenue
-- directly from the line items.

ALTER TABLE Tienda.sales_order_items
    ADD COLUMN IF NOT EXISTS list_price DECIMAL(10, 2) NULL DEFAULT 0;
