package com.gamma.sharedpreferencestest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gamma.sharedpreferencestest.adapter.ProductListAdapter;
import com.gamma.sharedpreferencestest.fragment.FavoriteListFragment;
import com.gamma.sharedpreferencestest.fragment.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment contentF;
    ProductListFragment productListFragment;
    FavoriteListFragment favoriteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Super ejecutado");
        setContentView(R.layout.activity_main);
        System.out.println("activity_main colocado");
        FragmentManager fm = getSupportFragmentManager();
        System.out.println("FragmentManager iniciado");
        if(savedInstanceState != null){
            System.out.println("Se encontró Bundle");
            if(savedInstanceState.containsKey("myContent")){
                String content = savedInstanceState.getString("myContent");
                if(content.equals(FavoriteListFragment.ARG_ITEM_ID)){
                    if(fm.findFragmentByTag(FavoriteListFragment.ARG_ITEM_ID) != null){
                        setFragmentTitle(R.string.favorites);
                        contentF = fm.findFragmentByTag(FavoriteListFragment.ARG_ITEM_ID);
                    }
                }
            }

            if(fm.findFragmentByTag(ProductListFragment.ARG_ITEM_ID) != null){
                productListFragment = (ProductListFragment) fm.findFragmentByTag(ProductListFragment.ARG_ITEM_ID);
                contentF = productListFragment;
            }
        } else {
            System.out.println("No se encontró Bundle");
            productListFragment = new ProductListFragment();
            System.out.println("productListFragment inicializado");
            setFragmentTitle(R.string.app_name);
            System.out.println("Titulo de fragment colocado");
            switchContent(productListFragment, ProductListFragment.ARG_ITEM_ID);
            System.out.println("Contenido colocado");
        }
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if( contentF instanceof FavoriteListFragment) outState.putString("myContent", FavoriteListFragment.ARG_ITEM_ID);
        else outState.putString("myContent", ProductListFragment.ARG_ITEM_ID);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_favorites:
                setFragmentTitle(R.string.favorites);
                favoriteListFragment = new FavoriteListFragment();
                switchContent(favoriteListFragment, FavoriteListFragment.ARG_ITEM_ID);
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.popBackStackImmediate());

        if (fragment != null){
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.contentFrame, fragment, tag);

            //Solo FavoriteListFragment se agrega al backStack

            if(!(fragment instanceof ProductListFragment)){
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentF = fragment;
        }
    }

    protected void setFragmentTitle(int resourseId) {
        setTitle(resourseId);
        getSupportActionBar().setTitle(resourseId);
    }

    /*
     * Se cierra la aplicacion cuando el stack tenga 0, es decir
     * cuando se tenga desplegado un ProductListFragment
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentF instanceof ProductListFragment
                || fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }
}
