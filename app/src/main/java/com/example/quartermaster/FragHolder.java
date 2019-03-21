package com.example.quartermaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FragHolder extends AppCompatActivity implements HomeScreen.OnFragmentInteractionListener, SettingsDialogFragment.SettingsDialogListener {

    private Fragment fragment = null;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private String cityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_holder);


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fragment = new HomeScreen();
//                System.out.println("FragHolder cityString = " + cityString);
                Bundle bundle = new Bundle();
                bundle.putString("cityID",cityString);
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
            }
        };
        timer.schedule(task, 0, 10000);
    }

    public void displaySettings(View view) {
        DialogFragment newSettingsFragment = new SettingsDialogFragment();
        newSettingsFragment.show(getSupportFragmentManager(), "HelpDialog");
    }

    @Override
    public void onClick(View v) {
//        Intent launchGoogleHome = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        Intent launchGoogleHome = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.chromecast.app");
        try {
            startActivity(launchGoogleHome);
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(v.getContext(), "Google Home is not installed.", Toast.LENGTH_SHORT).show();
        }

    }

//    public String getCityString () {
//        return cityString;
//    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
//        Toast.makeText(getApplicationContext(), "Weather will update on next refresh.", Toast.LENGTH_SHORT).show();
        cityString = Objects.requireNonNull(dialog.getArguments()).getString("cityID");
        fragment = new HomeScreen();
//                System.out.println("FragHolder cityString = " + cityString);
        Bundle bundle = new Bundle();
        bundle.putString("cityID", cityString);
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Toast.makeText(this, "Orientation changed, UI will update on next weather refresh.", Toast.LENGTH_SHORT).show();
        fragment = new HomeScreen();
//                System.out.println("FragHolder cityString = " + cityString);
        Bundle bundle = new Bundle();
        bundle.putString("cityID", cityString);
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
    }

}
