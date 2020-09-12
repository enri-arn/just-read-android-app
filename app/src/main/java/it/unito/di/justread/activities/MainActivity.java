package it.unito.di.justread.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import it.unito.di.justread.R;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.fragments.BorrowedFragment;
import it.unito.di.justread.fragments.CatalogFragment;
import it.unito.di.justread.fragments.FavouriteFragment;
import it.unito.di.justread.fragments.HistoryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CatalogFragment.OnFragmentInteractionListener,
        BorrowedFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        FavouriteFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_catalog, 0);
            navigationView.setCheckedItem(R.id.nav_catalog);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
            case R.id.nav_catalog:
                fragment = CatalogFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_borrowed:
                fragment = BorrowedFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_history:
                fragment = HistoryFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_favourite:
                fragment = FavouriteFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_options:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_contact:
                intent = new Intent(this, ContatctActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                break;
            case R.id.nav_logout:
                if (logout()) {
                    Snackbar.make(getCurrentFocus(), "Logout succesfully", Snackbar.LENGTH_LONG).show();
                    intent = new Intent(this, LoginActivity.class);
                } else {
                    Snackbar.make(getCurrentFocus(), "Logout not succesfully", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (intent != null) {
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean logout() {
        final boolean[] logout = {false};
        GetJsonTask getJsonTask = new GetJsonTask("DELETE", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")){
                    logout[0] =  true;
                }
            }
        });
        getJsonTask.execute("http://192.168.0.1:8080/JustRead/api/logout");
        return logout[0];
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // we don't have fragment interaction but method is required by default.
    }
}
