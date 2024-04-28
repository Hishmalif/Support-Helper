package org.hishmalif.supporthelperbot.service.geocode;

import org.hishmalif.supporthelperbot.data.GeoData;

public interface Geocode {
    GeoData getLocation(String geo, String uuid);
}