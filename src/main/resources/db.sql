CREATE DATABASE test
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    CONNECTION LIMIT = -1;


-- Table: public.payment

-- DROP TABLE public.payment;

CREATE TABLE public.payment
(
    accountuser character varying(255),
    accountbalance bigint,
    lastoperationdate timestamp without time zone,
    enumoperation character varying(255),
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );
ALTER TABLE public.payment
    OWNER TO postgres;


-- Table: public.tbl_addrs

-- DROP TABLE public.tbl_addrs;

CREATE TABLE public.tbl_addrs
(
    hostname character varying(1024),
    port integer
)
    WITH (
        OIDS=FALSE
    );
ALTER TABLE public.tbl_addrs
    OWNER TO postgres;
