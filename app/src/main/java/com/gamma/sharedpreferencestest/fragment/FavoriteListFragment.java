package com.gamma.sharedpreferencestest.fragment;

/**
 * Created by emers on 29/4/2018.
 */

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gamma.sharedpreferencestest.R;
import com.gamma.sharedpreferencestest.adapter.ProductListAdapter;
import com.gamma.sharedpreferencestest.beans.Product;
import com.gamma.sharedpreferencestest.utils.SharedPreference;

public class FavoriteListFragment extends Fragment implements ProductListAdapter.ProductListClickListener{

    public static final String ARG_ITEM_ID = "favorite_list";

    RecyclerView favoriteList;
    SharedPreference sharedPreference;
    List<Product> favorites;
    LinearLayoutManager lManager;

    Activity activity;
    ProductListAdapter productListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container,
                false);
        // Get favorite items from SharedPreferences.
        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {
            showAlert(getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        } else {

            if (favorites.size() == 0) {
                showAlert(
                        getResources().getString(R.string.no_favorites_items),
                        getResources().getString(R.string.no_favorites_msg));
            }

            favoriteList = (RecyclerView) view.findViewById(R.id.list_product);
            if (favorites != null) {
                favoriteList.setHasFixedSize(true);
                lManager = new LinearLayoutManager(container.getContext());
                favoriteList.setLayoutManager(lManager);

                productListAdapter = new ProductListAdapter(activity, favorites, this);
                favoriteList.setAdapter(productListAdapter);
            }
        }
        return view;
    }

    public void showAlert(String title, String message) {
        if (activity != null && !activity.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // activity.finish();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.favorites);
        //getActivity().getActionBar().setTitle(R.string.favorites);
        super.onResume();
    }

    @Override
    public void onProductClick(View v, int position) {

    }

    @Override
    public void onProductLongClick(View v, int position) {
        ImageView button = (ImageView) v
                .findViewById(R.id.imgbtn_favorite);

        String tag = button.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(activity,
                    favorites.get(position));
            Toast.makeText(
                    activity,
                    activity.getResources().getString(
                            R.string.add_favr),
                    Toast.LENGTH_SHORT).show();

            button.setTag("red");
            button.setImageResource(R.drawable.ic_fav);
        } else {
            sharedPreference.removeFavorite(activity,
                    favorites.get(position));
            button.setTag("grey");
            button.setImageResource(R.drawable.ic_fav_border);
            productListAdapter.remove(favorites
                    .get(position));
            Toast.makeText(
                    activity,
                    activity.getResources().getString(
                            R.string.remove_favr),
                    Toast.LENGTH_SHORT).show();
        }
    }
}