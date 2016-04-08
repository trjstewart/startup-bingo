package co.startupbingo.startupbingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.api.ISocketClient;
import co.startupbingo.startupbingo.api.RealSocketClient;
import co.startupbingo.startupbingo.api.RestApiClient;
import co.startupbingo.startupbingo.model.GameEvent;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Inject IApiClient apiClient;
    @Inject ISocketClient socketClient;

    public Subscription joinRoomSubscription;
    public Socket socket;

    @Bind(R.id.main_page_coordinator_layout) CoordinatorLayout mCoordinator;
    @Bind(R.id.main_page_random_text) TextView mRandomText;
    @Bind(R.id.main_page_hashtag_edit_text) EditText mHashText;
    @Bind(R.id.main_page_user_edit_text) EditText mUserText;
    @Bind(R.id.main_page_bingo_button_layout) LinearLayout mButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dagger injection stoof
        ((StartupBingo)getApplication()).getNetComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRandomText();
        setupSubscriptions();
        setupInputs();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (joinRoomSubscription != null && !joinRoomSubscription.isUnsubscribed()){
            joinRoomSubscription.unsubscribe();
        }
    }

    private void setupSubscriptions() {
        joinRoomSubscription = socketClient.getObservableStream()
                .filter(gameEvent -> gameEvent.gameStateEvent== GameEvent.GAME_STATE_EVENT.JOIN)
                .subscribe(next->{
                    if (next!=null) {
                        Observable.from(next.returnedObjects).forEach(nextJSON-> {
                            if (nextJSON != null && nextJSON instanceof JSONObject) {
                                try {
                                    if (((JSONObject)nextJSON).getString("result").equals("true")){
                                        transitionToGameScreen();
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG,"JSON thing broke",e);
                                }
                            }
                        });
                    }
                },err->{
                    Log.e(TAG,"Join Room Error",err);
                });
    }

    private void setupInputs() {
        mUserText.setOnEditorActionListener((view, actionId, event) -> {
            if (view != null && actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
            return true;
        });
        mButtonLayout.setOnClickListener(v->{
            if (hashTagValid()) {
                socketClient.joinRoom(mUserText.getText().toString(),mHashText.getText().toString());
            } else {
                Snackbar.make(mCoordinator,"Form Has Errors", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hashTagValid() {
        return mHashText!=null&&!mHashText.getText().toString().isEmpty()
                &&mUserText!=null&&!mUserText.getText().toString().isEmpty();
    }

    private void transitionToGameScreen() {
        Intent gameIntent = new Intent(this,GameActivity.class);
        gameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(gameIntent);
    }

    private void generateRandomText() {
        String[] stringArray = getResources().getStringArray(R.array.random_texts);
        int len = stringArray.length;
        Random rand = new Random(System.currentTimeMillis());
        mRandomText.setText(stringArray[rand.nextInt(len)]);
    }

    private void setupRandomText() {
        generateRandomText();
        mRandomText.setOnClickListener(v->generateRandomText());
    }

}
