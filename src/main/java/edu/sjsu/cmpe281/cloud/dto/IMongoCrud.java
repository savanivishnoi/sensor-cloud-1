package edu.sjsu.cmpe281.cloud.dto;

import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by Naks on 09-May-16.
 * Crud Operations on MongDB -> mlab
 */
public interface IMongoCrud {
    BarometerSensor getCollectionByTime(String timeStamp, String collectionName);

    void updateDB(String barometerReadings);

    JSONArray search(String latitude, String longitude);

    JSONArray searchAll();

    List<VirtualSensor> getDataByTimestampRange(String startTime, String endTime, String collectionName);
}
