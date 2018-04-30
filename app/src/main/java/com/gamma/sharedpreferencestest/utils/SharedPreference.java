package com.gamma.sharedpreferencestest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gamma.sharedpreferencestest.beans.Product;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by emers on 29/4/2018.
 */

public class SharedPreference {

    public static final String SHARED_PREFS_NAME = "PRODUCT_APP_TEST";
    public static final String FAVS = "Product_Favorite";

    public SharedPreference(){
        super();
    }

    //Los siguientes 4 metodos se usan para manejar los favoritos

    public void saveFavorites (Context c, List<Product> favs){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = c.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor  = settings.edit();

        Gson gson = new Gson();
        String jsonFavs = gson.toJson(favs);

        editor.putString(FAVS ,jsonFavs);
        editor.commit();
    }

    public ArrayList<Product> getFavorites(Context c){
        SharedPreferences settings;
        List<Product> favs;

        settings = c.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        if(settings.contains(FAVS)){
            String jsonFavs = settings.getString(FAVS, null);
            Gson g = new Gson();
            Product[] favItems = g.fromJson(jsonFavs, Product[].class);

            favs = Arrays.asList(favItems);
            favs = new ArrayList<Product>(favs);
        } else return null;

        return (ArrayList<Product>) favs;
    }

    public void addFavorite(Context c, Product p){
        List<Product> favs = getFavorites(c);
        if(favs == null) favs = new ArrayList<Product>();
        favs.add(p);
        saveFavorites(c, favs);
    }

    public void removeFavorite(Context c, Product p){
        ArrayList<Product> favs = getFavorites(c);
        if(favs != null) {
            favs.remove(p);
            saveFavorites(c, favs);
        }
    }
}
