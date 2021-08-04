-- Event
CREATE TABLE event(
    uuid UUID PRIMARY KEY,
    chain_id BIGINT NOT NULL,
    from_address VARCHAR NOT NULL,
    to_address VARCHAR NOT NULL,
    contract VARCHAR NOT NULL,
    hash VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    log_index BIGINT NOT NULL,
    asset VARCHAR NOT NULL,
    timestamp BIGINT NOT NULL,
    block_number BIGINT NOT NULL,
    block_hash VARCHAR NOT NULL,
    token_amount NUMERIC,
    token_value NUMERIC,
    payout_id BIGINT,
    revenue NUMERIC,
    CONSTRAINT uc_event_block_hash_tx_hash_log_index UNIQUE(block_hash, hash, log_index)
);

-- Task
CREATE TABLE task(
    uuid UUID PRIMARY KEY,
    block_number BIGINT NOT NULL,
    timestamp BIGINT NOT NULL
);

CREATE INDEX idx_event_from ON event(from_address);
CREATE INDEX idx_event_to ON event(to_address);
