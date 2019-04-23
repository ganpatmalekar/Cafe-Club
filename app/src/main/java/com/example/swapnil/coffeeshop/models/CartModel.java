package com.example.swapnil.coffeeshop.models;

public class CartModel {

    private String cart_item_id;
    private String cart_item_name;
    private String cart_item__price;
    private String cart_item__qty;

    public CartModel(String cart_item_id, String cart_item_name, String cart_item__price, String cart_item__qty)
    {
        this.cart_item_id = cart_item_id;
        this.cart_item_name = cart_item_name;
        this.cart_item__price= cart_item__price;
        this.cart_item__qty = cart_item__qty;
    }

    public String getCart_item_id() {
        return cart_item_id;
    }

    public String getCart_item_name() {
        return cart_item_name;
    }

    public String getCart_item__price() {
        return cart_item__price;
    }

    public String getCart_item__qty() {
        return cart_item__qty;
    }
}
