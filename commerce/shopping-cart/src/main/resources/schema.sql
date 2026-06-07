CREATE TABLE IF NOT EXISTS shopping_carts (
    shopping_cart_id UUID NOT NULL PRIMARY KEY,
    username VARCHAR NOT NULL,
    cart_state VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT
);