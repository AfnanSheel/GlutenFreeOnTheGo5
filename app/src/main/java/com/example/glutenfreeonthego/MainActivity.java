package com.example.glutenfreeonthego;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.glutenfreeonthego.databinding.ActivityMainBinding;
import com.example.glutenfreeonthego.databinding.NavigationResBinding;
import com.example.glutenfreeonthego.databinding.ToolbarBinding;

public class MainActivity extends AppCompatActivity {

    private NavigationResBinding navigationResBinding;
    private ActivityMainBinding activityMainBinding;
    private ToolbarBinding toolbarBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationResBinding = NavigationResBinding.inflate(getLayoutInflater());
        setContentView(navigationResBinding.getRoot());
        activityMainBinding = navigationResBinding.mainActivity;
        toolbarBinding = activityMainBinding.toolbar;

        setSupportActionBar(toolbarBinding.toolbar);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle (
                this,
                navigationResBinding.navDrawer,
                toolbarBinding.toolbar,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer
        );
        navigationResBinding.navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        NavigationUI.setupWithNavController(
        navigationResBinding.navigation,
        navController);
    }
}