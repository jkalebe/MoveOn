package com.example.moveon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.flcontent,
                    new MapasFragment()).commit();
        }

        mDrawerlayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(toggle);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void selectIterDrawer(MenuItem menuItem){
        Fragment myFragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.mapas:
                fragmentClass = MapasFragment.class;
                break;

            case R.id.scontr:
                fragmentClass = SContrFragment.class;
                break;

            case R.id.config:
                fragmentClass = ConfigFragment.class;
                break;

            case R.id.about:
                fragmentClass = SobreFragment.class;
                break;

            case R.id.logout:
                fragmentClass = SairFragment.class;
                break;
            default:
                fragmentClass = MapasFragment.class;
                break;

        }

        try{
            myFragment = (Fragment) fragmentClass.newInstance();

        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerlayout.closeDrawers();

    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectIterDrawer(menuItem);
                return true;
            }
        });

    }

}
