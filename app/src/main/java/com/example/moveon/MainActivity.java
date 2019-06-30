package com.example.moveon;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mapbox.mapboxsdk.Mapbox;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle toggle;
    public static final String GOOGLE_ACCOUNT = "google_account";
    private TextView profileName, profileEmail;
    private ImageView profileImage;
    public GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);

        Mapbox.getInstance(this, "pk.eyJ1IjoiamVyZW1pYXNrYWxlYmUiLCJhIjoiY2p2NzI0bXBtMDFwNzQ0bXgyYmt2eTFmNCJ9.kUMdrgj0HguRCn1bn20wRw");
        setContentView(R.layout.activity_main);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);

        View headerView = nvDrawer.getHeaderView(0);
        profileName = headerView.findViewById(R.id.profilename);
        profileEmail = headerView.findViewById(R.id.profileemail);
        profileImage = headerView.findViewById(R.id.profileimage);
        setDataOnView();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.flcontent,
                    new MapasFragment()).commit();
        }

        mDrawerlayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(toggle);


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

    private void setDataOnView() {

            Picasso.get().load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);
            profileName.setText(googleSignInAccount.getDisplayName());
            profileEmail.setText(googleSignInAccount.getEmail());

    }


}
