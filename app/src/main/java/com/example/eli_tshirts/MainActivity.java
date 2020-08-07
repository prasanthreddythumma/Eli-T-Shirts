package com.example.eli_tshirts;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new TShirtFragment());
        fragmentTransaction.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //  MenuItem item = menu.findItem(R.id.action_cart);

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {

            invalidateOptionsMenu();
            startActivity(new Intent(MainActivity.this, CartListActivity.class));

            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setCheckable(true);
        drawer.closeDrawers();
        drawer.animate();

        if(item.getItemId()== R.id.nav_item1){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new TShirtFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.nav_item2){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new PoloShirtFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.nav_item3){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new FormalShirtFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.nav_item4){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new CasualShirtFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.nav_item5){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new HoodieFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.nav_item6){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new OthersFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.my_cart){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new MyCartFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.my_account){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new MyProfileFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.my_orders){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new MyOrdersFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId()== R.id.my_wishlist){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new MyWishlistFragment());
            fragmentTransaction.commit();
        }
        return true;
    }
}