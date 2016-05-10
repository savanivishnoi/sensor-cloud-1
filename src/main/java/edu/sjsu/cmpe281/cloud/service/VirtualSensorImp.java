package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.dto.IVirtualSensorCrud;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Yassaman
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class VirtualSensorImp implements IVirtualSensor {

    @Autowired
    IVirtualSensorCrud vsDBOperations;

    @Override
    public List<VirtualSensor> getSensorMetadata(String sensorId, String userId) {
        return vsDBOperations.getVirtualSensorListByUserId(sensorId, userId);
    }

    @Override
    public List<VirtualSensor> groupVirtualSensorListByLatLong(String userId, String lat, String lng) {
        return vsDBOperations.groupVirtualSensorListByLatLong(userId, lat, lng);
    }

    @Override
    public List<VirtualSensor> groupVirtualSensorListByTimeCreated(String userId, String timeCreated) {
        return vsDBOperations.groupVirtualSensorListByTimeCreated(userId, timeCreated);
    }

    @Override
    public void updateVirtualSensorStatus(String userId, String sensorId, String state) {
        vsDBOperations.updateVirtualSensorStatus(userId, sensorId, state);
    }

    @Override
    public void storeInDB(JSONObject vsJsonObject) {
        vsDBOperations.storeInDB(vsJsonObject);
    }
}
