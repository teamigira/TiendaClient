-- V8: normalise brands/categories and relink products.
--
-- Products historically stored the brand/category NAME (e.g. 'General') in the
-- VARCHAR brand_id/category_id columns, while production_brands.brand_id and
-- production_categories.category_id are BIGINT. The seeding also inserted a new
-- 'General' brand/category on every startup, producing many duplicates. The
-- numeric JOIN used by the product and report screens then failed with
-- "NumberFormatException: For input string: General".
--
-- This migration repoints every product to the canonical (lowest) id for its
-- brand/category and deletes the duplicate rows, so the existing numeric JOINs
-- work again. It is safe when there are no products or no duplicates.

-- 1. Repoint products to the canonical brand id. The current value may be either
--    the brand NAME ('General') or an existing brand id ('36'); both are mapped
--    to the lowest id that shares the same brand name.
UPDATE Tienda.production_products p
SET brand_id = (
    SELECT MIN(b2.brand_id)
    FROM Tienda.production_brands b1
    JOIN Tienda.production_brands b2 ON b2.brand_name = b1.brand_name
    WHERE b1.brand_name = p.brand_id
       OR CAST(b1.brand_id AS VARCHAR) = p.brand_id
)
WHERE EXISTS (
    SELECT 1 FROM Tienda.production_brands b
    WHERE b.brand_name = p.brand_id
       OR CAST(b.brand_id AS VARCHAR) = p.brand_id
);

-- 2. Repoint products to the canonical category id (same logic).
UPDATE Tienda.production_products p
SET category_id = (
    SELECT MIN(c2.category_id)
    FROM Tienda.production_categories c1
    JOIN Tienda.production_categories c2 ON c2.category_name = c1.category_name
    WHERE c1.category_name = p.category_id
       OR CAST(c1.category_id AS VARCHAR) = p.category_id
)
WHERE EXISTS (
    SELECT 1 FROM Tienda.production_categories c
    WHERE c.category_name = p.category_id
       OR CAST(c.category_id AS VARCHAR) = p.category_id
);

-- 3. Remove duplicate brands/categories, keeping the lowest id per name.
DELETE FROM Tienda.production_brands
WHERE brand_id NOT IN (
    SELECT MIN(brand_id) FROM Tienda.production_brands GROUP BY brand_name
);

DELETE FROM Tienda.production_categories
WHERE category_id NOT IN (
    SELECT MIN(category_id) FROM Tienda.production_categories GROUP BY category_name
);
