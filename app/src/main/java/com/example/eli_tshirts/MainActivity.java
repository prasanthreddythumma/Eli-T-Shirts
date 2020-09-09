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

/**
 * This activity works the moment app runs.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    static ViewPager viewPager;
    static TabLayout tabLayout;

    /**
     * this is the method that works first when activity starts.
     * @param savedInstanceState : Bundle element.
     */
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
        Bundle extras = getIntent().getExtras();

        String recall;
        if(extras == null || extras.isEmpty()){
            recall ="";
        }
        else {
            recall = extras.getString("fragment");
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(recall.isEmpty())
            fragmentTransaction.add(R.id.fragment_container, new TShirtFragment());
        else if(recall.equals("CasualShirts"))
            fragmentTransaction.replace(R.id.fragment_container,new CasualShirtFragment());
        else if(recall.equals("TShirts"))
            fragmentTransaction.replace(R.id.fragment_container,new TShirtFragment());
        else if(recall.equals("FormalShirts"))
            fragmentTransaction.replace(R.id.fragment_container,new FormalShirtFragment());
        else if(recall.equals("Hoodies"))
            fragmentTransaction.replace(R.id.fragment_container,new HoodieFragment());
        else if(recall.equals("Others"))
            fragmentTransaction.replace(R.id.fragment_container,new OthersFragment());
        else if(recall.equals("Polo TShirts"))
            fragmentTransaction.replace(R.id.fragment_container,new PoloShirtFragment());

        fragmentTransaction.commit();


    }

    /**
     * To resume the paused application
     */
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    /**
     * To get beck to the previous page.
     */
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * To add the menu buttons.
     * @param menu : menu elements.
     * @return : returns true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * prepares the screen's standard options to be displayed.
     * @param menu :menu element.
     * @return : returns menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //  MenuItem item = menu.findItem(R.id.action_cart);

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * handles the click events.
     * @param item : passing the menu item.
     * @return :if handles successfully then returns true, otherwise it returns the false.
     */
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


    /**
     * called when an item in the navigation menu is selected.
     * @param item the selected item.
     * @return : True to display the item as the selected.
     */
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
            startActivity(new Intent(this,CartListActivity.class));
        }
        else if(item.getItemId()== R.id.my_account){
            startActivity(new Intent(this,ProfileActivity.class));
        }
        else if(item.getItemId()== R.id.my_orders){
            startActivity(new Intent(this,OrdersActivity.class));
        }
        else if(item.getItemId()== R.id.my_wishlist){
            startActivity(new Intent(this,WishListActivity.class));
        }
        return true;
    }
}