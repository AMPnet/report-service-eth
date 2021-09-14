TRUNCATE table event;
TRUNCATE table task;
ALTER TABLE task ADD CONSTRAINT uc_chain_id UNIQUE(chain_id);
