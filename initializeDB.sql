create schema usa;

create table usa.authorities(
    authority_id BIGSERIAL PRIMARY KEY,
    authority varchar(50) not null);

insert into usa.authorities values (1, 'USER');
insert into usa.authorities values (2, 'BARBER');
insert into usa.authorities values (3, 'SUPERUSER');

create table usa.user(
	user_id BIGSERIAL PRIMARY KEY,
	first_name varchar(255),
	last_name varchar(255),
	middle_name varchar(255),
	email varchar(255) UNIQUE,
	phone varchar(255),
	login_id varchar(255) UNIQUE,
	password varchar(255),
	create_date DATE,
	modify_date DATE,
	store_name varchar(255),
	login_source varchar(255),
	favourite_salon_id int8,
	verified BOOLEAN,
	authority_id int8 REFERENCES usa.authorities(authority_id));

create table usa.services(
	service_id BIGSERIAL PRIMARY KEY,
	service_name varchar(255),
	service_description varchar(255),
	create_date DATE,
	modify_date DATE);
	
create table usa.barber_services(
	barber_services_id BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	service_id int8 REFERENCES usa.services(service_id),
	time_to_perform int4,
	service_charges decimal,
	create_date DATE);

create table usa.address(
	address_id BIGSERIAL PRIMARY KEY,
	address_line_one varchar(255),
	address_line_two varchar(255),
	city varchar(255),
	state varchar(255),
	zip varchar(255),
	country varchar(255),
	create_date DATE,
	modify_date DATE,
	longitude DOUBLE PRECISION,
	latitude DOUBLE PRECISION,
	user_id int8 not null);

create table usa.facilities(
	facility_id BIGSERIAL PRIMARY KEY REFERENCES usa.user(user_id),
	facility_name varchar(255),
	facility_description varchar(255),
	create_date DATE,
	modify_date DATE);

create table usa.barber_facilities(
	barber_facilities_id BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	facility_id int8 REFERENCES usa.facilities(facility_id),
	facility_note varchar(255),
	create_date DATE);

create table usa.daily_barbers(
	daily_id BIGSERIAL PRIMARY KEY,
	barbers_description varchar(255),
	barbers_count int8,
	user_id int8 REFERENCES usa.user(user_id),
	create_timestamp TIMESTAMP);
	
create table usa.check_in(
	check_in_id BIGSERIAL PRIMARY KEY,
	user_mapping_id int8 REFERENCES usa.user(user_id),
	barber_mapping_id int8 REFERENCES usa.user(user_id),
	service_mapping_id int8 REFERENCES usa.services(service_id),
	create_timestamp TIMESTAMP,
	eta INTEGER,
	description varchar(255),
	checked_out boolean);
	
create table usa.promotions(
	promotion_id BIGSERIAL PRIMARY KEY REFERENCES usa.user(user_id),
	promotion_name varchar(255),
	promotion_value int8,
	promotion_description varchar(255),
	create_date DATE,
	modify_date DATE);

create table usa.barber_calendar(
	barber_calendar_id  BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	salon_open_time TIMESTAMP,
	salon_close_time TIMESTAMP,
	calendar_day varchar(255),
	calendar_date date,
	modify_date date);
	
create table usa.zip_code_lookup(
	id  BIGSERIAL PRIMARY KEY,
	zip_code bigint unique,
	longitude DOUBLE PRECISION,
	latitude DOUBLE PRECISION,
	create_date date);
	