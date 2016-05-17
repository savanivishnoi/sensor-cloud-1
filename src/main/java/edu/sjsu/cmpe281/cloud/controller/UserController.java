package edu.sjsu.cmpe281.cloud.controller;

import com.google.gson.Gson;
import edu.sjsu.cmpe281.cloud.exceptions.InvalidDetailsException;
import edu.sjsu.cmpe281.cloud.model.User;
import edu.sjsu.cmpe281.cloud.model.User_profiles;
import edu.sjsu.cmpe281.cloud.service.IUserService;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Vaishampayan Reddy on 5/13/2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private Gson gson = new Gson();
    @Autowired
    IUserService iUserService;
    private HttpSession session = null;
    /**
     *
     * @param email
     * @param password
     * @param first_name
     * @param last_name
     * @param ssn
     * @param credit_card
     * @param cvv
     * @param credit_card_month
     * @param credit_card_year
     * @param billing_address
     * @param city
     * @param state
     * @param zipcode
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity register(@RequestParam(value = "email", required = true) String email,
                                   @RequestParam(value = "password", required = true) String password,
                                   @RequestParam(value = "first_name", required = true) String first_name,
                                   @RequestParam(value = "last_name", required = true) String last_name,
                                   @RequestParam(value = "ssn", required = true) String ssn,
                                   @RequestParam(value = "credit_card", required = true) String credit_card,
                                   @RequestParam(value = "cvv", required = true) String cvv,
                                   @RequestParam(value = "credit_card_month", required = true) String credit_card_month,
                                   @RequestParam(value = "credit_card_year", required = true) String credit_card_year,
                                   @RequestParam(value = "billing_address", required = true) String billing_address,
                                   @RequestParam(value = "city", required = true) String city,
                                   @RequestParam(value = "state", required = true) String state,
                                   @RequestParam(value = "zipcode", required = true) String zipcode) {

        try {
            ObjectId id = iUserService.createUser(email, password, first_name, last_name, ssn, credit_card, cvv, credit_card_month, credit_card_year, billing_address, city, state, zipcode);
            if(id != null) {
                JSONObject responseObject = new JSONObject();
                responseObject.put("status","201");
                responseObject.put("message", "User created Successfully");
                responseObject.put("id", id.toString());
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("failed " + id, HttpStatus.OK);
            }
        }
        catch (JSONException e) {
            return new ResponseEntity<>("failed" + e.getMessage(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "401";
        }
        else {
            return session.getAttribute("userid").toString();
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getMyProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "401";
        }
        else {
            return session.getAttribute("profile").toString();
        }
    }

    /**
     *
     * @param email
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity doLogin(
                    @RequestParam(value = "email", required = true) String email,
                    @RequestParam(value = "password", required = true) String password,
                    HttpServletRequest request) throws JSONException{
        try {
            User_profiles user_profiles = iUserService.doLogin(email, password);
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("userid", user_profiles.getUserid());
            httpSession.setAttribute("profile", gson.toJson(user_profiles));
            JSONObject responseObject = new JSONObject();
            responseObject.put("status", 200);
            responseObject.put("message", "Login Successfull!!!");
            responseObject.put("profile", gson.toJson(user_profiles));
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        }
        catch(InvalidDetailsException ide) {
            System.out.println("ide: " + ide.getErrmsg());
            JSONObject responseObject = new JSONObject();
            responseObject.put("status", 401);
            responseObject.put("message", "Error: Login failure\n" + ide.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        }
        catch(Exception e) {
            System.out.println("e: " + e.getMessage());
            JSONObject responseObject = new JSONObject();
            responseObject.put("status", 401);
            responseObject.put("message", "Error: Login failure\n" + e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        }
    }

    /**
     *
     * function for logout
     *
     * @param request
     * @return
     */
    public ResponseEntity doLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("status", 200);
            responseObject.put("message", "Logged out Successfully!!!");
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        }
        catch(JSONException e) {
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        }
    }
}
