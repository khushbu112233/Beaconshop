package com.amplearch.beaconshop.Model;

public class StoreLocation {

	int id;
    String product_id;
	String store_name;
	byte[] store_image;
	String lat;
	String lng;
    String quantity;
	String offer_title;
	String offer_desc;
	String start_date;
	String end_date;
	String category_id;

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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public byte[] getStore_image() {
		return store_image;
	}

	public void setStore_image(byte[] store_image) {
		this.store_image = store_image;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public StoreLocation(String product_id, String store_name, byte[] store_image, String lat, String lng, String quantity, String offer_title, String offer_desc, String start_date, String end_date, String category_id ) {
		this.product_id = product_id;
        this.store_name = store_name;
		this.store_image = store_image;
        this.lat = lat;
        this.lng = lng;
        this.quantity = quantity;
        this.offer_title = offer_title;
        this.offer_desc = offer_desc;
        this.start_date = start_date;
        this.end_date = end_date;
        this.category_id = category_id;
	}

	public StoreLocation(int id, String store_name) {
		this.id = id;
		this.store_name = store_name;
	}
}
