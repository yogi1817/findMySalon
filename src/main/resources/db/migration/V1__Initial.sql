create table usa.authorities(
    authority_id BIGSERIAL PRIMARY KEY,
    authority varchar(50) not null);

insert into usa.authorities values (1, 'USER');
insert into usa.authorities values (2, 'BARBER');
insert into usa.authorities values (3, 'SUPERUSER');

create table usa.user(
	user_id BIGSERIAL PRIMARY KEY NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	middle_name varchar(255),
	email varchar(255) UNIQUE NOT NULL,
	phone varchar(255),
	password varchar(255) NOT NULL,
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE,
	modify_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE,
	store_name varchar(255),
	login_source varchar(255),
	favourite_salon_id int8,
	verified BOOLEAN,
	authority_id int8 REFERENCES usa.authorities(authority_id));

create table usa.services(
	service_id BIGSERIAL PRIMARY KEY,
	service_name varchar(255),
	service_description varchar(255),
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);

INSERT INTO usa.services (service_name,service_description) VALUES
('Mens Haircut','Haircut');

INSERT INTO usa.services (service_name,service_description) VALUES
('Womens Haircut','Haircut');

INSERT INTO usa.services (service_name,service_description) VALUES
('Mens Shaving','Shaving');

create table usa.barber_services(
	barber_services_id BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	service_id int8 REFERENCES usa.services(service_id),
	time_to_perform int4,
	service_charges decimal,
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);

create table usa.address(
	address_id BIGSERIAL PRIMARY KEY,
	address_line_one varchar(255) NOT NULL,
	address_line_two varchar(255),
	city varchar(255) NOT NULL,
	state varchar(255) NOT NULL,
	zip varchar(255) NOT NULL,
	country varchar(255) NOT NULL,
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE,
	modify_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE,
	longitude DOUBLE PRECISION,
	latitude DOUBLE PRECISION,
	user_id int8 not null);

create table usa.facilities(
	facility_id BIGSERIAL PRIMARY KEY REFERENCES usa.user(user_id),
	facility_name varchar(255),
	facility_description varchar(255),
	create_date TIMESTAMPTZ,
	modify_date TIMESTAMPTZ);

create table usa.barber_facilities(
	barber_facilities_id BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	facility_id int8 REFERENCES usa.facilities(facility_id),
	facility_note varchar(255),
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);

create table usa.daily_barbers(
	daily_id BIGSERIAL PRIMARY KEY,
	barbers_description varchar(255),
	barbers_count int8,
	user_id int8 REFERENCES usa.user(user_id),
	create_timestamp TIMESTAMPTZ NOT NULL DEFAULT now());
	
create table usa.check_in(
	check_in_id BIGSERIAL PRIMARY KEY,
	user_mapping_id int8 REFERENCES usa.user(user_id),
	barber_mapping_id int8 REFERENCES usa.user(user_id),
	service_mapping_id int8 REFERENCES usa.services(service_id),
	create_timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
	eta INTEGER NOT NULL,
	description varchar(255),
	checked_out boolean);
	
create table usa.promotions(
	promotion_id BIGSERIAL PRIMARY KEY REFERENCES usa.user(user_id),
	promotion_name varchar(255) NOT NULL,
	promotion_value int8,
	promotion_description varchar(255),
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE,
	modify_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);

create table usa.barber_calendar(
	barber_calendar_id  BIGSERIAL PRIMARY KEY,
	user_id int8 REFERENCES usa.user(user_id),
	salon_open_time TIME ,
	salon_close_time TIME ,
	calendar_day varchar(255),
	calendar_date TIMESTAMPTZ,
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);
	
create table usa.zip_code_lookup(
	id  BIGSERIAL PRIMARY KEY,
	zip_code bigint unique,
	longitude DOUBLE PRECISION,
	latitude DOUBLE PRECISION,
	create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_DATE);