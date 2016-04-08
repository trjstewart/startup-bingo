package co.startupbingo.startupbingo.api;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import co.startupbingo.startupbingo.api.deserializers.WordListDeserializer;
import co.startupbingo.startupbingo.model.ClearWord;
import co.startupbingo.startupbingo.model.GameEvent;
import co.startupbingo.startupbingo.model.Word;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;


public class RealSocketClient implements ISocketClient {
    public String userName;
    public String gameId;
    public String roomName;
    public boolean gameInProgress;
    public PublishSubject<GameEvent> gameEventStream = PublishSubject.create();
    public PublishSubject<Word> wordStream = PublishSubject.create();
    public Socket mGameSocket;

    public RealSocketClient() {
        IO.Options options = new IO.Options();
        options.port=3000;
        try{
            mGameSocket = IO.socket("http://swagme.me",options);
            setupSocketCallbacks();
            mGameSocket.connect();
        } catch (Exception e){
            mGameSocket = null;
        }
    }

    private void setupSocketCallbacks() {
        if (mGameSocket!=null) {
            mGameSocket.on("join_room", f ->
                    gameEventStream.onNext(new GameEvent(GameEvent.GAME_STATE_EVENT.JOIN, f)));
            mGameSocket.on("game_finished",f->
                    gameEventStream.onNext(new GameEvent(GameEvent.GAME_STATE_EVENT.FINISHED,f)));
            mGameSocket.on("words",this::translateToWords);
        }
    }

    private void translateToWords(Object[] f) {
        //Do thing
        if (f[0] != null && f[0] instanceof JSONObject) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Word[].class, new WordListDeserializer());
            Gson gson = gsonBuilder.create();
            Word[] words = gson.fromJson(f[0].toString(),Word[].class);
            wordStream.onNext(new ClearWord("meme"));
            for(Word word : words) {
                wordStream.onNext(word);
            }
        }
    }

    @Override
    public Observable<Word> getObservableWords() {
        return wordStream!=null?wordStream.asObservable():null;
    }

    @Override
    public void doWordThing() {
        if (mGameSocket!=null) {
            if (!mGameSocket.connected()) {
                mGameSocket.connect();
            }
            mGameSocket.emit("words");
        }
    }

    @Override
    public Observable<GameEvent> getObservableStream() {
        return gameEventStream!=null?gameEventStream.asObservable():null;
    }

    @Override
    public void connectToSocket(){
        mGameSocket.connect();
    }

    @Override
    public void joinRoom(String userName, String hashTag) {
        try {
            if (mGameSocket!=null){
                if (!mGameSocket.connected()) {
                    mGameSocket.connect();
                }
                JSONObject joinObject = new JSONObject()
                        .put("hashtag",hashTag)
                        .put("username",userName);

                mGameSocket.emit("join_room",joinObject);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void winGame(String username) {

    }
}
