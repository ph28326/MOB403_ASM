package com.ph28326.asmmob403;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ph28326.asmmob403.adapter.ProductAdapter;
import com.ph28326.asmmob403.itface.ITFProductOnclick;
import com.ph28326.asmmob403.itface.ServiceProduct;
import com.ph28326.asmmob403.model.Product;
import com.ph28326.asmmob403.tool.Tools;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String URL = "http://" + Tools.IP + ":9999/json/";
    RecyclerView rcv;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.myrcv);
        setProgressDialog();
        getData();
    }

    public void setProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage("please wait...");
        pd.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Tools.deleteCache(DemoActivity.this);
    }

    public void add(View view) {
        startActivity(new Intent(MainActivity.this, DetailProductActivity.class));
    }

    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceProduct serviceProduct = retrofit.create(ServiceProduct.class);
        Call<List<Product>> call = serviceProduct.getAllProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                SetData(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Không thể tải dữ liệu");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void SetData(List<Product> list) {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());
        ProductAdapter adapter = new ProductAdapter(list, MainActivity.this, new ITFProductOnclick() {
            @Override
            public void OnClick(Product product) {
                Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
                intent.putExtra("id", product._id);
                startActivity(intent);
            }
        });
        rcv.setAdapter(adapter);
    }
}