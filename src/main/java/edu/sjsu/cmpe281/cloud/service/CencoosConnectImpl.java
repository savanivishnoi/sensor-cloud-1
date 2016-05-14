package edu.sjsu.cmpe281.cloud.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * @author Savani
 *         Implementation class for ICencoosConnect interface
 */

@Service
public class CencoosConnectImpl implements ICencoosConnect {

    private static final Logger logger = LoggerFactory.getLogger(CencoosConnectImpl.class);

    @Autowired
    private IMongoService mongoService;

    /**
     * Implementation method to serve interface calls
     *
     * @param timestamp Timestamp of required data
     * @return Status of data fetch; if process not is completed successfully, it will return false
     */
    @Override
    public boolean fetchData(String timestamp) {
        try {
            JSONArray barometerReadings = sensorDataRetrieve(timestamp); //retrive data from cencoos
            createJSONObject(barometerReadings); //create and save in DB
            return true;
        } catch (Exception e) {
            logger.error("Exception : " + e.getMessage());
            return false;
        }
    }


    /**
     * To convert CENCOOS data in required JSON Object
     *
     * @param barometerReadings JSONArray of Barometric Pressure data
     * @return JSON String
     */
    private String createJSONObject(JSONArray barometerReadings) {
        String temp = null;
        String[] columns;
        ArrayList<BarometerSensor> readings = new ArrayList<BarometerSensor>();
        try {
            temp = barometerReadings.get(0).toString();
        } catch (JSONException e) {
            logger.error("JSONException: " + e.getMessage());
        }
        for (int i = 0; i < 2; i++) {
            temp = temp.replace("[", "").replace("]", "");
            columns = temp.split(",");
            BarometerSensor bs = new BarometerSensor(columns[0], columns[1],
                    columns[2], columns[3], columns[4], columns[7], columns[5], columns[6]);
            readings.add(bs);
        }
        Gson gson = new Gson();
        String json = gson.toJson(readings);

        try {
            mongoService.updateDB(json);  //update DB
        } catch (Exception e) {
            logger.error("Exception : " + e.getMessage());
            throw new ExceptionInInitializerError("DB Error" + e);
        }
        return json;

    }

    /**
     * Method to fetch Barometric Pressure Data from CENCOOS website
     *
     * @param timestamp Timestamp of required data
     * @return JSONArray of data
     */
    private JSONArray sensorDataRetrieve(String timestamp) {
        JSONObject erdapJSON = null;
        try {
            String temp = null;
            String url = "http://erddap.axiomdatascience.com/erddap/tabledap/"
                    + "cencoos_sensor_service.json?time,latitude,longitude,"
                    + "depth,station,unit,value,parameter&time>=" + timestamp + "&parameter=%22"
                    + "Barometric%20Pressure%22";
            URL erdapURL = new URL(url);
            URLConnection erdapURLConnection = erdapURL.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    erdapURLConnection.getInputStream()));
            StringBuilder responseStrBuilder = new StringBuilder();
            logger.info("reading barometric pressure data from erddap url...");
            while ((temp = br.readLine()) != null)
            responseStrBuilder.append(temp);
            erdapJSON = new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            logger.error("Exception : " + e.getMessage());
            throw new ExceptionInInitializerError("Error while reading from url!");
        }
        ObjectMapper objMap = new ObjectMapper();
        objMap.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        JSONArray barometerReadings = null;
        try {
            barometerReadings = (JSONArray) erdapJSON.getJSONObject("table").
                    getJSONArray("rows");
        } catch (JSONException e) {
            logger.error("JSONException : " + e.getMessage());
            e.printStackTrace();
        }
        return barometerReadings;

    }
}
