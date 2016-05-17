package edu.sjsu.cmpe281.cloud.controller;

import com.google.gson.Gson;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import edu.sjsu.cmpe281.cloud.service.IMongoService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(sensorList, HttpStatus.OK);
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
}