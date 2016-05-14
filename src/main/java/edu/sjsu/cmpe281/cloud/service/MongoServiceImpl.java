package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.dto.IMongoCrud;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Naks on 09-May-16.
 * Implementation of IMongoService interface
 */

@SuppressWarnings({"SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection"})
@Service
public class MongoServiceImpl implements IMongoService {

    private static final Logger logger = LoggerFactory.getLogger(MongoServiceImpl.class);

    @Autowired
    IMongoCrud mongoCrud;

    @Override
    public boolean updateDB(String barometerReadings) {
        try {
            mongoCrud.updateDB(barometerReadings);
            return true;
        } catch (UncategorizedMongoDbException e) {
            logger.error("UncategorizedMongoDbException: " + e.getMessage());
            throw new IllegalStateException("MongoException occurred in MongoServiceImpl.updateDB method");
        }
    }

    @Override
    public List<BarometerSensor> listSensorData(String startTime, String endTime, String latitude, String longitude){
            try{
                return mongoCrud.getDataByTimeRangeAndCoordinates(startTime,endTime,latitude,longitude);
            }
            catch (UncategorizedMongoDbException e){
                logger.error("UncategorizedMongoDbException: " + e.getMessage());
                throw new IllegalStateException("MongoException occurred in MongoServiceImpl.listSensorData method");
            }
    }

    @Override
    public JSONArray search(String latitude, String longitude) {
        try {
            return mongoCrud.search(latitude, longitude);
        } catch (UncategorizedMongoDbException e) {
            logger.error("UncategorizedMongoDbException: " + e.getMessage());
            throw new IllegalStateException("MongoException occurred in MongoServiceImpl.search method");
        }
    }

    @Override
    public JSONArray searchAll() {
        try {
            return mongoCrud.searchAll();
        } catch (UncategorizedMongoDbException e) {
            logger.error("UncategorizedMongoDbException: " + e.getMessage());
            throw new IllegalStateException("MongoException occurred in MongoServiceImpl.searchAll method");
        }
    }

}
