package com.example.swapnil.coffeeshop.models;

public class CategoryModel {
    private String cat_id;
    private String cat_name;
    private String cat_desc;
    private String cat_img_path;

    public CategoryModel(String cat_id, String cat_name, String cat_desc, String cat_img_path){
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_desc = cat_desc;
        this.cat_img_path = cat_img_path;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getCat_img_path() {
        return cat_img_path;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public String getCat_id() {
        return cat_id;
    }
}
