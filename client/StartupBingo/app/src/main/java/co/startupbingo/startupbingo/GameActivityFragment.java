package co.startupbingo.startupbingo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.model.Word;
import co.startupbingo.startupbingo.ui.GameGridRecyclerAdapter;
import rx.Observable;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameActivityFragment extends Fragment implements GameGridRecyclerAdapter.GameGridRecyclerEvents {

    @Inject IApiClient apiClient;
    @Bind(R.id.fragment_game_recycler) RecyclerView mRecycler;
    Context mContext;
    GameGridRecyclerAdapter mAdapter;

    public GameActivityFragment() {

    }

    public static GameActivityFragment newInstance() {
        return new GameActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((StartupBingo)getActivity().getApplication()).getNetComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new GameGridRecyclerAdapter(getContext());
        mAdapter.setOnEventsListener(this);
        setupRecycler();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupRecycler() {
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        if (mAdapter ==null){
            mAdapter = new GameGridRecyclerAdapter(getContext());
            mAdapter.setOnEventsListener(this);
        }
        mRecycler.setAdapter(mAdapter);

        Observable.range(0,25)
                .map(i->new Word(String.valueOf(i)))
                .doOnNext(mAdapter::addWord).subscribe();
    }

    @Override
    public void onClickTile(Word selectedWord, int position) {
        //Wew lad
    }

    @Override
    public void onLongClickTile(Word selectedTile, int position) {
        //Wew lad
    }
}
