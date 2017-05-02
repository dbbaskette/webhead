package com.dbbaskette.funcollector.webhead;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;


/**
 * Created by dbaskette on 3/22/17.
 */

@Entity
public class UploadedImage {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String description = "...Processing";


    private double value = 0.00;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    private ArrayList<String> matchingURLS = new ArrayList<>();
    private ArrayList<String> termlist = new ArrayList<>();
    private byte[] image;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected UploadedImage(){}

    public UploadedImage(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getTermlist() {
        return termlist;
    }

    public void setTermlist(ArrayList<String> termlist) {
        this.termlist = termlist;
    }

    public ArrayList<String> getMatchingURLS() {
        return matchingURLS;
    }

    public void setMatchingURLS(ArrayList<String> matchingURLS) {
        this.matchingURLS = matchingURLS;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
