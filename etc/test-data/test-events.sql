CREATE TEMPORARY TABLE temp_issuers (
    issuer_address  VARCHAR   NOT NULL,
    asset_addresses VARCHAR[] NOT NULL,
    num_campaigns   INT       NOT NULL
);

CREATE TEMPORARY TABLE temp_campaigns (
    issuer_address   VARCHAR NOT NULL,
    asset_address    VARCHAR NOT NULL,
    campaign_address VARCHAR NOT NULL,
    num_investors    INT     NOT NULL
);

CREATE TEMPORARY TABLE temp_investors (
    investor_address VARCHAR    NOT NULL,
    index            INT UNIQUE NOT NULL PRIMARY KEY
);

CREATE TEMPORARY TABLE temp_campaign_investors (
    issuer_address            VARCHAR NOT NULL,
    asset_address             VARCHAR NOT NULL,
    campaign_address          VARCHAR NOT NULL,
    investor_address          VARCHAR NOT NULL,
    num_reserve_investments   INT     NOT NULL,
    num_canceled_investments  INT     NOT NULL,
    num_finalized_investments INT     NOT NULL
);

CREATE FUNCTION pg_temp.random_int_range(min_val INT, max_val INT) RETURNS INT AS
'SELECT (trunc(random() * (max_val - min_val + 1))::INT % (max_val - min_val + 1)) + min_val'
    LANGUAGE sql;

CREATE FUNCTION pg_temp.random_eth_address() RETURNS VARCHAR AS
'SELECT ''0x'' ||
        substr(replace(gen_random_uuid()::TEXT, ''-'', '''') || replace(gen_random_uuid()::TEXT, ''-'', ''''), 25)'
    LANGUAGE sql;

CREATE FUNCTION pg_temp.random_eth_hash() RETURNS VARCHAR AS
'SELECT ''0x'' || replace(gen_random_uuid()::TEXT, ''-'', '''') || replace(gen_random_uuid()::TEXT, ''-'', '''')'
    LANGUAGE sql;

CREATE FUNCTION pg_temp.random_eth_address_array(min_size INT, max_size INT) RETURNS VARCHAR[] AS
'SELECT array(SELECT pg_temp.random_eth_address()
              FROM generate_series(0, pg_temp.random_int_range(min_size, max_size)))'
    LANGUAGE sql;

-- Test data set: 300 issuers, 50% issuers will have 1 campaign, 25% issuers will have 2 campaigns,
-- 15% will have 3 campaigns and the remaining 10% will randomly have 4-15 campaigns. Every issuer will have 1-5 assets.
-- This means there will be an average of 645 campaigns.
INSERT INTO temp_issuers(issuer_address, asset_addresses, num_campaigns)
SELECT pg_temp.random_eth_address(),
    pg_temp.random_eth_address_array(1, 5),
    CASE WHEN series.series <= 150 -- 50% of 300
             THEN 1
         WHEN series.series <= 225 -- 75% of 300
             THEN 2
         WHEN series.series <= 270 -- 90% of 300
             THEN 3
         ELSE pg_temp.random_int_range(4, 10) END
FROM generate_series(1, 300) AS series;

-- Every campaign will randomly have 10-500 investors. Each campaign will use a random asset from its issuer.
INSERT INTO temp_campaigns(issuer_address, asset_address, campaign_address, num_investors)
SELECT issuer_address, asset_addresses[pg_temp.random_int_range(1, array_length(asset_addresses, 1))],
    pg_temp.random_eth_address(),
    pg_temp.random_int_range(10, 500)
FROM temp_issuers
         JOIN generate_series(1, num_campaigns) ON TRUE;

-- Generate 15,000 random investor addresses.
INSERT INTO temp_investors(investor_address, index)
SELECT pg_temp.random_eth_address(), series.series
FROM generate_series(1, 15000) AS series;

CREATE FUNCTION pg_temp.random_investor() RETURNS VARCHAR AS
'WITH random_index AS (SELECT pg_temp.random_int_range(1, 15000) AS ri)
 SELECT investor_address
 FROM temp_investors
          JOIN random_index ON TRUE
 WHERE index = random_index.ri'
    LANGUAGE sql;

-- Each investor will randomly have 1-30 reserve investments, 0-3 cancel investments and 0-10 finalized investments
-- for every assigned campaign, which means there will be a total of 3,618,450 investments on average per chain.
INSERT INTO temp_campaign_investors(issuer_address, asset_address, campaign_address, investor_address,
                                    num_reserve_investments, num_canceled_investments, num_finalized_investments)
SELECT issuer_address, asset_address, campaign_address, pg_temp.random_investor(),
    pg_temp.random_int_range(1, 30), pg_temp.random_int_range(0, 3), pg_temp.random_int_range(0, 10)
FROM temp_campaigns
         JOIN generate_series(1, num_investors) ON TRUE;

-- Generate reserve investments for chain_id = 1
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    1, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'RESERVE_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_reserve_investments) AS series ON TRUE;

-- Generate canceled investments for chain_id = 1
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    1, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'CANCEL_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_canceled_investments) AS series ON TRUE;

-- Generate completed investments for chain_id = 1
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    1, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'COMPLETED_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_finalized_investments) AS series ON TRUE;

-- Generate reserve investments for chain_id = 137
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    137, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'RESERVE_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_reserve_investments) AS series ON TRUE;

-- Generate canceled investments for chain_id = 137
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    137, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'CANCEL_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_canceled_investments) AS series ON TRUE;

-- Generate completed investments for chain_id = 137
INSERT INTO public.event(uuid,
                         chain_id,
                         from_address,
                         to_address,
                         contract,
                         hash,
                         type,
                         log_index,
                         asset,
                         timestamp,
                         block_number,
                         block_hash,
                         token_amount,
                         token_value,
                         payout_id,
                         revenue,
                         token_symbol,
                         issuer,
                         decimals)
SELECT gen_random_uuid(),
    137, -- chain_id
    temp_campaign_investors.investor_address, -- from_address
    temp_campaign_investors.campaign_address, -- to_address
    temp_campaign_investors.campaign_address, -- contract
    pg_temp.random_eth_hash(), -- hash
    'COMPLETED_INVESTMENT', -- type
    series.series, --log_index
    'Asset at ' || temp_campaign_investors.asset_address, -- asset
    pg_temp.random_int_range(6000000, 7000000), -- timestamp
    pg_temp.random_int_range(30000, 60000), -- block_number
    pg_temp.random_eth_hash(), -- block_hash
    0, -- token_amount, not needed
    0, -- token_value, not needed
    0, -- payout_id, not needed
    0, --revenue, not needed
    'SYM', -- token_symbol
    temp_campaign_investors.issuer_address, -- issuer
    18 -- decimals
FROM temp_campaign_investors
         JOIN generate_series(1, num_finalized_investments) AS series ON TRUE;

DROP FUNCTION pg_temp.random_int_range;
DROP FUNCTION pg_temp.random_eth_address;
DROP FUNCTION pg_temp.random_eth_hash;
DROP FUNCTION pg_temp.random_eth_address_array;
DROP FUNCTION pg_temp.random_investor;
DROP TABLE temp_issuers;
DROP TABLE temp_campaigns;
DROP TABLE temp_investors;
DROP TABLE temp_campaign_investors;
