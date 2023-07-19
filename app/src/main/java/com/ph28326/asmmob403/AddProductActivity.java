package com.ph28326.asmmob403;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ph28326.asmmob403.model.Product;

public class AddProductActivity extends AppCompatActivity {
    Spinner spBrandAdd, spCategoryAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        spBrandAdd = findViewById(R.id.spBrandAdd);
        spCategoryAdd = findViewById(R.id.spCategoryAdd);

        setSpinner();
    }

    public void setSpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(AddProductActivity.this,
                android.R.layout.simple_spinner_item, DetailProductActivity.categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryAdd.setAdapter(categoryAdapter);

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(AddProductActivity.this,
                android.R.layout.simple_spinner_item, DetailProductActivity.brands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrandAdd.setAdapter(brandAdapter);

    }
}