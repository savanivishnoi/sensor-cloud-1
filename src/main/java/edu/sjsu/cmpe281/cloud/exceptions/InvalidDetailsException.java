package edu.sjsu.cmpe281.cloud.exceptions;

/**
 * Created by Vaishampayan Reddy on 5/13/2016.
 */
public class InvalidDetailsException extends Exception {

    private String errmsg;

    public InvalidDetailsException(String errmsg) {
        super();
        this.errmsg = errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public String getMessage() {
        return errmsg;
    }
}
