package com.amplearch.beaconshop.Model;

/**
 * Created by admin on 04/15/2017.
 */

public class UserRedeemSql {

    private int id;
    private String user_id;
    private String offer_id;
    private String offer_code;
    private byte[] store_image;
    private String offer_title;
    private String offer_desc;
    private String quantity;
    private String redeem;

    public UserRedeemSql() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public byte[] getStore_image() {
        return store_image;
    }

    public void setStore_image(byte[] store_image) {
        this.store_image = store_image;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRedeem() {
        return redeem;
    }

    public void setRedeem(String redeem) {
        this.redeem = redeem;
    }
}
