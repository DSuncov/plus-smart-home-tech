CREATE TABLE IF NOT EXISTS delivery (
    delivery_id UUID NOT NULL PRIMARY KEY,

    country_from VARCHAR NOT NULL,
    city_from VARCHAR NOT NULL,
    street_from VARCHAR NOT NULL,
    house_from VARCHAR NOT NULL,
    flat_from VARCHAR NOT NULL,

    country_to VARCHAR NOT NULL,
    city_to VARCHAR NOT NULL,
    street_to VARCHAR NOT NULL,
    house_to VARCHAR NOT NULL,
    flat_to VARCHAR NOT NULL,

    state VARCHAR NOT NULL,
    order_id UUID NOT NULL
);