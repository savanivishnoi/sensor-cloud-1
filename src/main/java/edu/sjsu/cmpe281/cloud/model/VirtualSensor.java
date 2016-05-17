package edu.sjsu.cmpe281.cloud.model;

import edu.sjsu.cmpe281.cloud.enums.SensorState;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yassaman
 */
public class VirtualSensor implements Serializable{

    @Id
    private String id;
//    private String sensorid;
    private String userid;
    private String name;
    private String latitude;
    private String longitude;
    private String timecreated;
    private String timeupdated;
    private SensorState state;
    // For now there's only one type. Add an attribute for type later

    public VirtualSensor() {
        super();
    }

    public VirtualSensor(String id, String userId, String name, String latitude, String longitude) {
        super();

        Date currentDate = new Date();

        this.id = id;
//        this.sensorid = sensorId;
        this.userid = userId;
        this.name= name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timecreated = currentDate.toString();
        this.timeupdated = currentDate.toString();
        this.state = SensorState.RUNNING;
    }

    public VirtualSensor(String id, String userId, String name, String latitude, String longitude, String timeCreated,
                         String timeUpdated, SensorState state) {
        super();
        this.id = id;
//        this.sensorid = sensorId;
        this.userid = userId;
        this.name= name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timecreated = timeCreated;
        this.timeupdated = timeUpdated;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getSensorid() {
//        return sensorid;
//    }
//
//    public void setSensorid(String sensorid) {
//        this.sensorid = sensorid;
//    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(String timecreated) {
        this.timecreated = timecreated;
    }

    public String getTimeupdated() {
        return timeupdated;
    }

    public void setTimeupdated(String timeupdated) {
        this.timeupdated = timeupdated;
    }

    public SensorState getState() {
        return state;
    }

    public void setState(SensorState state) {
        this.state = state;
    }

    public String toString() {

        return "VirtualSensor [ id= " + id + ", userid= " + userid + ", name= " + name +
                ", latitude= " + latitude + ", longitude= " + longitude +
                ", timecreated= " + timecreated + ", timeupdated= " + timeupdated +
                ", state= " + state.toString() + "]";
    }
}
