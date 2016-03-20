package co.startupbingo.startupbingo;

import android.content.Intent;
import android.os.Bundle;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.api.RestApiClient;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Inject IApiClient apiClient;

    public Subscription wordListSubscription;
    public Socket socket;

    @Bind(R.id.main_page_random_text) TextView mRandomText;
    @Bind(R.id.main_page_hashtag_edit_text) EditText mHashText;
    @Bind(R.id.main_page_user_edit_text) EditText mUserText;
    @Bind(R.id.main_page_bingo_button_layout) LinearLayout mButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dagger injection stoof
        ((StartupBingo)getApplication()).getNetComponent().inject(this);

        try {
            Log.d(TAG,"Creating Socket");
            IO.Options options = new IO.Options();
            options.port = 3000;
            socket = IO.socket("http://startupbingo.co",options);
            Log.d(TAG, "Socket Created");
            socket.connect();
            Log.d(TAG, "Socket Connected");
            socket.emit("test", "Hey Tylor");
            Log.d(TAG,"Socket Test Emission");
        } catch (Exception e){
            //Probably think about logging this
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRandomText();
        setupInputs();
        setupSubscriptions();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (wordListSubscription!=null&&!wordListSubscription.isUnsubscribed()){
            wordListSubscription.unsubscribe();
        }
    }

    private void setupSubscriptions() {
        if (wordListSubscription!=null){
            wordListSubscription.unsubscribe();
        }
        wordListSubscription = apiClient.getRandomizedWordList()
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .retry(3)
                .subscribe(n->{
                    Log.d(TAG,n.associatedWord);
                },e->{
                    Log.e(TAG,"Word List Error",e);
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
            transitionToGameScreen();
        });
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
