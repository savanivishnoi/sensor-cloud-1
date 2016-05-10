package edu.sjsu.cmpe281.cloud.dto;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import edu.sjsu.cmpe281.cloud.enums.MongoCollection;
import edu.sjsu.cmpe281.cloud.enums.SensorState;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yassaman
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class VirtualSensorCrudImpl implements IVirtualSensorCrud {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void storeInDB(JSONObject virtualSensorData) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        // should I check if the document exists in the collection first?
        System.out.println("*** VirtualSensorDBOperations: " + virtualSensorData.toString());
        BasicDBObject document = new BasicDBObject();

        try {
            Date timeCreated = new Date(); // timecreated and timeupdated are the same at this point

            // a unique id is generated
            document.put("sensorid", virtualSensorData.get("sensorid"));
            document.put("userid", virtualSensorData.get("userid"));
            document.put("latitude", virtualSensorData.get("latitude"));
            document.put("longitude", virtualSensorData.get("longitude"));
            document.put("timecreated", timeCreated.toString());
            document.put("timeupdated", timeCreated.toString());
            document.put("state", SensorState.RUNNING.toString());

            table.insert(document);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } /*finally {
            dbClient.close();
        }*/
    }

    @Override
    public List<VirtualSensor> getVirtualSensorListByUserId(String sensorId, String userId) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());
        Gson gson = new Gson();
        List<VirtualSensor> virtualSensorList = new ArrayList<>();

        BasicDBObject dbQuery = new BasicDBObject();
        List<BasicDBObject> query_parameters = new ArrayList<>();
        DBCursor dbCursor = null;

        try {

            query_parameters.add(new BasicDBObject("userid", userId));
            query_parameters.add(new BasicDBObject("sensorid", sensorId));
            dbQuery.put("$and", query_parameters);

            // execute the search query
            dbCursor = table.find(dbQuery);

            while (dbCursor.hasNext()) {
                DBObject obj = dbCursor.next();
                VirtualSensor vs = gson.fromJson(obj.toString(), VirtualSensor.class);
                virtualSensorList.add(vs);
            }
            return virtualSensorList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } /*finally {
            dbClient.close();
        }*/
    }

    @Override
    public List<VirtualSensor> groupVirtualSensorListByLatLong(String userId, String lat, String lng) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());
        Gson gson = new Gson();
        List<VirtualSensor> virtualSensorList = new ArrayList<>();

        BasicDBObject dbQuery = new BasicDBObject();
        List<BasicDBObject> query_parameters = new ArrayList<>();
        DBCursor dbCursor = null;

        try {

            query_parameters.add(new BasicDBObject("userid", userId));
            query_parameters.add(new BasicDBObject("latitude", lat));
            query_parameters.add(new BasicDBObject("longitude", lng));
            dbQuery.put("$and", query_parameters);

            // execute the search query
            dbCursor = table.find(dbQuery);

            while (dbCursor.hasNext()) {
                DBObject obj = dbCursor.next();
                VirtualSensor vs = gson.fromJson(obj.toString(), VirtualSensor.class);
                virtualSensorList.add(vs);
            }
            return virtualSensorList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } /*finally {
            dbClient.close();
        }*/
    }

    @Override
    public List<VirtualSensor> groupVirtualSensorListByTimeCreated(String userId, String timeCreated) {

        Gson gson = new Gson();
        List<VirtualSensor> virtualSensorList = new ArrayList<>();

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        BasicDBObject dbQuery = new BasicDBObject();
        List<BasicDBObject> query_parameters = new ArrayList<>();
        DBCursor dbCursor = null;

        try {

            query_parameters.add(new BasicDBObject("userid", userId));
            query_parameters.add(new BasicDBObject("timecreated", timeCreated));
            dbQuery.put("$and", query_parameters);

            // execute the search query
            dbCursor = table.find(dbQuery);

            while (dbCursor.hasNext()) {
                DBObject obj = dbCursor.next();
                VirtualSensor vs = gson.fromJson(obj.toString(), VirtualSensor.class);
                virtualSensorList.add(vs);
            }
            return virtualSensorList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } /*finally {
            dbClient.close();
        }*/
    }

    // call this method for soft delete in which the state of the virtual sensor is changed to stop
    @Override
    public void updateVirtualSensorStatus(String userId, String sensorId, String state) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        try {
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("state", state));

            BasicDBObject searchQuery = new BasicDBObject().append("userid", userId).append("sensorid", sensorId);

            table.update(searchQuery, newDocument);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } /*finally {
            dbClient.close();
        }*/
    }
}
