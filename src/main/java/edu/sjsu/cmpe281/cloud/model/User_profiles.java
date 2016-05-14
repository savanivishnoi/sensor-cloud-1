package edu.sjsu.cmpe281.cloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vaishampayan Reddy on 5/13/2016.
 */

@Document(collection = "User_profiles")
public class User_profiles {
    @Id
    private String id;
    private String userid;
    private String first_name;
    private String last_name;
    private String ssn;
    private String credit_card;
    private String cvv;
    private String credit_card_month;
    private String credit_card_year;
    private String billing_address;
    private String city;
    private String state;
    private String zipcode;

    public User_profiles(String id, String userid, String first_name, String last_name, String ssn, String credit_card, String cvv, String credit_card_month, String credit_card_year, String billing_address, String city, String state, String zipcode) {
        this.id = id;
        this.userid = userid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ssn = ssn;
        this.credit_card = credit_card;
        this.cvv = cvv;
        this.credit_card_month = credit_card_month;
        this.credit_card_year = credit_card_year;
        this.billing_address = billing_address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getCredit_card() {
        return credit_card;
    }

    public void setCredit_card(String credit_card) {
        this.credit_card = credit_card;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCredit_card_month() {
        return credit_card_month;
    }

    public void setCredit_card_month(String credit_card_month) {
        this.credit_card_month = credit_card_month;
    }

    public String getCredit_card_year() {
        return credit_card_year;
    }

    public void setCredit_card_year(String credit_card_year) {
        this.credit_card_year = credit_card_year;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "User_profiles{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", ssn='" + ssn + '\'' +
                ", credit_card='" + credit_card + '\'' +
                ", cvv='" + cvv + '\'' +
                ", credit_card_month='" + credit_card_month + '\'' +
                ", credit_card_year='" + credit_card_year + '\'' +
                ", billing_address='" + billing_address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
