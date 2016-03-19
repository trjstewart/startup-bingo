package co.startupbingo.startupbingo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import co.startupbingo.startupbingo.api.IApiClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameActivityFragment extends Fragment {

    @Inject IApiClient apiClient;

    public GameActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((StartupBingo)getActivity().getApplication()).getNetComponent().inject(this);
        return inflater.inflate(R.layout.fragment_game, container, false);
    }
}
