ALTER TABLE event ALTER COLUMN token_value SET DEFAULT 0;
ALTER TABLE event ALTER COLUMN token_value DROP NOT NULL;
