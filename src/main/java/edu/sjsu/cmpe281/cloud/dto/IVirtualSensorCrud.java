package edu.sjsu.cmpe281.cloud.dto;

import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Yassaman
 */
public interface IVirtualSensorCrud {
    void storeInDB(JSONObject virtualSensorData);

    @Deprecated
    List<VirtualSensor> getVirtualSensorListByUserId(String sensorId, String userId);

    List<VirtualSensor> groupVirtualSensorListByLatLong(String userId, String latitude, String longitude);

    List<VirtualSensor> groupVirtualSensorListByTimeCreated(String userId, String timeCreated);

    void updateVirtualSensorStatus(String userId, String sensorId, String state);

    List<VirtualSensor> getAllSensors();
}
