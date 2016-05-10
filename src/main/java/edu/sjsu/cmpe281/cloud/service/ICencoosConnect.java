package edu.sjsu.cmpe281.cloud.service;

/**
 * Created by Naks on 09-May-16.
 * Interface to fetch data CENCOOS Data from ERRDAP website.
 */
public interface ICencoosConnect {

    public boolean fetchData(String timestamp);
}
