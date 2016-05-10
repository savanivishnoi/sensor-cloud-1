package edu.sjsu.cmpe281.cloud.dto;

import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;

/**
 * Created by Naks on 09-May-16.
 * Crud Operations on MongDB -> mlab
 */
public interface IMongoCrud {
    BarometerSensor getCollectionByTime(String timeStamp, String collectionName);

    public void updateDB(String barometerReadings);

    public JSONArray search(String latitude, String longitude);

    public JSONArray searchAll();
}
