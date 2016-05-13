package edu.sjsu.cmpe281.cloud.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Naks on 13-May-16.
 * Test POJO to run Test Controller
 */

@Component
public class TestDto implements Serializable{

    private int id;
    private String name;


    public TestDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
