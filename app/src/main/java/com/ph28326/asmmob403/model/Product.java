package com.ph28326.asmmob403.model;

public class Product {

    public String _id, name, brand, category, price0, price1, image, description;

    public Product() {
    }

    public Product(String _id, String name, String brand, String category, String price0, String price1, String image, String description) {
        this._id = _id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price0 = price0;
        this.price1 = price1;
        this.image = image;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", price0='" + price0 + '\'' +
                ", price1='" + price1 + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}'+"\n";
    }
}
