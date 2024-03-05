package org.hishmalif.supporthelperbot.data;

import lombok.Data;

@Data
public class GeoData {
    private String ui;
    private String locality;
    private String street;
    private String house;
    private String latitude;
    private String longitude;

    public String getAddress() {
        final StringBuilder addressBuilder = new StringBuilder();

        if (locality != null) {
            addressBuilder.append(locality);
        }
        if (street != null) {
            addressBuilder.append(", ").append(street);
        }
        if (house != null) {
            addressBuilder.append(", ").append(house);
        }
        return addressBuilder.toString();
    }
}