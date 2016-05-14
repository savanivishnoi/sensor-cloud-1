package edu.sjsu.cmpe281.cloud.service;

import edu.sjsu.cmpe281.cloud.exceptions.InvalidDetailsException;
import edu.sjsu.cmpe281.cloud.model.User_profiles;
import org.bson.types.ObjectId;
import org.json.JSONException;

/**
 * Created by Vaishampayan Reddy on 5/13/2016.
 */
public interface IUserService {
    public ObjectId createUser(String email, String password, String first_name, String last_name, String ssn, String credit_card, String cvv, String credit_card_month, String credit_card_year, String billing_address, String city, String state, String zipcode)  throws JSONException;

    public void updateUser(String userid, String email, String password, String first_name, String last_name, String ssn, String credit_card, String cvv, String credit_card_month, String credit_card_year, String billing_address, String city, String state, String zipcode);

    public void deleteUser(String userid);

    public User_profiles getUser(String userid);

    public User_profiles doLogin(String email, String password) throws InvalidDetailsException;
}
