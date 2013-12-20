CREATE USER reports PASSWORD 'reports';
CREATE SCHEMA reports AUTHORIZATION DBA;
--GRANT ALL PRIVILEGES ON DATABASE reports to reports;
--ALTER USER reports SET search_path TO reports;

------------------------------------------------------------------
--  TABLE tre_pattern
------------------------------------------------------------------

CREATE TABLE tre_pattern
(
   id             bigint,
   upload_date    timestamp (6) WITH TIME ZONE,
   manifest       text,
   "name"         CHARACTER VARYING (255),
   version        CHARACTER VARYING (255),
   "_cdate"       timestamp (6) WITH TIME ZONE,
   "_cgsid"       CHARACTER VARYING (255),
   "_edate"       timestamp (6) WITH TIME ZONE,
   "_egsid"       CHARACTER VARYING (255),
   "_stamp"       bigint,
   creator_id     CHARACTER VARYING (255),
   creator_name   CHARACTER VARYING (255)
);

ALTER TABLE tre_pattern
ADD CONSTRAINT "PK_TRE_PATTERN"
PRIMARY KEY (id);


------------------------------------------------------------------
--  TABLE tre_pattern_asset
------------------------------------------------------------------

CREATE TABLE tre_pattern_asset
(
   id           bigint,
   "path"       CHARACTER VARYING (512),
   content      bytea,
   pattern_id   bigint
);

ALTER TABLE tre_pattern_asset
ADD CONSTRAINT "FK_TRE_PATTERN_ASSET_PATTERN"
FOREIGN KEY (pattern_id) REFERENCES tre_pattern(id);

ALTER TABLE tre_pattern_asset
ADD CONSTRAINT "PK_TRE_PATTERN_ASSET"
PRIMARY KEY (id);

------------------------------------------------------------------
--  TABLE tre_pattern_role
------------------------------------------------------------------

CREATE TABLE tre_pattern_role
(
   id_pattern   bigint,
   role         CHARACTER VARYING (255)
);

ALTER TABLE tre_pattern_role
ADD CONSTRAINT "FK_TRE_PATTERN_ROLE_PATTERN"
FOREIGN KEY (id_pattern) REFERENCES tre_pattern(id);



--  TABLE tpr_auth_role
------------------------------------------------------------------

CREATE TABLE tpr_auth_role
(
   id_role        bigint,
   id_authority   bigint
);

ALTER TABLE tpr_auth_role
ADD CONSTRAINT "FKPR_AUTHROLE_AUTH"
FOREIGN KEY (id_role) REFERENCES tpr_authority(id);

ALTER TABLE tpr_auth_role
ADD CONSTRAINT "FKPR_AUTHROLE_ROLE"
FOREIGN KEY (id_role) REFERENCES tpr_role(id);


------------------------------------------------------------------
--  TABLE tpr_authority
------------------------------------------------------------------

CREATE TABLE tpr_authority
(
   id       bigint,
   "name"   CHARACTER VARYING (255)
);

ALTER TABLE tpr_authority
ADD CONSTRAINT "PKPR_AUTHORITY"
PRIMARY KEY (id);

------------------------------------------------------------------
--  TABLE tpr_role
------------------------------------------------------------------

CREATE TABLE tpr_role
(
   id       bigint,
   "name"   CHARACTER VARYING (255)
);

ALTER TABLE tpr_role
ADD CONSTRAINT "PKPR_ROLE"
PRIMARY KEY (id);


