package com.amplearch.beaconshop.Model;

import java.util.Date;

public class StoreLocation {

	int id;
	String store_name;
	String lat;
	String lng;
	String offer_title;
	String offer_desc;
	String start_date;
	String end_date;

	// constructors
	public StoreLocation() {

	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
        /*this.lat = lat;
        this.lng = lng;
        this.offer_title = offer_title;
        this.offer_desc = offer_desc;
        this.start_date = start_date;
        this.end_date = end_date;*/
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

	public StoreLocation(String store_name, String lat, String lng, String offer_title, String offer_desc, String start_date, String end_date ) {
		this.store_name = store_name;
        this.lat = lat;
        this.lng = lng;
        this.offer_title = offer_title;
        this.offer_desc = offer_desc;
        this.start_date = start_date;
        this.end_date = end_date;
	}

	public StoreLocation(int id, String store_name) {
		this.id = id;
		this.store_name = store_name;
	}
}
