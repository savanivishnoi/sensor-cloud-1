package edu.sjsu.cmpe281.cloud.controller;

import edu.sjsu.cmpe281.cloud.dto.VirtualSensorCrudImpl;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import edu.sjsu.cmpe281.cloud.service.ICencoosConnect;
import edu.sjsu.cmpe281.cloud.service.IMongoService;
import edu.sjsu.cmpe281.cloud.service.IVirtualSensor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Savani
 *         Controller API for updating sensor data in mongodb by reading from cencoos
 */

@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "/api/sensor")
public class SensorDataCollector {

    private static final Logger logger = LoggerFactory.getLogger(SensorDataCollector.class);

    @Autowired
    IMongoService mongoService;
    @Autowired
    IVirtualSensor virtualSensor;
    @Autowired
    ICencoosConnect cencoosConnect;



    @RequestMapping(method = RequestMethod.GET, value = "/update/backend")
    public ResponseEntity getCencoosData(@RequestParam(value = "timestamp", required = true) String timestamp) {
        boolean fetchStatus = cencoosConnect.fetchData(timestamp);
        if (fetchStatus)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    /**
     * Fetch data from mongo db database
     *
     * @param userId    user id
     * @param sensorId  unique sensor id
     * @param latitude  latitude of a sensor
     * @param longitude longitude of a sensor
     * @param startTime start time to fetch the data
     * @param endTime   end time to fetch the data
     * @return JSON list of sensor data
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{user_id}/{sensor_id}/data")
    public ResponseEntity fetchData(@RequestParam(value = "time1") String startTime,
                                    @RequestParam(value = "time2") String endTime,
                                    @PathVariable(value = "user_id")String userId,
                                    @PathVariable(value = "sensor_id") String sensorId) {
        List<BarometerSensor> sensorList;
        try {
            sensorList = mongoService.listSensorData(userId,sensorId,startTime, endTime);
            logger.info("GET call on /sensor/data has been completed successfully.");
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sensorList.toString(), HttpStatus.OK);
    }

    /**
     * Check whether sensor coordinates exists in database or not.
     *
     * @param latitude  latitude of a sensor
     * @param longitude longitude of a sensor
     * @return true if sensor exist, else false.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ResponseEntity checkData(@RequestParam(value = "latitude") String latitude,
                                    @RequestParam(value = "longitude") String longitude) {
        try {
            JSONObject responseObject = new JSONObject();
            if (mongoService.checkSensors(latitude, longitude)) {
                responseObject.put("status", "200");
                responseObject.put("message", "coordinates exist");
            } else {
                responseObject.put("status", "400");
                responseObject.put("message", "bad request");
            }
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{user_id}/pricing")
    public ResponseEntity fetchPricing(@PathVariable(value = "user_id") String userId){
         JSONArray responseObject = new JSONArray();
        JSONObject jsonObject =  new JSONObject();
        VirtualSensorCrudImpl vs = new VirtualSensorCrudImpl();
        List<VirtualSensor> virtualSensorsList = virtualSensor.getVirtualSensorListByUserId(userId);
        if(virtualSensorsList.size() == 0){ return new ResponseEntity("No sensors.",HttpStatus.BAD_REQUEST); }
        Double price = 0.0;
        String timeCreated = null;
        String timeUpdated = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        long diff;
        try {
        for(int i = 0 ; i < virtualSensorsList.size(); i++){

            timeCreated = ( virtualSensorsList.get(i).getTimecreated());
            timeUpdated = (virtualSensorsList.get(i).getTimeupdated());
           // diff = timeUpdated - timeCreated;
            Date date1 = formatter.parse(timeCreated);
            Date date2 = formatter.parse(timeUpdated);
            diff = date2.getTime() - date1.getTime();
                jsonObject.put("sensorid", virtualSensorsList.get(i).getSensorid());
                jsonObject.put("totaltime", diff);
            }
            responseObject.put(jsonObject);
            //price =  price + 0.01 * diff;
      }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
    }
}