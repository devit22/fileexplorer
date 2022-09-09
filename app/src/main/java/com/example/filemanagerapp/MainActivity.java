package com.example.filemanagerapp;

import static android.os.Build.VERSION.SDK_INT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.filemanagerapp.fragments.ExternalFragment;
import com.example.filemanagerapp.fragments.HomeFragment;
import com.example.filemanagerapp.fragments.InternFagment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

private DrawerLayout drawerLayout;
private Toolbar toolbar;
private NavigationView navigationView;
FileRecyclerAdapter fileRecyclerAdapter;
    String[] permissions = { android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE};
    int permissioncode = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectlayout();

        setSupportActionBar(toolbar);

        if(SDK_INT > Build.VERSION_CODES.M)
        {
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions,permissioncode);
            }
        }

       navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open_Drawyer,R.string.Close_Drawyer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        openFragment(new HomeFragment());
        navigationView.setCheckedItem(R.id.home);
    }

    private void connectlayout() {
        drawerLayout = findViewById(R.id.drawyerlayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigaiton_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case   R.id.home:
                openFragment(new HomeFragment());
                break;
            case  R.id.external_storage:
                openFragment(new ExternalFragment());
                break;
            case   R.id.interl_storage:
                openFragment(new InternFagment());
                break;
            case R.id.about_more:
                Toast.makeText(getApplicationContext(),"about is press",Toast.LENGTH_LONG).show();
                break;
        }
         drawerLayout.closeDrawer(GravityCompat.START);
         return true;
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

  void  openFragment(Fragment fragment){
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.fragement_container,fragment);
      fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utils.isPermissionGranted(this))
        {
            new AlertDialog.Builder(this)
                    .setTitle("All Files permission")
                    .setMessage("Due to android 11 the file permission required by the app at runtime ok")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            takepermssion();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else
        {
            Toast.makeText(getApplicationContext(), "permission already granted here", Toast.LENGTH_SHORT).show();
        }
    }

    private void takepermssion() {
        if(SDK_INT>= Build.VERSION_CODES.R)
        {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);

                requestpermissionlanucher.launch(intent);
            }catch (Exception e){
                e.printStackTrace();
                Intent intent =new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                requestpermissionlanucher.launch(intent);
            }

        }else{
            if(SDK_INT > Build.VERSION_CODES.M)
            {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(permissions,permissioncode);
                }
            }
        }
    }
    ActivityResultLauncher<Intent> requestpermissionlanucher= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}