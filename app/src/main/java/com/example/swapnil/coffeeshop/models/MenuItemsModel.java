package com.example.swapnil.coffeeshop.models;

public class MenuItemsModel {

    private String prod_id;
    private String image_url;
    private String prod_name;
    private String prod_desc;
    private String prod_price;

    public MenuItemsModel(String prod_id, String image_url, String prod_name, String prod_desc, String prod_price)
    {
        this.prod_id = prod_id;
        this.image_url = image_url;
        this.prod_name = prod_name;
        this.prod_desc = prod_desc;
        this.prod_price = prod_price;
    }

    public String getProd_id() {
        return prod_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public String getProd_price() {
        return prod_price;
    }
}
