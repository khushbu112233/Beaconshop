package com.amplearch.beaconshop.Model;

/**
 * Created by ample-arch on 4/17/2017.
 */

public class Images
{
    int id ;
    String name;
    byte[] image ;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
