ALTER TABLE usa.address ADD CONSTRAINT unique_address UNIQUE (address_line_one, address_line_two, city ,zip , country);