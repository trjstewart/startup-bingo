package co.startupbingo.startupbingo.api;

import co.startupbingo.startupbingo.model.GameEvent;
import co.startupbingo.startupbingo.model.Word;
import rx.Observable;

/**
 * Created by jubb on 20/03/16.
 */
public interface ISocketClient {
    Observable<GameEvent> getObservableStream();
    Observable<Word> getObservableWords();
    void connectToSocket();
    void joinRoom(String hashTag);
    void winGame(String username);
}