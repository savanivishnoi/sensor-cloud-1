package edu.sjsu.cmpe281.cloud.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import edu.sjsu.cmpe281.cloud.service.IVirtualSensor;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yassaman
 */

@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "api/sensor")
public class VirtualSensorController {

    private static final Logger logger = LoggerFactory.getLogger(VirtualSensorController.class);

    @Autowired
    IVirtualSensor virtualSensorService;

    @RequestMapping(method = RequestMethod.GET, value = "/{userid}")
    public ResponseEntity getAllSensors(@PathVariable("userid") String userId) {

        ObjectMapper objMap = new ObjectMapper();
        objMap.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        JSONArray userSensors;

        Gson gson = new Gson();
        try {

            String vsensorstr= gson.toJson(virtualSensorService.getVirtualSensorListByUserId(userId).toString());
            userSensors = new JSONArray(gson.toJson(virtualSensorService.getVirtualSensorListByUserId(userId)));
        } catch (Exception e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userSensors.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userid}/{sensorid}")
    public ResponseEntity getSensor(@PathVariable("userid") String userId,
                                    @PathVariable("sensorid") String sensorId) {

        ObjectMapper objMap = new ObjectMapper();
        objMap.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        JSONObject userSensor;

        Gson gson = new Gson();
        try {
            userSensor = new JSONObject (gson.toJson(virtualSensorService.getVirtualSensor(userId, sensorId)));
        } catch (Exception e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userSensor.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createSensor(@RequestBody VirtualSensor virtualSensorData) {
        try {

//            // check if the record exists in db
//            VirtualSensor vs = virtualSensorService.getVirtualSensor(virtualSensorData.getUserid(), virtualSensorData.getSensorid());
//            if (vs != null) {
//                virtualSensorService.updateVirtualSensor(virtualSensorData);
//                JSONObject responseObject = new JSONObject();
//                responseObject.put("status", "200");
//                responseObject.put("message", "Virtual sensor was updated successfully");
//                responseObject.put("userId", virtualSensorData.getUserid());
//                responseObject.put("sensorId", virtualSensorData.getSensorid());
//                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
//            }

//            else {
                ObjectId id = virtualSensorService.createSensor(virtualSensorData.getUserid(), virtualSensorData.getSensorid(),
                        virtualSensorData.getName(), virtualSensorData.getLatitude(), virtualSensorData.getLongitude());
                if (id != null) {
                    JSONObject responseObject = new JSONObject();
                    responseObject.put("status", "201");
                    responseObject.put("message", "Virtual sensor created successfully");
                    responseObject.put("id", id.toString());
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("creation failed userId: " + virtualSensorData.getUserid() +
                            " - sensorId: " + virtualSensorData.getSensorid(),
                            HttpStatus.BAD_REQUEST);
                }
//            }
        }
        catch (JSONException e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>("failed " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
            public ResponseEntity deleteSensor(@RequestParam(value = "userid", required = true) String userId,
                                               @RequestParam(value = "sensorid", required = true) String sensorId) {
        try {
//            VirtualSensor vs = virtualSensorService.getVirtualSensor(userId, sensorId);
//            if(vs != null) {
                virtualSensorService.deleteVirtualSensor(userId, sensorId);
                JSONObject responseObject = new JSONObject();
                responseObject.put("status","202");
                responseObject.put("message", "Virtual sensor was marked for deletion successfully");
                responseObject.put("Virtual Sensor: ", sensorId);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
//            }
//            else {
//                logger.error("Unable to delete. User with id " + userId + " and sensorId " + sensorId + " not found");
//                return new ResponseEntity<>("deletion failed " + userId + " - " + userId, HttpStatus.NOT_FOUND);
//            }
        }
        catch (JSONException e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>("failed " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userid}/update", consumes = "application/json")
    public ResponseEntity updateSensor(@PathVariable("userid") String userId,
                                       @RequestBody VirtualSensor virtualSensorData) {
        try {
            String sensorId= virtualSensorData.getSensorid();
            VirtualSensor vs = virtualSensorService.getVirtualSensor(userId, sensorId);
            if(vs != null) {
                virtualSensorService.updateVirtualSensor(virtualSensorData);
                JSONObject responseObject = new JSONObject();
                responseObject.put("status","200");
                responseObject.put("message", "Virtual sensor was uapdated successfully");
                responseObject.put("Virtual Sensor: ", virtualSensorData.toString());
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            }
            else {
                logger.error("Unable to update. User with id " + userId + " and sensorId " + sensorId + " not found");
                return new ResponseEntity<>("update failed " + userId + " - " + userId, HttpStatus.NOT_FOUND);
            }
        }
        catch (JSONException e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>("failed " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
