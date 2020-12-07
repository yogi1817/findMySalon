create table usa.user_device(
     id BIGSERIAL PRIMARY KEY NOT NULL,
     native_device_id varchar(255),
     device_type_id int2,
     device_model varchar(255),
     gmt_offset_minutes int4,
     os_version varchar(255),
     push_notification_enabled bool,
     push_token varchar(255),
     application_version varchar(255),
     email varchar(255),
     last_logged_in_date TIMESTAMPTZ,
     date_added TIMESTAMPTZ,
     date_updated TIMESTAMPTZ
);