ALTER TABLE usa.check_in
    ADD COLUMN check_out_timestamp timestamptz;

ALTER TABLE usa.check_in
	RENAME COLUMN create_timestamp TO check_in_timestamp;