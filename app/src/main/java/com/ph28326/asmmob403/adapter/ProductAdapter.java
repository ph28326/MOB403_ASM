package com.ph28326.asmmob403.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ph28326.asmmob403.R;
import com.ph28326.asmmob403.itface.ITFProductOnclick;
import com.ph28326.asmmob403.itface.ServiceProduct;
import com.ph28326.asmmob403.model.Product;
import com.ph28326.asmmob403.tool.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProVH> {
    List<Product> list;
    ITFProductOnclick itf;
    Context context;
    String URL = "http://"+ Tools.IP+":9999/json/";
    String url = "http://"+Tools.IP+":9999/images/products/";

    public ProductAdapter(List<Product> list, Context context, ITFProductOnclick itf) {
        this.list = list;
        this.itf = itf;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductAdapter.ProVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductAdapter.ProVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProVH holder, int position) {
        Product product = list.get(position);
        if (product == null){
            return;
        }
        holder.tv_name.setText(product.name);
        try {
            holder.tv_price0.setText("₫"+Tools.IntegerToVND(Integer.parseInt(product.price0)));
        }catch (Exception e){

        }
        try {
            holder.tv_price1.setText("₫"+Tools.IntegerToVND(Integer.parseInt(product.price1)));
        }catch (Exception e){

        }

        Picasso.get().load(url+product.image)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.iv_pro);
        holder.tv_name.setOnClickListener(view -> {
            itf.OnClick(product);
        });
        holder.iv_pro.setOnClickListener(view -> {
            itf.OnClick(product);
        });
        holder.iv_brand.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa sản phẩm: "+product.name);
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ServiceProduct serviceProduct = retrofit.create(ServiceProduct.class);
                    Call<Boolean> call = serviceProduct.deleteProduct(product._id);
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.body() == true){
                                list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }else {
                                Toast.makeText(context, "không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(context, "fail: không tìm thấy!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }


    public class ProVH extends RecyclerView.ViewHolder{

        TextView tv_name, tv_price1, tv_price0, tv_del;
        ImageView iv_pro, iv_brand;
        public ProVH(@NonNull View i) {
            super(i);
            tv_name = i.findViewById(R.id.tv_name);
            tv_price1 = i.findViewById(R.id.tv_price1);
            tv_price0 = i.findViewById(R.id.tv_price0);
            iv_brand = i.findViewById(R.id.iv_delete);
            iv_pro = i.findViewById(R.id.iv_image);
            tv_del = i.findViewById(R.id.tv_del_pro);
        }
    }
}
