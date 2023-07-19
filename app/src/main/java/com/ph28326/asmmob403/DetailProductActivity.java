package com.ph28326.asmmob403;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ph28326.asmmob403.itface.ServiceProduct;
import com.ph28326.asmmob403.model.Product;
import com.ph28326.asmmob403.tool.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailProductActivity extends AppCompatActivity {

    String URL = "http://" + Tools.IP + ":9999/json/";
    String _id;
    String idProduct;
    TextInputEditText ed_name, ed_price0, ed_price1, ed_des;
    TextInputLayout til_1;
    Spinner sp_brand, sp_category;
    ImageView iv_product;
    Button btn_reload, btn_delete, btn_update;
    ProgressDialog pd;
    public static final String[] categories = {"Smartphone", "Laptop", "Smartwatch", "Bluetooth Speaker", "Wireless Headphones", "Power Bank"};
    public static final String[] brands = {"Samsung", "Apple", "Oppo", "Sony", "Nokia", "Xiaomi", "Realme", "JBL", "BeU"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        InitView();
        setProgressDialog();
        Intent intent = getIntent();
        idProduct = intent.getStringExtra("id");
        try {
            getData();
        } catch (Exception e) {

        }
    }

    public void setProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage("please wait...");
        pd.show();
    }

    public void InitView() {
        ed_name = findViewById(R.id.edtNameProDetail);
        ed_price0 = findViewById(R.id.ed_acti_add_p0);
        ed_price1 = findViewById(R.id.ed_acti_add_p1);
        ed_des = findViewById(R.id.ed_acti_add_des);
        sp_brand = findViewById(R.id.spBrandDetail);
        sp_category = findViewById(R.id.spCategoryDetail);
        iv_product = findViewById(R.id.ivProDetail);
        til_1 = findViewById(R.id.til_1);
        btn_update = findViewById(R.id.btn_update_pro);
        btn_delete = findViewById(R.id.btn_delete_pro);
        btn_reload = findViewById(R.id.btn_reload_pro);

        btn_reload.setOnClickListener(view -> {
            getData();
            Toast.makeText(DetailProductActivity.this, "reload", Toast.LENGTH_SHORT).show();
        });

    }

    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceProduct serviceProduct = retrofit.create(ServiceProduct.class);
        Call<Product> call = serviceProduct.getProduct(idProduct);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                setData(response.body());
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(Product product) {
        if (product == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sản phẩm không tồn tại");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(DetailProductActivity.this, MainActivity.class));
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        ed_name.setText(product.name);
        try {
            ed_price0.setText(Tools.IntegerToVND(Integer.parseInt(product.price0)));
        } catch (Exception e) {
            ed_price0.setText((product.price0));
        }
        try {
            ed_price1.setText(Tools.IntegerToVND(Integer.parseInt(product.price1)));
        } catch (Exception e) {
            ed_price1.setText((product.price1));
        }

        setSpinner(product);

        ed_des.setText(product.description);
        Tools.setImage(product.image, iv_product, DetailProductActivity.this);
        ed_price0.setOnClickListener(view -> {
            ed_price0.setText((product.price0));
        });
        ed_price1.setOnClickListener(view -> {
            ed_price1.setText((product.price1));
        });
    }

    public void setSpinner(Product product) {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(DetailProductActivity.this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(categoryAdapter);
        for (int i = 0; i < categories.length; i++) {
            if (product.category.equalsIgnoreCase(categories[i])) {
                sp_category.setSelection(i);
                break;
            }
        }

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(DetailProductActivity.this,
                android.R.layout.simple_spinner_item, brands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_brand.setAdapter(brandAdapter);
        for (int i = 0; i < brands.length; i++) {
            if (product.brand.equalsIgnoreCase(brands[i])) {
                sp_brand.setSelection(i);
                break;
            }
        }
    }

}