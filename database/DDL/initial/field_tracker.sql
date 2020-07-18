-- database generated with pgmodeler (postgresql database modeler).
-- pgmodeler  version: 0.9.3-alpha
-- postgresql version: 12.0
-- project site: pgmodeler.io
-- model author: ---


-- database creation must be done outside a multicommand file.
-- these commands were put in this file only as a convenience.
-- -- object: new_database | type: database --
-- -- drop database if exists new_database;
-- create database new_database;
-- -- ddl-end --
-- 

-- these commands were put in this file only as a convenience.
-- object: postgis | type: extension --
-- drop extension if exists postgis cascade;
-- create extension postgis
-- with schema public;
-- ddl-end --


-- object: field_manage | type: schema --
-- drop schema if exists field_manage cascade;
create schema field_manage;
-- ddl-end --
alter schema field_manage owner to postgres;
-- ddl-end --

set search_path to pg_catalog,public,field_manage;
-- ddl-end --

-- object: field_manage."field_user" | type: table --
-- drop table if exists field_manage."field_user" cascade;
create table field_manage."field_user" (
	"field_user_id" serial not null,
	"last_name" varchar(50) not null,
	"first_name" varchar(50) not null,
	"app_admin_b" boolean not null,
	"app_login" varchar(15),
	"app_encpassword" text,
	"email_address" varchar(150),
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	constraint "field_user_pk" primary key ("field_user_id")

);
-- ddl-end --
comment on column field_manage."field_user"."app_encpassword" is e'encrypted password with pgcrypto';
-- ddl-end --
alter table field_manage."field_user" owner to postgres;
-- ddl-end --

-- object: field_manage."field_user_hist" | type: table --
-- drop table if exists field_manage."field_user_hist" cascade;
create table field_manage."field_user_hist" (
	"field_user_hist_id" serial not null,
	"field_user_id" integer not null,
	"column_name" varchar(20) not null,
	"old_value" varchar(100) not null,
	"new_value" varchar(100),
	"created_datetz" timestamptz not null,
	"edit_user_id" integer not null,
	constraint "field_user_hist_pk" primary key ("field_user_hist_id")

);
-- ddl-end --
alter table field_manage."field_user_hist" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_user_hist" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_user_hist" add constraint "field_user_fk" foreign key ("field_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field" | type: table --
-- drop table if exists field_manage."field" cascade;
create table field_manage."field" (
	"field_id" serial not null,
	"field_name" varchar(50),
	"field_desc" text,
	"address" varchar(50),
	"adddress_2" varchar(50),
	"city" varchar(50),
	"state" varchar(2),
	"zip" varchar(10),
	"county" varchar(50),
	"field_attributes" jsonb,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_pk" primary key ("field_id")

);
-- ddl-end --
alter table field_manage."field" owner to postgres;
-- ddl-end --

select addgeometrycolumn('field_manage', 'field', 'field_geom', 4326, 'POLYGON', 2);

-- object: "field_user_fk1" | type: constraint --
-- alter table field_manage."field_user_hist" drop constraint if exists "field_user_fk1" cascade;
alter table field_manage."field_user_hist" add constraint "field_user_fk1" foreign key ("edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_geom_hist" | type: table --
-- drop table if exists field_manage."field_geom_hist" cascade;
create table field_manage."field_geom_hist" (
	"field_geom_hist_id" serial not null,
	"field_id" integer not null,
	"created_datetz" timestamptz not null,
	"edit_user_id" integer not null,
	constraint "field_geom_hist_pk" primary key ("field_geom_hist_id")

);
-- ddl-end --
alter table field_manage."field_geom_hist" owner to postgres;
-- ddl-end --

select addgeometrycolumn('field_manage', 'field_geom_hist', 'old_geom', 4326, 'POLYGON', 2);

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_geom_hist" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_geom_hist" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_geom_hist" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_geom_hist" add constraint "field_user_fk" foreign key ("edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_hist" | type: table --
-- drop table if exists field_manage."field_hist" cascade;
create table field_manage."field_hist" (
	"field_hist_id" serial not null,
	"field_id" integer not null,
	"table_name" varchar(25) not null,
	"column_name" varchar(20) not null,
	"old_value" varchar(100) not null,
	"new_value" varchar(100),
	"created_datetz" timestamptz not null,
	"edit_user_id" integer not null,
	constraint "field_hist_pk" primary key ("field_hist_id")

);
-- ddl-end --
alter table field_manage."field_hist" owner to postgres;
-- ddl-end --

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_hist" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_hist" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_hist" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_hist" add constraint "field_user_fk" foreign key ("edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_grower" | type: table --
-- drop table if exists field_manage."field_grower" cascade;
create table field_manage."field_grower" (
	"field_grower_id" serial not null,
	"field_id" integer not null,
	"grower_id" integer not null,
	"created_datetz" timestamptz not null,
	constraint "field_grower_pk" primary key ("field_grower_id")

);
-- ddl-end --
alter table field_manage."field_grower" owner to postgres;
-- ddl-end --

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_grower" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_grower" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_grower" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_grower" add constraint "field_user_fk" foreign key ("grower_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_attribute_hist" | type: table --
-- drop table if exists field_manage."field_attribute_hist" cascade;
create table field_manage."field_attribute_hist" (
	"field_attribute_hist_id" bigserial not null,
	"field_id" integer not null,
	"old_attributes" jsonb not null,
	"created_datetz" timestamptz not null,
	"edit_user_id" integer not null,
	constraint "field_attribute_hist_pk" primary key ("field_attribute_hist_id")

);
-- ddl-end --
alter table field_manage."field_attribute_hist" owner to postgres;
-- ddl-end --

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_attribute_hist" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_attribute_hist" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_attribute_hist" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_attribute_hist" add constraint "field_user_fk" foreign key ("edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_owner" | type: table --
-- drop table if exists field_manage."field_owner" cascade;
create table field_manage."field_owner" (
	"field_owner_id" serial not null,
	"field_id" integer not null,
	"owner_id" integer not null,
	"created_datetz" timestamptz not null,
	constraint "field_owner_pk" primary key ("field_owner_id")

);
-- ddl-end --
alter table field_manage."field_owner" owner to postgres;
-- ddl-end --

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_owner" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_owner" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_owner" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_owner" add constraint "field_user_fk" foreign key ("owner_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_activity_type" | type: table --
-- drop table if exists field_manage."field_activity_type" cascade;
create table field_manage."field_activity_type" (
	"field_activity_type_id" smallserial not null,
	"activity_type" varchar(50) not null,
	"activity_desc" text,
	"active_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_activity_type_pk" primary key ("field_activity_type_id")

);
-- ddl-end --
alter table field_manage."field_activity_type" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_activity_type" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_activity_type" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."field_activity" | type: table --
-- drop table if exists field_manage."field_activity" cascade;
create table field_manage."field_activity" (
	"field_activity_id" bigserial not null,
	"field_id" integer not null,
	"field_activity_type_id" smallint not null,
	"description" text,
	"activity_datetz" timestamptz not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_activity_pk" primary key ("field_activity_id")

);
-- ddl-end --
alter table field_manage."field_activity" owner to postgres;
-- ddl-end --

select addgeometrycolumn('field_manage', 'field_activity', 'activity_geom', 4326, 'POINT', 2);

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_activity" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_activity" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_activity_type_fk" | type: constraint --
-- alter table field_manage."field_activity" drop constraint if exists "field_activity_type_fk" cascade;
alter table field_manage."field_activity" add constraint "field_activity_type_fk" foreign key ("field_activity_type_id")
references field_manage."field_activity_type" ("field_activity_type_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_activity_file_type" | type: table --
-- drop table if exists field_manage."field_activity_file_type" cascade;
create table field_manage."field_activity_file_type" (
	"field_activity_file_type_id" smallserial not null,
	"file_type" varchar(50) not null,
	"file_type_desc" text,
	"active_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_activity_file_type_pk" primary key ("field_activity_file_type_id")

);
-- ddl-end --
alter table field_manage."field_activity_file_type" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_activity_file_type" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_activity_file_type" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."field_activity_file" | type: table --
-- drop table if exists field_manage."field_activity_file" cascade;
create table field_manage."field_activity_file" (
	"field_activity_file" bigserial not null,
	"field_activity_id" bigint not null,
	"field_activity_file_type_id" smallint not null,
	"file_size_mb" smallint,
	"file_location" varchar(500) not null,
	"vendor_partner_id" integer,
	"filename" varchar(100),
	"georeferenced_b" boolean not null,
	"file_datetz" timestamptz,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_activity_file_pk" primary key ("field_activity_file")

);
-- ddl-end --
alter table field_manage."field_activity_file" owner to postgres;
-- ddl-end --

-- object: "field_activity_fk" | type: constraint --
-- alter table field_manage."field_activity_file" drop constraint if exists "field_activity_fk" cascade;
alter table field_manage."field_activity_file" add constraint "field_activity_fk" foreign key ("field_activity_id")
references field_manage."field_activity" ("field_activity_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_activity_hist" | type: table --
-- drop table if exists field_manage."field_activity_hist" cascade;
create table field_manage."field_activity_hist" (
	"field_activity_hist_id" bigserial not null,
	"table_name" varchar(25) not null,
	"column_identifier" bigint not null,
	"column_name" varchar(25),
	"old_value" text not null,
	"new_value" text,
	"created_datetz" timestamptz not null,
	"edit_user_id" integer not null,
	constraint "field_activity_hist_pk" primary key ("field_activity_hist_id")

);
-- ddl-end --
alter table field_manage."field_activity_hist" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_activity_hist" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_activity_hist" add constraint "field_user_fk" foreign key ("edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_activity_file_type_fk" | type: constraint --
-- alter table field_manage."field_activity_file" drop constraint if exists "field_activity_file_type_fk" cascade;
alter table field_manage."field_activity_file" add constraint "field_activity_file_type_fk" foreign key ("field_activity_file_type_id")
references field_manage."field_activity_file_type" ("field_activity_file_type_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: field_manage."field_access" | type: table --
-- drop table if exists field_manage."field_access" cascade;
create table field_manage."field_access" (
	"field_access_id" serial not null,
	"field_id" integer not null,
	"access_point_desc" text,
	"active_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_access_pk" primary key ("field_access_id")

);
-- ddl-end --
alter table field_manage."field_access" owner to postgres;
-- ddl-end --

select addgeometrycolumn('field_manage', 'field_access', 'access_geom', 4326, 'POINT', 2);

-- object: "field_fk" | type: constraint --
-- alter table field_manage."field_access" drop constraint if exists "field_fk" cascade;
alter table field_manage."field_access" add constraint "field_fk" foreign key ("field_id")
references field_manage."field" ("field_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_access" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_access" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."field_attribute" | type: table --
-- drop table if exists field_manage."field_attribute" cascade;
create table field_manage."field_attribute" (
	"field_attribute_id" serial not null,
	"attribute" varchar(50) not null,
	"attr_value" varchar(75) not null,
	"active_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_attribute_pk" primary key ("field_attribute_id")

);
-- ddl-end --
alter table field_manage."field_attribute" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_attribute" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_attribute" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."state" | type: table --
-- drop table if exists field_manage."state" cascade;
create table field_manage."state" (
	"state_id" smallserial not null,
	"fips_code" varchar(2) not null,
	"state_abbr" varchar(2) not null,
	"state_name" varchar(35) not null,
	"app_visible_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "state_pk" primary key ("state_id")

);
-- ddl-end --
alter table field_manage."state" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."state" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."state" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."county" | type: table --
-- drop table if exists field_manage."county" cascade;
create table field_manage."county" (
	"county_id" smallserial not null,
	"fips_code" varchar(5) not null,
	"county_name" varchar(50) not null,
	"app_visible_b" boolean not null,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "county_pk" primary key ("county_id")

);
-- ddl-end --
alter table field_manage."county" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."county" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."county" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_activity" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_activity" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_activity_file" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_activity_file" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: "idx_last_name" | type: index --
-- drop index if exists field_manage."idx_last_name" cascade;
create index "idx_last_name" on field_manage."field_user"
	using btree
	(
	  "last_name"
	);
-- ddl-end --

-- object: "idx_address" | type: index --
-- drop index if exists field_manage."idx_address" cascade;
create index "idx_address" on field_manage."field"
	using btree
	(
	  "address"
	);
-- ddl-end --

-- object: "idx_field_name" | type: index --
-- drop index if exists field_manage."idx_field_name" cascade;
create index "idx_field_name" on field_manage."field"
	using btree
	(
	  "field_name"
	);
-- ddl-end --

-- object: "idx_field_geom" | type: index --
-- drop index if exists field_manage."idx_field_geom" cascade;
create index "idx_field_geom" on field_manage."field"
	using gist
	(
	  "field_geom"
	);
-- ddl-end --

-- object: "idx_access_geom" | type: index --
-- drop index if exists field_manage."idx_access_geom" cascade;
create index "idx_access_geom" on field_manage."field_access"
	using gist
	(
	  "access_geom"
	);
-- ddl-end --



-- object: "idx_old_geom" | type: index --
-- drop index if exists field_manage."idx_old_geom" cascade;
create index "idx_old_geom" on field_manage."field_geom_hist"
	using gist
	(
	  "old_geom"
	);
-- ddl-end --

-- object: "idx_field_attributes" | type: index --
-- drop index if exists field_manage."idx_field_attributes" cascade;
create index "idx_field_attributes" on field_manage."field"
	using gin
	(
	  "field_attributes"
	);
-- ddl-end --

-- object: "idx_old_attributes" | type: index --
-- drop index if exists field_manage."idx_old_attributes" cascade;
create index "idx_old_attributes" on field_manage."field_attribute_hist"
	using gin
	(
	  "old_attributes"
	);
-- ddl-end --

-- object: "idx_activity_geom" | type: index --
-- drop index if exists field_manage."idx_activity_geom" cascade;
create index "idx_activity_geom" on field_manage."field_activity"
	using gist
	(
	  "activity_geom"
	);
-- ddl-end --

-- object: field_manage."vendor_partner" | type: table --
-- drop table if exists field_manage."vendor_partner" cascade;
create table field_manage."vendor_partner" (
	"vendor_partner_id" smallserial not null,
	"vendor_name" varchar(75) not null,
	"vendor_desc" text,
	"main_api_url" varchar(250),
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "vendor_partner_pk" primary key ("vendor_partner_id")

);
-- ddl-end --
alter table field_manage."vendor_partner" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."vendor_partner" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."vendor_partner" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: field_manage."field_user_vendor" | type: table --
-- drop table if exists field_manage."field_user_vendor" cascade;
create table field_manage."field_user_vendor" (
	"field_user_vendor_id" serial not null,
	"field_user_id" integer not null,
	"vendor_partner_id" smallint not null,
	"vendor_login" varchar(50),
	"vendor_encpassword" text,
	"created_datetz" timestamptz not null,
	"edit_datetz" timestamptz,
	"last_edit_user_id" integer,
	constraint "field_user_vendor_pk" primary key ("field_user_vendor_id")

);
-- ddl-end --
alter table field_manage."field_user_vendor" owner to postgres;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field_user_vendor" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field_user_vendor" add constraint "field_user_fk" foreign key ("field_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "vendor_partner_fk" | type: constraint --
-- alter table field_manage."field_user_vendor" drop constraint if exists "vendor_partner_fk" cascade;
alter table field_manage."field_user_vendor" add constraint "vendor_partner_fk" foreign key ("vendor_partner_id")
references field_manage."vendor_partner" ("vendor_partner_id") match full
on delete restrict on update cascade;
-- ddl-end --

-- object: "field_user_fk1" | type: constraint --
-- alter table field_manage."field_user_vendor" drop constraint if exists "field_user_fk1" cascade;
alter table field_manage."field_user_vendor" add constraint "field_user_fk1" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: "field_user_vendor_fk" | type: constraint --
-- alter table field_manage."field_activity_file" drop constraint if exists "field_user_vendor_fk" cascade;
alter table field_manage."field_activity_file" add constraint "field_user_vendor_fk" foreign key ("vendor_partner_id")
references field_manage."field_user_vendor" ("field_user_vendor_id") match full
on delete set null on update cascade;
-- ddl-end --

-- object: "field_user_fk" | type: constraint --
-- alter table field_manage."field" drop constraint if exists "field_user_fk" cascade;
alter table field_manage."field" add constraint "field_user_fk" foreign key ("last_edit_user_id")
references field_manage."field_user" ("field_user_id") match full
on delete set null on update cascade;
-- ddl-end --

alter table field alter column field_geom set not null;
alter table field_geom_hist alter column old_geom set not null;
alter table field_access alter column access_geom set not null;
