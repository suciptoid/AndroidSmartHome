package com.pringstudio.agnosthings.model;

/**
 * Created by sucipto on 4/25/16.
 */
public class Saklar {
    String id;
    String name;
    int value;

    public Saklar(String name){
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
