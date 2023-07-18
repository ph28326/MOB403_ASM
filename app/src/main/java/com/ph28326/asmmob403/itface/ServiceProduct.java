package com.ph28326.asmmob403.itface;

import com.ph28326.asmmob403.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceProduct {




    @GET("list-product")
    Call<List<Product>> getAllProduct();

    @GET("detail-product")
    Call<Product> getProduct(@Query("id") String id);

    @POST("add-product")
    Call<Product> addProduct(@Body Product product);

    @DELETE("delete-product")
    Call<Boolean> deleteProduct(@Query("id") String id);

}
