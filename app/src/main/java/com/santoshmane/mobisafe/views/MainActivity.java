package com.santoshmane.mobisafe.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobisafe.R;
import com.example.mobisafe.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding mbinding;
    public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Binding Main Activity
        mbinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mbinding.getRoot());
        //BottomNavigationView
        bottomNavigationView=mbinding.bottomNavView;
        //
        //Setting Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){

            }else{
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.SEND_SMS}, 2);
            }
        }
        else{
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //Setting Permissions
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Getting Selected bottom item
                int item_id = item.getItemId();
                //Operations on Selected Items
                if(item_id == R.id.LocationNav){
                    loadFragment(new ShareLocationFragment(),true);
                } else if (item_id == R.id.ContactsNav) {
                    loadFragment(new ContactsListFragment(),false);
                }
                else {
                    //Emergency Contacts
                    loadFragment(new EmergencyContactsFragments(),false);
                }
                return true;
            }
        });
        //By Default Fragment Selected
        bottomNavigationView.setSelectedItemId(R.id.LocationNav);
    }
    //LoadFragment() function
    public void loadFragment(Fragment fragment , boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag){
            ft.add(R.id.frameContainer,fragment);
        }
        else{
            ft.replace(R.id.frameContainer,fragment);
        }
        ft.commit();
    }
}