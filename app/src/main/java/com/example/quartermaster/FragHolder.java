package com.example.quartermaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FragHolder extends AppCompatActivity implements HomeScreen.OnFragmentInteractionListener, SettingsDialogFragment.SettingsDialogListener {

    private Fragment fragment = null;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private String cityString;
    private boolean activityPaused;
    private AudioManager audioManager;
    private String artist;
    private String track;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean playing = intent.getBooleanExtra("playing", false);
            String cmd = intent.getStringExtra("command");
            Log.v("tag ", action + " / " + cmd);
            artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            track = intent.getStringExtra("track");
            Log.i("tag", "This is the track information:");
            Log.v("tag", artist + " : " + album + " : " + track);
        }
    };

    public String getMetaData() {
        return track + " by " + artist;
    }

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
                bundle.putBoolean("activityState", activityPaused);
                bundle.putString("metaData", track + " by " + artist);
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
            }
        };
        timer.schedule(task, 0, 10000);

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("fm.last.android.playbackpaused");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.nullsoft.winamp.playstatechanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.amazon.mp3.playstatechanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.miui.player.playstatechanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.real.IMP.playstatechanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.rdio.android.playstatechanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.playstatechanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.andrew.apollo.playstatechanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.htc.music.playstatechanged");
        iF.addAction("com.spotify.music.playbackstatechanged");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.rhapsody.playstatechanged");

        registerReceiver(mReceiver, iF);

    }


    @Override
    public void onResume() {
        super.onResume();
        activityPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        activityPaused = true;
    }

    /**
     * Displays a settings dialog fragment over the current view
     *
     * @param view the window/fragment that contains the dialog
     */
    public void displaySettings(View view) {
        DialogFragment newSettingsFragment = new SettingsDialogFragment();
        newSettingsFragment.show(getSupportFragmentManager(), "HelpDialog");
    }

    /**
     * click handler that launches google home if it's installed, and displays a toast if it isn't
     * @param view the window/fragment that houses the button
     */
    @Override
    public void onClick(View view) {
//        Intent launchGoogleHome = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        Intent launchGoogleHome = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.chromecast.app");
        try {
            startActivity(launchGoogleHome);
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(view.getContext(), "Google Home is not installed.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * registers the "confirm" button click that updates the app settings
     * @param dialog the fragment where the "confirm" button is located
     */
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

    /**
     * changes the UI on orientation change
     * @param newConfig holds the new configuration (orientation)
     */
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

    /**
     * handles the functionality for the play/pause button. If the music is playing, pressing the
     * button pauses the music, and changes the button to a 'play' button. If the music is paused,
     * pressing the button will play the music, and changes the button to a 'pause' button.
     * @param view the window/fragment that houses the button
     */
    public void onClickPlayPause(View view) {
        ImageButton playPause = view.findViewById(R.id.mediaPlayPause);
        if (audioManager == null)
            audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        if (audioManager.isMusicActive()) {
            //if music is currently playing
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            FragHolder.this.sendBroadcast(i);
//            Toast.makeText(this, "PLAY/PAUSE REGISTER: PAUSE", Toast.LENGTH_SHORT).show();
        } else {
            //if music is paused
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "play");
            FragHolder.this.sendBroadcast(i);
//            Toast.makeText(this, "PLAY/PAUSE REGISTER: PLAY", Toast.LENGTH_SHORT).show();
        }

        //Second check changes the icon if the button press responds as successful

        if (audioManager.isMusicActive()) {
            playPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        } else {
            playPause.setImageResource(R.drawable.ic_pause_black_24dp);

        }
        Toast.makeText(this, track + " by " + artist, Toast.LENGTH_SHORT).show();
    }

    public void onClickPrev(View view) {
        if (audioManager == null)
            audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "previous");
        FragHolder.this.sendBroadcast(i);
//        Toast.makeText(this, "PREVIOUS REGISTER: PREVIOUS", Toast.LENGTH_SHORT).show();
    }

    public void onClickNext(View view) {
        if (audioManager == null)
            audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "next");
        FragHolder.this.sendBroadcast(i);
//        Toast.makeText(this, "PREVIOUS REGISTER: NEXT", Toast.LENGTH_SHORT).show();

    }

}
