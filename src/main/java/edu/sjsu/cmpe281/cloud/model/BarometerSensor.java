package edu.sjsu.cmpe281.cloud.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by Naks on 13-Apr-16.
 * POJO class for Barometer Sensors.
 */


@Document(collection = "BarometerData")
public class BarometerSensor implements Serializable{

    @Id
    private String id;
    private String time;
    private String latitude;
    private String longitude;
    private String depth;
    private String station;
    private String parameter;
    private String unit;
    private String value;
    private String coordinate;

    public BarometerSensor(String time, String latitude, String longitude, String depth, String station, String parameter, String unit, String value) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.station = station;
        this.parameter = parameter;
        this.unit = unit;
        this.value = value;
        this.coordinate = latitude + "," + longitude;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonIgnore
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonIgnore
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonIgnore
    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    @JsonIgnore
    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    @JsonIgnore
    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @JsonIgnore
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "BarometerSensor [id=" + id + ", time=" + time + ", latitude=" + latitude + ", longitude" + longitude + ", depth" + depth + ", station" + station + ", parameter" + parameter + ", unit" + unit + ", value" + value + ", coordinate" + coordinate + "]";
    }
}

