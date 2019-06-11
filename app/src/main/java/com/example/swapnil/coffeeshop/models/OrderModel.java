package com.example.swapnil.coffeeshop.models;

public class OrderModel {
    private String prod_image;
    private String prod_name;

    public OrderModel(String prod_image, String prod_name)
    {
        this.prod_image = prod_image;
        this.prod_name = prod_name;
    }

    public String getProd_image() {
        return prod_image;
    }

    public String getProd_name() {
        return prod_name;
    }
}
