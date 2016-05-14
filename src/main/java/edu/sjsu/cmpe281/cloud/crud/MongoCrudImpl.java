package edu.sjsu.cmpe281.cloud.crud;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import edu.sjsu.cmpe281.cloud.enums.MongoCollection;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naks on 09-May-16.
 * Implementation of MongoCrud Interface
 */

@SuppressWarnings("SpringJavaAutowiringInspection")
@Repository
public class MongoCrudImpl implements IMongoCrud {

    private static final Logger logger = LoggerFactory.getLogger(MongoCrudImpl.class);
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public List<BarometerSensor> getDataByTimeRangeAndCoordinates(String startTime, String endTime, String latitude, String longitude) {

        Criteria range = (new Criteria()
                .andOperator(
                        Criteria.where("time").gte(startTime),
                        Criteria.where("time").lte(endTime)
                )
        );

        Criteria coordinates = (new Criteria()
                .andOperator(
                        Criteria.where("latitude").is(latitude),
                        Criteria.where("longitude").is(longitude)
                )
        );


        Criteria resultantCriteria = new Criteria().andOperator(range, coordinates);
        Query query = new Query(resultantCriteria);

        return mongoOperations.find(query, BarometerSensor.class, MongoCollection.BarometerData.toString());
    }

    @Override
    public void updateDB(String barometerReadings) {
        JSONArray barometerReadingsArr = null;
        DBObject document;
        Object o;
        DBCollection table = mongoOperations.getCollection(MongoCollection.BarometerData.toString());

        try {
            barometerReadingsArr = new JSONArray(barometerReadings);
            logger.info("Updating Database... Please wait!");
            for (int i = 0; i < barometerReadingsArr.length(); i++) {
                o = com.mongodb.util.JSON.parse(barometerReadingsArr.get(i).toString());
                document = (DBObject) o;
                table.insert(document);
            }
        } catch (JSONException e) {
            logger.error("JSONException: " + e.getMessage());
            throw new ExceptionInInitializerError("Error while database writing... " + e);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            throw new ExceptionInInitializerError("Error while database writing... " + e);
        }

    }

    //search based on latitude and longitude
    @Override
    public JSONArray search(String latitude, String longitude) {
        JSONArray sensorReadings = new JSONArray();
        BasicDBObject nQuery = new BasicDBObject();
        List<BasicDBObject> ls_srch = new ArrayList<BasicDBObject>();
        DBCollection table = mongoOperations.getCollection(MongoCollection.BarometerData.toString());
        DBCursor cursor1 = null;
        try {
            ls_srch.add(new BasicDBObject("latitude", latitude));
            ls_srch.add(new BasicDBObject("longitude", longitude));
            nQuery.put("$and", ls_srch);
            cursor1 = table.find(nQuery);
            while (cursor1.hasNext()) {
                sensorReadings.put(JSON.serialize(cursor1.next()));
            }
        } catch (Exception e) {
            logger.error("Exception: while searching : " + e.getMessage());
        }
        return sensorReadings;
    }

    // retrieve all records
    @Override
    public JSONArray searchAll() {
        JSONArray sensorReadings = new JSONArray();
        DBCollection table = mongoOperations.getCollection(MongoCollection.BarometerData.toString());
        DBCursor cursor = table.find();
        System.out.println("Total  :" + cursor.count());
        while (cursor.hasNext()) {
            sensorReadings.put(JSON.serialize(cursor.next()));
        }
        return sensorReadings;
    }
}
