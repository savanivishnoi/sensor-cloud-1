package edu.sjsu.cmpe281.cloud.dto;

import com.google.gson.Gson;
import com.mongodb.*;
import edu.sjsu.cmpe281.cloud.enums.MongoCollection;
import edu.sjsu.cmpe281.cloud.enums.SensorState;
import edu.sjsu.cmpe281.cloud.model.VirtualSensor;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yassaman
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Service
public class VirtualSensorCrudImpl implements IVirtualSensorCrud {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void storeInDB(JSONObject virtualSensorData) {

        // check if the document exists in the db by calling getVirtualSensor(String userId, String sensorId) before calling this method
        System.out.println("*** VirtualSensorDBOperations: " + virtualSensorData.toString());
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        BasicDBObject document = new BasicDBObject();

        try {
            Date timeCreated = new Date(); // timecreated and timeupdated are the same at this point

            // a unique id is generated
            document.put("userid", virtualSensorData.get("userid"));
            document.put("sensorid", virtualSensorData.get("sensorid"));
            document.put("name", virtualSensorData.get("name"));
            document.put("latitude", virtualSensorData.get("latitude"));
            document.put("longitude", virtualSensorData.get("longitude"));
            document.put("timecreated", timeCreated.toString());
            document.put("timeupdated", timeCreated.toString());
            document.put("state", SensorState.RUNNING.toString());

            table.insert(document);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ObjectId createSensor(String userId, String sensorId, String name, String latitude, String longitude) {

        // check if the document exists in the db by calling getVirtualSensor(String userId, String sensorId) before calling this method
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        // should I check if the document exists in the collection?
        BasicDBObject document = new BasicDBObject();
        ObjectId id= null;

        try {
            Date timeCreated = new Date(); // timecreated and timeupdated are the same at this point

            // a unique id is generated
            document.put("userid", userId);
            document.put("sensorid", sensorId);
            document.put("name", name);
            document.put("latitude", latitude);
            document.put("longitude", longitude);
            document.put("timecreated", timeCreated.toString());
            document.put("timeupdated", timeCreated.toString());
            document.put("state", SensorState.RUNNING.toString());

            table.insert(document);
            id = (ObjectId)document.get( "_id" );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public void updateVirtualSensor(VirtualSensor virtualSensorData) {
        // check if the document exists in the db by calling getVirtualSensor(String userId, String sensorId) before calling this method
        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        BasicDBObject updateFields = new BasicDBObject();
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject setQuery = new BasicDBObject();

        try {
            searchQuery.append("userid", virtualSensorData.getUserid())
                    .append("sensorid", virtualSensorData.getSensorid());

            Date timeUpdated = new Date();

            updateFields.append("userid", virtualSensorData.getUserid());
            updateFields.append("sensorid", virtualSensorData.getSensorid());
            updateFields.append("name", virtualSensorData.getName());
            updateFields.append("latitude", virtualSensorData.getLatitude());
            updateFields.append("longitude", virtualSensorData.getLongitude());
            updateFields.append("timecreated", virtualSensorData.getTimecreated());
            updateFields.append("state", virtualSensorData.getState().toString());

            // time updated is set to the current time
            updateFields.append("timeupdated", timeUpdated.toString());

            setQuery.append("$set", updateFields);
            table.update(searchQuery, setQuery);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public VirtualSensor getVirtualSensor(String userId, String sensorId) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        BasicDBObject dbQuery = new BasicDBObject();
        List<BasicDBObject> query_parameters = new ArrayList<>();
        DBCursor dbCursor = null;
        Gson gson= new Gson();
        VirtualSensor virtualSensor= null;

        try {

            query_parameters.add(new BasicDBObject("userid", userId));
            query_parameters.add(new BasicDBObject("sensorid", sensorId));
            dbQuery.put("$and", query_parameters);

            dbCursor= table.find(dbQuery);
            while (dbCursor.hasNext()) {
                DBObject obj = dbCursor.next();
                virtualSensor =gson.fromJson(obj.toString(), VirtualSensor.class);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return virtualSensor;
    }

    @Override
    public List<VirtualSensor> getVirtualSensorListByUserId(String userId) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());
        Gson gson = new Gson();
        List<VirtualSensor> virtualSensorList = new ArrayList<>();

        BasicDBObject dbQuery = new BasicDBObject();
        DBCursor dbCursor = null;

        try {
            dbQuery.put("userid", userId);
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
        }
    }

    @Override
    public void deleteVirtualSensor(String userId, String sensorId) {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

        BasicDBObject dbQuery = new BasicDBObject();
        List<BasicDBObject> query_parameters = new ArrayList<>();

        try {

            query_parameters.add(new BasicDBObject("userid", userId));
            query_parameters.add(new BasicDBObject("sensorid", sensorId));
            dbQuery.put("$and", query_parameters);

            table.remove(dbQuery);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        }
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
        }
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
        }
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
        }
    }

    @Override
    public List<VirtualSensor> getAllSensors() {

        DBCollection table = mongoOperations.getCollection(MongoCollection.VirtualSensor.toString());

             Gson gson = new Gson();
        List<VirtualSensor> virtualSensorList = new ArrayList<>();

        DBCursor dbCursor = null;

        try {
            // get all the sensors
            dbCursor = table.find();

            while (dbCursor.hasNext()) {
                DBObject obj = dbCursor.next();
                VirtualSensor vs = gson.fromJson(obj.toString(), VirtualSensor.class);
                virtualSensorList.add(vs);
            }
            return virtualSensorList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
