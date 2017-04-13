package com.amplearch.beaconshop.Model;

public class Favourites {

	int id;
	String product_id;
	String user_id;

	// constructors
	public Favourites() {
	}

	public Favourites(String product_id, String user_id ) {
		this.product_id = product_id;
		this.user_id = user_id;
	}

	public Favourites(int id, String product_id, String user_id) {
		this.id = id;
		this.product_id = product_id;
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
