-- V12: Phase 2 — customer loyalty points.
--
-- Adds a running loyalty-points balance to each customer. Points are managed
-- from the Premium "Customer loyalty" screen (award on purchase, redeem for a
-- discount). Defaults to 0 so existing customers simply start with no points.

ALTER TABLE Tienda.sales_customers
    ADD COLUMN IF NOT EXISTS loyalty_points INT NOT NULL DEFAULT 0;
