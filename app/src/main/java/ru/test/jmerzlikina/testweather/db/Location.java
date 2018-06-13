package ru.test.jmerzlikina.testweather.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * локация из списка доступных локаций
 */
@Entity
public class Location {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer code;
    private String location;
    private String coordLon;
    private String coordLat;

    public Location(Integer code, String location, String coordLon, String coordLat) {
        this.code = code;
        this.location = location;
        this.coordLon = coordLon;
        this.coordLat = coordLat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCode() {
        return code;
    }

    public String getCoordLon() {
        return coordLon;
    }

    public String getCoordLat() {
        return coordLat;
    }

}
