package edu.sjsu.cmpe281.cloud.service;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import edu.sjsu.cmpe281.cloud.exceptions.InvalidDetailsException;
import edu.sjsu.cmpe281.cloud.model.User_profiles;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaishampayan Reddy on 5/13/2016.
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection"})
@Service
public class UserService implements IUserService {
    private static final String userCollectionName = "User";
    private static final String userProfileCollectionName = "User_profiles";
    private Gson gson = new Gson();
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public ObjectId createUser(String email, String password, String first_name, String last_name, String ssn, String credit_card, String cvv, String credit_card_month, String credit_card_year, String billing_address, String city, String state, String zipcode) throws JSONException{
        /**
         * create user record
         */
        BasicDBObject document = new BasicDBObject();
        DBCollection userCollection = mongoOperations.getCollection(userCollectionName);
        document.put("email", email);
        document.put("password", password);
        userCollection.insert(document);

        ObjectId id = (ObjectId)document.get( "_id" );

        /**
         * create user_profile record
         */

        DBCollection userProfileCollection = mongoOperations.getCollection(userProfileCollectionName);
        document = new BasicDBObject();
        document.put("userid", id.toString());
        document.put("first_name", first_name);
        document.put("last_name", last_name);
        document.put("ssn", ssn);
        document.put("credit_card", credit_card);
        document.put("cvv", cvv);
        document.put("credit_card_month", credit_card_month);
        document.put("credit_card_year", credit_card_year);
        document.put("billing_address", billing_address);
        document.put("city", city);
        document.put("state", state);
        document.put("zipcode", zipcode);
        userProfileCollection.insert(document);

        return id;
    }

    @Override
    public void updateUser(String userid, String email, String password, String first_name, String last_name, String ssn, String credit_card, String cvv, String credit_card_month, String credit_card_year, String billing_address, String city, String state, String zipcode) {

    }

    @Override
    public void deleteUser(String userid) {

    }

    @Override
    public User_profiles getUser(String userid) {
        return null;
    }

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws InvalidDetailsException
     */
    @Override
    public User_profiles doLogin(String email, String password) throws InvalidDetailsException {
        try {
            List<BasicDBObject> userSearch = new ArrayList<BasicDBObject>();
            userSearch.add(new BasicDBObject("email", email));
            BasicDBObject queryObject = new BasicDBObject();
            queryObject.put("$and", userSearch);
            DBCursor dbCursor;
            DBCollection userCollection = mongoOperations.getCollection(userCollectionName);
            dbCursor = userCollection.find(queryObject);
            if(dbCursor == null) {
                throw new InvalidDetailsException("Error: Cannot find user");
            }
            else {
                while(dbCursor.hasNext()) {
                    JSONObject userObject = new JSONObject(JSON.serialize(dbCursor.next()));
                    if(userObject.get("password").equals(password)) {
                        userSearch = new ArrayList<BasicDBObject>();
                        userSearch.add(new BasicDBObject("userid", ((JSONObject) userObject.get("_id")).get("$oid")));
                        queryObject = new BasicDBObject();
                        queryObject.put("$and", userSearch);
                        DBCollection userProfileCollection = mongoOperations.getCollection(userProfileCollectionName);
                        DBCursor dbCursor2 = userProfileCollection.find(queryObject);
                        while(dbCursor2.hasNext()) {
                            return gson.fromJson(dbCursor2.next().toString(), User_profiles.class);
                        }
                    }
                    else {
                        throw new InvalidDetailsException("Error: Cannot find user");
                    }
                }
                throw new InvalidDetailsException("Error: Cannot find user");
            }
        }
        catch (Exception e) {
            throw new InvalidDetailsException("Error: Cannot find user: " + e.getMessage());
        }
    }
}
