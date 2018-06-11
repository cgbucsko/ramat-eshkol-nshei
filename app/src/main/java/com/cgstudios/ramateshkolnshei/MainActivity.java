package com.cgstudios.ramateshkolnshei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cgstudios.ramateshkolnshei.fragments.Announcements;
import com.cgstudios.ramateshkolnshei.fragments.Babysitting;
import com.cgstudios.ramateshkolnshei.fragments.Classes;
import com.cgstudios.ramateshkolnshei.fragments.Clothing;
import com.cgstudios.ramateshkolnshei.fragments.Cosmetics;
import com.cgstudios.ramateshkolnshei.fragments.Events;
import com.cgstudios.ramateshkolnshei.fragments.Food;
import com.cgstudios.ramateshkolnshei.fragments.ForSale;
import com.cgstudios.ramateshkolnshei.fragments.Gemach;
import com.cgstudios.ramateshkolnshei.fragments.GeneralInfo;
import com.cgstudios.ramateshkolnshei.fragments.GivingAway;
import com.cgstudios.ramateshkolnshei.fragments.HashavasAveidah;
import com.cgstudios.ramateshkolnshei.fragments.Health;
import com.cgstudios.ramateshkolnshei.fragments.Home;
import com.cgstudios.ramateshkolnshei.fragments.Jobs;
import com.cgstudios.ramateshkolnshei.fragments.MazalTov;
import com.cgstudios.ramateshkolnshei.fragments.Photography;
import com.cgstudios.ramateshkolnshei.fragments.Rentals;
import com.cgstudios.ramateshkolnshei.fragments.Sales;
import com.cgstudios.ramateshkolnshei.fragments.Services;
import com.cgstudios.ramateshkolnshei.fragments.Shiurim;
import com.cgstudios.ramateshkolnshei.fragments.Wanted;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goHome();

        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Set status back to logged out
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().putBoolean("LOGIN_KEY", false).apply();
            startActivity(new Intent(this, LauncherActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String fragTag = null;

        if (id == R.id.nav_rentals) {
            fragment = new Rentals();
        } else if (id == R.id.nav_wanted) {
            fragment = new Wanted();
        } else if (id == R.id.nav_givingaway) {
            fragment = new GivingAway();
        } else if (id == R.id.nav_forsale) {
            fragment = new ForSale();
        } else if (id == R.id.nav_sales) {
            fragment = new Sales();
        } else if (id == R.id.nav_announcements) {
            fragment = new Announcements();
        }else if (id == R.id.nav_mazal_tov) {
            fragment = new MazalTov();
        } else if (id == R.id.nav_hashavas_avediah) {
            fragment = new HashavasAveidah();
        } else if (id == R.id.nav_shiurim) {
            fragment = new Shiurim();
        } else if (id == R.id.nav_classes) {
            fragment = new Classes();
        } else if (id == R.id.nav_health) {
            fragment = new Health();
        } else if (id == R.id.nav_events) {
            fragment = new Events();
        } else if (id == R.id.nav_clothing) {
            fragment = new Clothing();
        } else if (id == R.id.nav_cosmetics) {
            fragment = new Cosmetics();
        } else if (id == R.id.nav_food) {
            fragment = new Food();
        } else if (id == R.id.nav_photography) {
            fragment = new Photography();
        } else if (id == R.id.nav_services) {
            fragment = new Services();
        } else if (id == R.id.nav_babysitting) {
            fragment = new Babysitting();
        } else if (id == R.id.nav_jobs) {
            fragment = new Jobs();
        } else if (id == R.id.nav_general) {
            fragment = new GeneralInfo();
        } else if (id == R.id.nav_home) {
            fragment = new Home();
            fragTag = "HOME";
        } else if (id == R.id.nav_gemach) {
            fragment = new Gemach();
        }else if (id == R.id.nav_contact) {
            String[] address = new String[] {"ramateshkolnshei@gmail.com"};

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, address);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen_area, fragment, fragTag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goHome() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen_area, new Home(), "HOME");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
