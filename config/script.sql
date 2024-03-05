CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    balance INT NOT NULL DEFAULT 0,
    lmt INT NOT NULL
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES clients (id),
    value INT NOT NULL,
    type CHAR(1) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(10) NOT NULL
);

DO $$
BEGIN
    INSERT INTO clients (name, lmt)
    VALUES
        ('o barato sai caro', 1000 * 100),
        ('zan corp ltda', 800 * 100),
        ('les cruders', 10000 * 100),
        ('padaria joia de cocaia', 100000 * 100),
        ('kid mais', 5000 * 100);
END; $$
