package ru.test.jmerzlikina.testweather.object;

import com.google.gson.annotations.SerializedName;

/**
 * координаты локации (долгота + широта)
 */
public class Coord {

    // долгота
    @SerializedName("lon")
    private String lon;

    // широта
    @SerializedName("lat")
    private String lat;

    public Coord(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
