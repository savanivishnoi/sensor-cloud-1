package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by Naks on 09-May-16.
 * Service interface for all mongodb operation related activities
 */
public interface IMongoService {

    boolean updateDB(String barometerReadings);

    List<BarometerSensor> listSensorData(String userId, String sensorId, String startTime, String endTime);

    @Deprecated
    JSONArray search(String latitude, String longitude);

    boolean checkSensors(String latitude, String longitude);

    @Deprecated
    JSONArray searchAll();

}
