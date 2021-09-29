package com.example.familymap.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBar.DisplayOptions;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.example.familymap.R;
import com.example.familymap.core.DataCache;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Main Activity";
    FragmentManager fm = getSupportFragmentManager();
    DataCache dataCache = DataCache.getInstance();
    //arbitrary request code
    public static final int REQUEST_CODE_SETTINGS = 1005;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null & dataCache.getUser() == null) {

            fragment = LoginFragment.newInstance();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        else {
            loginSuccessful();
        }
    }
    public void loginSuccessful()
    {
        invalidateOptionsMenu();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment != null)
            fm.beginTransaction().remove(fragment).commit();
        MapsFragment mapFragment = new MapsFragment();
        fm.beginTransaction().add(R.id.fragmentContainer, mapFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        if(dataCache.getUser() == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            new MenuInflater(this).inflate(R.menu.map_menu, menu);
            IconDrawable search = new IconDrawable(this,
                    FontAwesomeIcons.fa_search).colorRes(R.color.white).actionBarSize();
            IconDrawable settings = new IconDrawable(this,
                    FontAwesomeIcons.fa_gear).colorRes(R.color.white).actionBarSize();
            MenuItem searchIcon = menu.findItem(R.id.action_search);
            searchIcon.setIcon(search);
            searchIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            MenuItem settingsIcon = menu.findItem(R.id.action_settings);
            settingsIcon.setIcon(settings);
            settingsIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_search:
                goToSearch();
                return true;
            case R.id.action_settings:
                goToSettings();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_SETTINGS) {
            if (data == null) {
                return;
            }
            boolean logout = data.getBooleanExtra(SettingsActivity.LOGOUT, false);
            if (logout){
                logout();
                return;
            }
            boolean changed = data.getBooleanExtra(SettingsActivity.CHANGES, false);
            if (changed){
                loginSuccessful();
            }
        }
    }

    private void logout() {
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment != null)
            fm.beginTransaction().remove(fragment).commit();
        dataCache.clear();
        LoginFragment loginFragment = new LoginFragment();
        fm.beginTransaction().add(R.id.fragmentContainer, loginFragment).commit();
        invalidateOptionsMenu();
    }
}