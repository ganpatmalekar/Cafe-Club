package com.example.swapnil.coffeeshop.models;

public class FavModel {
    private String fav_item_name;
    private String fav_item__url;

    public FavModel(String fav_item_name, String fav_item__url)
    {
        this.fav_item_name = fav_item_name;
        this.fav_item__url = fav_item__url;
    }

    public String getFav_item_name() {
        return fav_item_name;
    }

    public String getFav_item__url() {
        return fav_item__url;
    }
}
