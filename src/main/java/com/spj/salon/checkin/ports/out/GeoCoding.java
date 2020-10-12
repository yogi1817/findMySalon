package com.spj.salon.checkin.ports.out;

import com.google.maps.model.GeocodingResult;

import java.io.IOException;

public interface GeoCoding {
    GeocodingResult[] findGeocodingResult(String addessOrZip) throws IOException;
}
