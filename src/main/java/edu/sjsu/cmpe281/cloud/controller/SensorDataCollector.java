package edu.sjsu.cmpe281.cloud.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import edu.sjsu.cmpe281.cloud.service.ICencoosConnect;
import edu.sjsu.cmpe281.cloud.service.IMongoService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Savani
 *         Controller API for updating sensor data in mongodb by reading from cencoos
 */

@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "/sensor")
public class SensorDataCollector {

    private static final Logger logger = LoggerFactory.getLogger(SensorDataCollector.class);

    @Autowired
    ICencoosConnect cencoosConnect;

    @Autowired
    IMongoService mongoService;

    /**
     * Update the backend (mongodb) by fetching data from CENCOOS website
     *
     * @param timestamp (required parameter) Date to fetch the data from CENCOOS server
     * @return HttpStatus OK if data fetching process completes successfully, else HttpStatus BAD REQUEST
     */
    @RequestMapping(method = RequestMethod.GET, value = "/update/backend")
    public ResponseEntity getCencoosData(@RequestParam(value = "timestamp", required = true) String timestamp) {
        boolean fetchStatus = cencoosConnect.fetchData(timestamp);
        if (fetchStatus)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    /**
     * Fetch data from MongoDb database
     *
     * @param input values of sensor coordinates in format like <latitude,longitude>
     * @return returns the sensor readings based on coordinates
     */
    @RequestMapping(method = RequestMethod.GET, value = "/fetch")
    public ResponseEntity fetchData(@RequestParam(value = "latitude", required = false) String latitude,
                                    @RequestParam(value = "longitude", required = false) String longitude) {

        ObjectMapper objMap = new ObjectMapper();
        objMap.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        JSONArray sensorReadings;

        try {
            if (latitude.isEmpty() && longitude.isEmpty()) {
                sensorReadings = mongoService.searchAll(); //retrieve all results
            } else {
                sensorReadings = mongoService.search(latitude, longitude); //search on the basis of latitude & longitude
            }
        } catch (Exception e) {
            logger.error("Exception: "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sensorReadings.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getReadingsByTimestamp(@RequestParam("timestamp") String timestamp) {
        try {
            //test with this timestamp -> 2016-03-11T00:00:00Z
            BarometerSensor bs = mongoService.getBarometerSensorDataByTime(timestamp);
            return new ResponseEntity<>(bs, HttpStatus.OK);
        } catch (IllegalStateException e) {
            logger.error("IllegalStateExecption: "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}