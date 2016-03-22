package co.startupbingo.startupbingo.api;

import co.startupbingo.startupbingo.model.GameEvent;
import rx.Observable;

/**
 * Created by jubb on 20/03/16.
 */
public interface ISocketClient {
    Observable<GameEvent> getObservableStream();
    void connectToSocket();
    void joinRoom(String hashTag);
    void winGame(String username);
}