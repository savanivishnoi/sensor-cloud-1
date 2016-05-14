package edu.sjsu.cmpe281.cloud.crud;

import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by Naks on 09-May-16.
 * Crud Operations on MongDB -> mlab
 */
public interface IMongoCrud {

    void updateDB(String barometerReadings);

    JSONArray search(String latitude, String longitude);

    JSONArray searchAll();

    List<BarometerSensor> getDataByTimeRangeAndCoordinates(String startTime, String endTime, String latitude, String longitude);
}
