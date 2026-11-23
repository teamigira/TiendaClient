-- V11: Phase 5 — "hold / reopen" support for the Sale screen.
--
-- The cart is the set of sales_order_items that have no matching row in
-- sales_orders. To let a cashier PARK an unfinished sale (customer stepped
-- away) and resume it later, each item now carries a status and an optional
-- hold reference:
--   * CART  (or NULL) — in the active cart, shown on the Sale screen
--   * HELD            — parked under hold_ref, hidden from the active cart
-- Completing a sale is unchanged (it inserts the sales_orders rows); reopening
-- a completed sale simply deletes those rows so the items return to the cart.

ALTER TABLE Tienda.sales_order_items
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NULL DEFAULT 'CART';

ALTER TABLE Tienda.sales_order_items
    ADD COLUMN IF NOT EXISTS hold_ref VARCHAR(40) NULL;

ALTER TABLE Tienda.sales_order_items
    ADD COLUMN IF NOT EXISTS hold_label VARCHAR(100) NULL;

-- Any pre-existing uncommitted items belong to the active cart.
UPDATE Tienda.sales_order_items SET status = 'CART' WHERE status IS NULL;
