package com.amplearch.beaconshop.Model;

/**
 * Created by admin on 04/10/2017.
 */

public class Voucher {

    int id;
    String store_name;
    String lat;
    String lng;
    String offer_title;
    String offer_desc;
    String start_date;
    String end_date;
    String message;
    String uuid;
    String major;
    String minor;
    String product_id;

    public Voucher() {
    }

    public Voucher(String store_name, String lat, String lng, String offer_title, String offer_desc, String start_date, String end_date, String message, String uuid, String major, String minor, String product_id) {
        this.store_name = store_name;
        this.lat = lat;
        this.lng = lng;
        this.offer_title = offer_title;
        this.offer_desc = offer_desc;
        this.start_date = start_date;
        this.end_date = end_date;
        this.message = message;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.product_id = product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_desc() {
        return offer_desc;
    }

    public void setOffer_desc(String offer_desc) {
        this.offer_desc = offer_desc;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
}
