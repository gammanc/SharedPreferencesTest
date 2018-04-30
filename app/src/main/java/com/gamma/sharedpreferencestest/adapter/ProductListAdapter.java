package com.gamma.sharedpreferencestest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamma.sharedpreferencestest.R;
import com.gamma.sharedpreferencestest.beans.Product;
import com.gamma.sharedpreferencestest.utils.SharedPreference;

import java.util.List;

/**
 * Created by emers on 29/4/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductsViewHolder>{
    private Context mContext;
    List<Product> mProducts;
    SharedPreference sharedPreference;

    public interface ProductListClickListener{
        public void onProductClick(View v, int position);
        public void onProductLongClick(View v, int position);

    }
    private ProductListClickListener mListener;

    public ProductListAdapter(Context context, List<Product> products, ProductListClickListener mListener){
        mContext = context;
        mProducts = products;
        this.mListener = mListener;
        sharedPreference = new SharedPreference();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rel;
        TextView productNameTxt;
        TextView productDescTxt;
        TextView productPriceTxt;
        ImageView favoriteImg;

        public ProductsViewHolder(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.pdt_layout_item);
            productNameTxt = itemView.findViewById(R.id.txt_pdt_name);
            productDescTxt = itemView.findViewById(R.id.txt_pdt_desc);
            productPriceTxt = itemView.findViewById(R.id.txt_pdt_price);
            favoriteImg = itemView.findViewById(R.id.imgbtn_favorite);
        }
    }

    @Override
    public ProductsViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return (new ProductsViewHolder(v));
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {
        final Product product = (Product) mProducts.get(position);
        holder.productNameTxt.setText(product.getName());
        holder.productDescTxt.setText(product.getDescription());
        holder.productPriceTxt.setText(product.getPrice() + "");

        if (checkFavoriteItem(product)) {
            holder.favoriteImg.setImageResource(R.drawable.ic_fav);
            holder.favoriteImg.setTag("red");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.ic_fav_border);
            holder.favoriteImg.setTag("grey");
        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mListener.onProductClick(v, position);
            }
        });

        holder.rel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            mListener.onProductLongClick(v, position);
            return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    public boolean checkFavoriteItem(Product checkProduct) {
        boolean check = false;
        List<Product> favorites = sharedPreference.getFavorites(mContext);
        if (favorites != null) {
            for (Product product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }



    /*@Override
    public void add(Product product) {
        super.add(product);
        mProducts.add(product);
        notifyDataSetChanged();
    }
*/

    public void remove(Product product) {
        //super.remove(product);
        mProducts.remove(product);
        notifyDataSetChanged();
    }

}
