package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.dto.IVirtualSensorCrud;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Yassaman
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Service
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
    public void updateVirtualSensor(VirtualSensor virtualSensorData) {
        vsDBOperations.updateVirtualSensor(virtualSensorData);
    }

    @Override
    public VirtualSensor getVirtualSensor(String userId, String sensorId) {
        return vsDBOperations.getVirtualSensor(userId, sensorId);
    }

    @Override
    public List<VirtualSensor> getVirtualSensorListByUserId(String userId) {
        return vsDBOperations.getVirtualSensorListByUserId(userId);
    }

    @Override
    public void deleteVirtualSensor(String userId, String sensorId) {
        vsDBOperations.deleteVirtualSensor(userId, sensorId);
    }

    @Override
    public void storeInDB(JSONObject vsJsonObject) {
        vsDBOperations.storeInDB(vsJsonObject);
    }

    @Override
    public ObjectId createSensor(String userId, String sensorId, String name, String latitude, String longitude) {
        return vsDBOperations.createSensor(userId, sensorId, name, latitude, longitude);
    }

    @Override
    public List<VirtualSensor> getAllSensors() {
        return vsDBOperations.getAllSensors();
    }
}
