package co.startupbingo.startupbingo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    @Bind(R.id.main_page_random_text) TextView mRandomText;
    @Bind(R.id.main_page_hashtag_edit_text) EditText mHashText;
    @Bind(R.id.main_page_user_edit_text) EditText mUserText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRandomText();
        setupInputs();
    }

    private void setupInputs() {
        mUserText.setOnEditorActionListener((view,actionId,event)->{
            if (view != null && actionId == EditorInfo.IME_ACTION_DONE){
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                view.clearFocus();
            }
            return true;
        });
    }

    private void setupRandomText() {
        String[] stringArray = getResources().getStringArray(R.array.random_texts);
        int len = stringArray.length;
        Random rand = new Random(System.currentTimeMillis());
        mRandomText.setText(stringArray[rand.nextInt(len)]);
    }

}
