/*2019-08-19*/

update app.reg_movimiento set transferencia_dominio = false;
ALTER TABLE app.reg_ficha ADD COLUMN user_revision character varying(100);
ALTER TABLE app.reg_ficha ADD COLUMN fecha_revision timestamp without time zone;

CREATE TABLE bitacora.orden_fichas
(
    id bigserial NOT NULL,
    fecha_orden timestamp without time zone,
    tipo_orden integer,
    estado bigint,
    fecha_fin timestamp without time zone,
    tramite bigint,
    movimiento bigint,
    ficha bigint,
    certificado bigint,
    observacion character varying(500) COLLATE pg_catalog."default",
    usuario bigint,
    num_orden integer,
    mes_orden integer,
    anio_orden integer,
    CONSTRAINT generacion_fichas_pkey PRIMARY KEY (id),
    CONSTRAINT orden_fichas_usuario_fkey FOREIGN KEY (usuario)
        REFERENCES app.acl_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE bitacora.orden_fichas
    OWNER to sisapp;


CREATE TABLE bitacora.orden_fichas_detalle
(
    id bigserial NOT NULL,
    orden_ficha bigint NOT NULL,
    fecha_inicio timestamp without time zone,
    fecha_fin timestamp without time zone,
    name_user character varying COLLATE pg_catalog."default",
    movimiento bigint,
    tramite bigint,
    ficha bigint,
    observacion character varying COLLATE pg_catalog."default",
    CONSTRAINT orden_fichas_detalle_pkey PRIMARY KEY (id),
    CONSTRAINT orden_fichas_detalle_ficha_fkey FOREIGN KEY (ficha)
        REFERENCES app.reg_ficha (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT orden_fichas_detalle_movimiento_fkey FOREIGN KEY (movimiento)
        REFERENCES app.reg_movimiento (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT orden_fichas_detalle_orden_ficha_fkey FOREIGN KEY (orden_ficha)
        REFERENCES bitacora.orden_fichas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT orden_fichas_detalle_tramite_fkey FOREIGN KEY (tramite)
        REFERENCES flow.historico_tramites (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE bitacora.orden_fichas_detalle
    OWNER to sisapp;


/** 21-08-2019 >> ANGEL NAVARRO */
ALTER TABLE app.reg_ficha ADD COLUMN manazana VARCHAR(20);
ALTER TABLE app.reg_ficha ADD COLUMN lote VARCHAR(20);
ALTER TABLE app.reg_ficha ADD COLUMN division VARCHAR(20);
ALTER TABLE app.reg_ficha ADD COLUMN departamento VARCHAR(100);
ALTER TABLE app.reg_ficha ADD COLUMN nombre_predio VARCHAR(500);

CREATE TABLE app.barrios (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	parroquia BIGINT REFERENCES app.cat_parroquia (id),
	nombre VARCHAR(250), 
	estado BOOLEAN
);

ALTER TABLE app.reg_ficha ADD COLUMN barrio BIGINT REFERENCES app.barrios (id);


/*2019-08-28*/
ALTER TABLE app.barrios ADD COLUMN user_creacion character varying(100);
ALTER TABLE app.barrios ADD COLUMN fecha_creacion timestamp without time zone;

