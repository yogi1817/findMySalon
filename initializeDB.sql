create schema usa;

create table usa.barber(
	barber_id BIGSERIAL PRIMARY KEY,
	first_name varchar(255),
	last_name varchar(255),
	middle_name varchar(255),
	email varchar(255),
	phone varchar(255),
	login_id varchar(255),
	password varchar(255),
	create_date DATE,
	modify_date DATE);

create table usa.user(
	user_id BIGSERIAL PRIMARY KEY,
	first_name varchar(255),
	last_name varchar(255),
	middle_name varchar(255),
	email varchar(255),
	phone varchar(255),
	login_type varchar(255),
	create_date DATE,
	modify_date DATE,
	favourite_salon_id int8 REFERENCES usa.barber(barber_id));

create table usa.services(
	service_id BIGSERIAL PRIMARY KEY,
	service_name varchar(255),
	service_description varchar(255),
	create_date DATE,
	modify_date DATE);
	
create table usa.barber_services(
	barber_services_id BIGSERIAL PRIMARY KEY,
	barber_id int8 REFERENCES usa.barber(barber_id),
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
	mapping_id int8 not null);
		
create table usa.facilities(
	facility_id BIGSERIAL PRIMARY KEY REFERENCES usa.barber(barber_id),
	facility_name varchar(255),
	facility_description varchar(255),
	create_date DATE,
	modify_date DATE);

create table usa.barber_facilities(
	barber_facilities_id BIGSERIAL PRIMARY KEY,
	barber_id int8 REFERENCES usa.barber(barber_id),
	facility_id int8 REFERENCES usa.facilities(facility_id),
	facility_note varchar(255),
	create_date DATE);

create table usa.daily_barbers(
	daily_id BIGSERIAL PRIMARY KEY,
	barbers_description varchar(255),
	barbers_count int8,
	barber_mapping_id int8 REFERENCES usa.barber(barber_id),
	create_timestamp TIMESTAMP);
	
create table usa.check_in(
	check_in_id BIGSERIAL PRIMARY KEY,
	user_mapping_id int8 REFERENCES usa.user(user_id),
	barber_mapping_id int8 REFERENCES usa.barber(barber_id),
	service_mapping_id int8 REFERENCES usa.services(service_id),
	create_timestamp TIMESTAMP,
	eta INTEGER,
	description varchar(255),
	checked_out boolean);
	
create table usa.promotions(
	promotion_id BIGSERIAL PRIMARY KEY REFERENCES usa.barber(barber_id),
	promotion_name varchar(255),
	promotion_value int8,
	promotion_description varchar(255),
	create_date DATE,
	modify_date DATE);

create table usa.barber_calendar(
	barber_calendar_id  BIGSERIAL PRIMARY KEY,
	barber_mapping_id int8 REFERENCES usa.barber(barber_id),
	salon_open_time TIMESTAMP,
	salon_close_time TIMESTAMP,
	calendar_day varchar(255),
	calendar_date date,
	modify_date date);