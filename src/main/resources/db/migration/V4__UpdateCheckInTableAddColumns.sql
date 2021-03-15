ALTER TABLE usa.check_in
    ADD COLUMN updated_by BIGSERIAL,
    ADD COLUMN create_date DATE;