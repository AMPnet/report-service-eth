DROP DATABASE IF EXISTS report_service_eth;
CREATE DATABASE report_service_eth ENCODING 'UTF-8';

DROP DATABASE IF EXISTS report_service_eth_test;
CREATE DATABASE report_service_eth_test ENCODING 'UTF-8';

DROP USER IF EXISTS report_service_eth;
CREATE USER report_service_eth WITH PASSWORD 'password';

DROP USER IF EXISTS report_service_eth_test;
CREATE USER report_service_eth_test WITH PASSWORD 'password';
