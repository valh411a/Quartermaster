package com.example.quartermaster;

import android.content.Intent;
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
    String cityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_holder);


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fragment = new HomeScreen();
                Bundle bundle = new Bundle();
                bundle.putString("cityID",cityString);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
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

    public void onDialogConfirm (View view) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        cityString = Objects.requireNonNull(dialog.getArguments()).getString("cityID");
    }
}
