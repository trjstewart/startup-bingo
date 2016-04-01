package co.startupbingo.startupbingo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.game.IGameThread;

public class GameActivity extends AppCompatActivity{

    @Inject IApiClient apiClient;
    //@Inject IGameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StartupBingo)getApplication()).getNetComponent().inject(this);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupGameFragment();
    }

    private void setupGameFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.activity_game_frame_layout, GameActivityFragment.newInstance());
        ft.commit();
    }

}
