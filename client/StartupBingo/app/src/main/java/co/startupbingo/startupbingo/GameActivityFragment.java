package co.startupbingo.startupbingo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.api.ISocketClient;
import co.startupbingo.startupbingo.dependencies.DaggerGameComponent;
import co.startupbingo.startupbingo.game.IGameThread;
import co.startupbingo.startupbingo.model.GameEvent;
import co.startupbingo.startupbingo.model.Word;
import co.startupbingo.startupbingo.ui.GameGridRecyclerAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameActivityFragment extends Fragment implements GameGridRecyclerAdapter.GameGridRecyclerEvents {

    private static final String TAG = GameActivityFragment.class.getName();
    @Inject IApiClient apiClient;
    @Inject ISocketClient socketClient;
    public IGameThread gameThreadInstance;
    @Bind(R.id.fragment_game_recycler) RecyclerView mRecycler;
    Context mContext;
    GameGridRecyclerAdapter mAdapter;

    public GameActivityFragment() {

    }

    public static GameActivityFragment newInstance() {
        return new GameActivityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameThreadInstance = ((StartupBingo)getActivity().getApplication())
                .getGameComponent().gameThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        StartupBingo app = ((StartupBingo)getActivity().getApplication());
        app.getNetComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new GameGridRecyclerAdapter(getContext());
        mAdapter.setOnEventsListener(this);
        setupRecycler();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupRecycler() {
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        if (mAdapter ==null){
            mAdapter = new GameGridRecyclerAdapter(getContext());
            mAdapter.setOnEventsListener(this);
        }
        mRecycler.setAdapter(mAdapter);
        socketClient.getObservableWords()
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter::addWord, e -> {
                    Log.e(TAG, "Word Subject Error", e);
                });
        socketClient.doWordThing();
    }

    @Override
    public void onClickTile(Word selectedWord, int position) {
        if (selectedWord.isChecked) {
            //Send clicked thing
            socketClient.selectWord(selectedWord);
        }
    }

    @Override
    public void onLongClickTile(Word selectedTile, int position) {
        //Wew lad
    }
}
