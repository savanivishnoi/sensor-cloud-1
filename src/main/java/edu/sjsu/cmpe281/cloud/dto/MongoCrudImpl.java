package edu.sjsu.cmpe281.cloud.dto;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import edu.sjsu.cmpe281.cloud.enums.MongoCollection;
import edu.sjsu.cmpe281.cloud.model.BarometerSensor;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
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
    public BarometerSensor getCollectionByTime(String timeStamp, String collectionName) {
        BarometerSensor barometerSensorCollection = mongoOperations.findOne(new Query(Criteria.where("time")
                .is(timeStamp)), BarometerSensor.class, collectionName);
        return barometerSensorCollection;
    }

    @Override
    public List<VirtualSensor> getDataByTimestampRange(String startTime, String endTime, String collectionName){

        Criteria startRange= Criteria.where("time").gte(startTime);
        Criteria endRange= Criteria.where("time").lte(endTime);
        Criteria rangeCriteria= new Criteria().andOperator(startRange,endRange);
        Query query = new Query(rangeCriteria);

        return mongoOperations.find(query,VirtualSensor.class,collectionName);
    }

    @Override
    public void updateDB(String barometerReadings) {
        JSONArray barometerReadingsArr = null;
        DBObject document;
        Object o;
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

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
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());
        DBCursor cursor1 = null;
        try {
            ////// TODO: 09-May-16 Remove the hard-coded latitude and longitude
            ls_srch.add(new BasicDBObject("latitude", "40.9833333333333"));
            ls_srch.add(new BasicDBObject("longitude", "-124.1"));
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
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());
        DBCursor cursor = table.find();
        System.out.println("Total  :" + cursor.count());
        while (cursor.hasNext()) {
            sensorReadings.put(JSON.serialize(cursor.next()));
        }
        return sensorReadings;
    }
}
