--Crear DATABASE
DROP DATABASE IF EXISTS "LibreriaDB";
CREATE DATABASE "LibreriaDB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Colombia.1252'
    LC_CTYPE = 'Spanish_Colombia.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Eliminar tablas si existen en orden inverso para evitar conflictos con FK
DROP TABLE IF EXISTS public.articulo CASCADE;
DROP TABLE IF EXISTS public.ponencia CASCADE;
DROP TABLE IF EXISTS public.libro CASCADE;
DROP TABLE IF EXISTS public.evento CASCADE;
DROP TABLE IF EXISTS public.reserva CASCADE;
DROP TABLE IF EXISTS public.documento CASCADE;
DROP TABLE IF EXISTS public.usuario CASCADE;

-- Crear la tabla usuario
CREATE TABLE IF NOT EXISTS public.usuario
(
    nombre character varying(255) COLLATE pg_catalog."default" NOT NULL,
    contrasena character varying(255) COLLATE pg_catalog."default" NOT NULL,
    correoelectronico character varying(255) COLLATE pg_catalog."default" NOT NULL,
    direccionfisica character varying(255) COLLATE pg_catalog."default" NOT NULL,
    numerotelefonico character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT usuario_pk PRIMARY KEY (nombre)
);

-- Crear la tabla documento
CREATE TABLE IF NOT EXISTS public.documento
(
    iddocumento integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    titulo character varying(255) COLLATE pg_catalog."default",
    fechapublicacion date,
    autores character varying(255) COLLATE pg_catalog."default",
    mespublicacion character varying(10) COLLATE pg_catalog."default",
    diapublicacion character varying(10) COLLATE pg_catalog."default",
    editorial character varying(255) COLLATE pg_catalog."default",
    estado character varying(11) COLLATE pg_catalog."default" NOT NULL,
    propietario character varying(255) COLLATE pg_catalog."default" NOT NULL,
    tipo character varying(11) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT documento_pk PRIMARY KEY (iddocumento),
    CONSTRAINT "usuario_FK" FOREIGN KEY (propietario)
        REFERENCES public.usuario (nombre) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Crear la tabla reserva
CREATE TABLE IF NOT EXISTS public.reserva
(
    idreserva integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    fechareserva date NOT NULL,
    fechaentrega date,
    estado character varying(10) COLLATE pg_catalog."default" NOT NULL,
    documento integer NOT NULL,
    usuario character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT reserva_pk PRIMARY KEY (idreserva),
    CONSTRAINT "documento_FK" FOREIGN KEY (documento)
        REFERENCES public.documento (iddocumento) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT "usuario_FK" FOREIGN KEY (usuario)
        REFERENCES public.usuario (nombre) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Crear la tabla evento
CREATE TABLE IF NOT EXISTS public.evento
(
    idevento integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    tipoevento character varying COLLATE pg_catalog."default" NOT NULL,
    usuario character varying(255) COLLATE pg_catalog."default" NOT NULL,
    documento integer NOT NULL,
    fechaevento date NOT NULL,
    CONSTRAINT evento_pk PRIMARY KEY (idevento),
    CONSTRAINT "documento_FK" FOREIGN KEY (documento)
        REFERENCES public.documento (iddocumento) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT "usuario_FK" FOREIGN KEY (usuario)
        REFERENCES public.usuario (nombre) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Crear la tabla libro
CREATE TABLE IF NOT EXISTS public.libro
(
    iddocumento integer NOT NULL,
    numeropaginas smallint,
    isbn character varying COLLATE pg_catalog."default",
    CONSTRAINT libro_pk PRIMARY KEY (iddocumento),
    CONSTRAINT "documento_FK" FOREIGN KEY (iddocumento)
        REFERENCES public.documento (iddocumento) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);


-- Crear la tabla
CREATE TABLE IF NOT EXISTS public.ponencia
(
    iddocumento integer NOT NULL,
    congreso character varying(255) COLLATE pg_catalog."default",
    isbn character varying COLLATE pg_catalog."default",
    CONSTRAINT ponencia_pk PRIMARY KEY (iddocumento),
    CONSTRAINT "documento_FK" FOREIGN KEY (iddocumento)
        REFERENCES public.documento (iddocumento) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Crear la tabla articulo
CREATE TABLE IF NOT EXISTS public.articulo
(
    iddocumento integer NOT NULL,
    ssn character varying COLLATE pg_catalog."default",
    CONSTRAINT articulo_pk PRIMARY KEY (iddocumento),
    CONSTRAINT "documento_FK" FOREIGN KEY (iddocumento)
        REFERENCES public.documento (iddocumento) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

