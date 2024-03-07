CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    balance INT NOT NULL DEFAULT 0,
    lmt INT NOT NULL
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    client_id INT,
    value INT NOT NULL,
    type CHAR(1) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(10) NOT NULL,
    CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE INDEX idx_client_id ON transactions (client_id);

CREATE INDEX idx_transactions_by_client_and_created_at ON transactions (client_id, created_at DESC);

CREATE OR REPLACE FUNCTION update_client_balance(
    client_id_arg INT,
    transaction_id_arg INT,
    transaction_value_arg INT,
    transaction_type_arg CHAR(1)
) RETURNS VOID AS $$
DECLARE
    new_balance INT;
    client_limit INT;
BEGIN
    IF NOT EXISTS (SELECT 1 FROM clients WHERE id = client_id_arg) THEN
        RAISE EXCEPTION 'Client with ID % does not exist', client_id_arg;
    END IF;

    SELECT balance, lmt INTO new_balance, client_limit FROM clients WHERE id = client_id_arg;

    IF transaction_type_arg = 'd' THEN
        new_balance := new_balance - transaction_value_arg;
    ELSE
        new_balance := new_balance + transaction_value_arg;
    END IF;

    IF transaction_type_arg = 'd' AND new_balance < -client_limit THEN
        RAISE EXCEPTION 'Transaction not allowed: Debit transaction exceeds client limit';
    END IF;

    UPDATE clients SET balance = new_balance WHERE id = client_id_arg;

    INSERT INTO transactions (client_id, value, type, description)
    VALUES (client_id_arg, transaction_value_arg, transaction_type_arg, 'Some description'); -- Adjust the description as needed

    COMMIT;
END;
$$ LANGUAGE plpgsql;


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
