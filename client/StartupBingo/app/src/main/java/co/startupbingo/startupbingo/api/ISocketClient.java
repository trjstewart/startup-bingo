package co.startupbingo.startupbingo.api;

import org.json.JSONObject;

import co.startupbingo.startupbingo.model.GameEvent;
import co.startupbingo.startupbingo.model.Score;
import co.startupbingo.startupbingo.model.User;
import co.startupbingo.startupbingo.model.Word;
import rx.Observable;

/**
 * Created by jubb on 20/03/16.
 */
public interface ISocketClient {
    Observable<GameEvent> getObservableStream();
    Observable<Word> getObservableWords();
    void connectToSocket();
    void joinRoom(User user);
    void winGame(String username);
    void sendMessage(String message);
    void emitEvent(String event, Object... objects);
    void doWordThing();
    void selectWord(Word selectedWord);
    void reFetchWords(Object[] objects);
    void scoreThing(Object[] objects);
    Observable<Score> getObservableScores();
    User getCurrentUser();
    void translateToScores(Object[] objects);
}