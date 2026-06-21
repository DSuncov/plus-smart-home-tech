CREATE TABLE IF NOT EXISTS orders (
    order_id UUID NOT NULL PRIMARY KEY,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR,
    weight DOUBLE PRECISION,
    volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DECIMAL(10, 2),
    delivery_price DECIMAL(10, 2),
    products_price DECIMAL(10, 2),
    username VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_products FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

