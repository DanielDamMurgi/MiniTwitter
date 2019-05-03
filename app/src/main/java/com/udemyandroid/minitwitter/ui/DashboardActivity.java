package com.udemyandroid.minitwitter.ui;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.udemyandroid.minitwitter.R;
import com.udemyandroid.minitwitter.common.Constantes;
import com.udemyandroid.minitwitter.common.SharedPreferencesManager;

public class DashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView ivAvatar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment f = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    f = TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    fab.show();
                    break;
                case R.id.navigation_tweets_like:
                    f = TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    fab.hide();
                    break;
                case R.id.navigation_profile:
                        fab.hide();
                    break;
            }

            if (f != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, f)
                        .commit();

                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().hide();

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewToolbarPhoto);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoTweetDialogFragment dialog = new NuevoTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
            }
        });

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);

        if (photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .into(ivAvatar);
        }
    }

}
