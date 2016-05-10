package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;

/**
 * Created by Naks on 09-May-16.
 * Service interface for all mongodb operation related activities
 */
public interface IMongoService {

    BarometerSensor getBarometerSensorDataByTime(String timestamp);

    public boolean updateDB(String barometerReadings);

    public JSONArray search(String latitude, String longitude);

    public JSONArray searchAll();

}
