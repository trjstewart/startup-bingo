package co.startupbingo.startupbingo.api;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import co.startupbingo.startupbingo.api.deserializers.WordListDeserializer;
import co.startupbingo.startupbingo.model.ClearWord;
import co.startupbingo.startupbingo.model.GameEvent;
import co.startupbingo.startupbingo.model.Score;
import co.startupbingo.startupbingo.model.User;
import co.startupbingo.startupbingo.model.Word;
import rx.Observable;
import rx.subjects.PublishSubject;


public class RealSocketClient implements ISocketClient {
    public String userName;
    public String gameId;
    public String roomName;
    public boolean gameInProgress;
    public PublishSubject<GameEvent> gameEventStream = PublishSubject.create();
    public PublishSubject<Word> wordStream = PublishSubject.create();
    public Socket mGameSocket;
    private Observable<Score> mScoreStream;
    private User currentUser;

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
            mGameSocket.on("restart",this::reFetchWords);
            mGameSocket.on("",this::translateToScores);
        }
    }

    @Override
    public User getCurrentUser() {
        return this.currentUser;
    }

    @Override
    public void translateToScores(Object[] objects) {

    }

    @Override
    public Observable<Score> getObservableScores(){
        return mScoreStream.asObservable();
    }

    public void scoreThing(Object[] objects) {

    }

    @Override
    public void reFetchWords(Object[] objects) {
        this.doWordThing();
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
    public void joinRoom(User newUser) {
        try {
            if (mGameSocket!=null){
                if (!mGameSocket.connected()) {
                    mGameSocket.connect();
                }
                JSONObject joinObject = new JSONObject()
                        .put("hashtag",newUser.userRoom)
                        .put("username",newUser.userName);
                mGameSocket.emit("join_room",joinObject);
                currentUser = newUser;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void emitEvent(String event, Object... objects) {

    }

    @Override
    public void selectWord(Word selectedWord) {
        try {
            if (mGameSocket != null) {
                if (!mGameSocket.connected()){
                    mGameSocket.connect();
                }
                String wordToFind = selectedWord.associatedWord;
                mGameSocket.emit("word-found",wordToFind);
            }
        } catch (Exception e){
            //probs do something here ey ahaha
        }
    }

    @Override
    public void winGame(String username) {

    }
}
