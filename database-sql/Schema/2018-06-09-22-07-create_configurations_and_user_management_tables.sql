---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
-- USER MANAGEMENT TABLES
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------

--
-- This is the version table, used to control schema creation and system updates:
--
create table version (
  id raw(16) default sys_guid() primary key,
  file_name varchar(256) unique not null,
  version varchar(64) unique not null,
  create_date timestamp with time zone  default current_timestamp not null
);

-- This table stores the entity history in the entire database. It's a JSON
-- database, indexed on the id field, to enable fast search
create table entity_history (
  -- The primary key is a sequential big number, to ensure order.
  -- This is the only table in the entire system not using a GUID id.
  id numeric(38,0) not null primary key,
  entity_name varchar(1024) not null,
  entity_id raw(16) not null,
  entity_data clob not null check(entity_data is json),
  create_date timestamp with time zone default current_timestamp not null,
  constraint entity_history_name_id_uidx unique(entity_name, entity_id)
);

--
-- This is the seed control table, used to control seeding:
--
create table seed (
  id raw(16) default sys_guid() primary key,
  file_name varchar(256) unique not null,
  create_date timestamp with time zone default current_timestamp not null
);

-- Create the general configurations table:
create table configuration (
  id raw(16) default sys_guid() primary key,
  name varchar2(191) unique not null,
  value varchar2(2048) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- Create the table to store temporary runtime data on the system:
create table runtime (
  id raw(16) default sys_guid() primary key,
  name varchar(256) unique not null,
  value varchar(1024) null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- Table used to control critical sections:
create table lock_control (
  id raw(16) default sys_guid() primary key,
  name varchar(256) unique not null,
  expiration timestamp with time zone not null,
  status char(1) not null,
  owner varchar(256) not null,
  constraint lock_control_status_ck check(status in ('L', 'F'))
);

-- Create the Phone-number entity table:
create table phone_number (
  id raw(16) default sys_guid() primary key,
  "number" varchar(32) unique not null,
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- Create the E-mail entity table:
create table email_address (
  id raw(16) default sys_guid() primary key,
  address varchar(256) unique not null,
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- Create the Person entity table - this is just a reference for an external unstructured
-- data repository, since Person information and identification is unstructured. As an
-- example, the document identification for a person, in Brazil, consists of two different
-- numbers, the CPF, and the RG, and a person can have multiple RGs, one for each Brazilian
-- state. This same person can be a European Union citizen, identified by the Passport, and
-- an American citizen, identified by the social security number.
create table person (
  id raw(16) default sys_guid() primary key,
  name varchar(512) not null,
  birthday date null,
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- This is the User table, having BCrypt2 password hashes, but with
-- no token management on it:
create table user_ (
  id raw(16) default sys_guid() primary key,
  person_id raw(16) null references person(id),
  password varchar(256) not null,
  name varchar(256) unique not null,
  alias varchar(256) not null,
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

-- User->email address and User->phone number relationships:
create table user__email_address (
  id raw(16) default sys_guid() primary key,
  user_id raw(16) not null references user_(id),
  email_address_id raw(16) not null references email_address(id),
  preference_order integer not null check(preference_order >= 0),
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null,
  constraint user_id_email_address_id_uidx
      unique(user_id, email_address_id, preference_order)
);

create table user__phone_number (
  id raw(16) default sys_guid() primary key,
  user_id raw(16) not null references user_(id),
  phone_number_id raw(16) not null references phone_number(id),
  preference_order integer not null check(preference_order >= 0),
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null,
  constraint user_id_phone_number_id_uidx
      unique(user_id, phone_number_id, preference_order)
);

-- The access token is an ENTITY, with complete life cycle, which
-- can be used for user AUTHORIZATION.
create table access_token (
  id raw(16) default sys_guid() primary key,
  token varchar(256) unique not null,
  expiration timestamp with time zone,
  user_id raw(16) not null references user_(id),
  status varchar(64) not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

create table role (
  id raw(16) default sys_guid() primary key,
  name varchar(245) unique not null,
  created_at timestamp with time zone default current_timestamp not null,
  updated_at timestamp with time zone default current_timestamp not null
);

create table user__role (
  id raw(16) default sys_guid() primary key,
  user_id raw(16) not null references user_(id),
  role_id raw(16) not null references role(id),
  created_at timestamp with time zone default current_timestamp not null,
  constraint user_id_role_id_uidx unique(user_id, role_id)
);

-- Increments the database version:
insert into version (file_name, version, create_date)
values ('2018-06-09-22-07-create_configurations_anduser__management_tables.sql', '0.0.0', current_timestamp);
