package co.startupbingo.startupbingo.api;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import co.startupbingo.startupbingo.model.GameEvent;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by jubb on 20/03/16.
 */
public class RealSocketClient implements ISocketClient {
    public String userName;
    public String gameId;
    public String roomName;
    public boolean gameInProgress;
    public PublishSubject<GameEvent> gameEventStream;
    public Socket mGameSocket;

    public RealSocketClient() {
        IO.Options options = new IO.Options();
        options.port=3000;
        try{
            mGameSocket = IO.socket("http://startupbingo.co",options);
            mGameSocket.connect();
        } catch (Exception e){
            mGameSocket = null;
        }
        gameEventStream = PublishSubject.create();
        setupSocketCallbacks();
    }

    private void setupSocketCallbacks() {
        if (mGameSocket!=null){
            mGameSocket.on("join_room",f->
                    gameEventStream.onNext(new GameEvent(GameEvent.GAME_STATE_EVENT.JOIN,f)));
            mGameSocket.on("game_finished",f->
                    gameEventStream.onNext(new GameEvent(GameEvent.GAME_STATE_EVENT.FINISHED,f)));
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
    public void joinRoom(String hashTag) {
        if (mGameSocket!=null){
            if (!mGameSocket.connected()) {
                mGameSocket.connect();
            }
            mGameSocket.emit("join_room",hashTag);
        }
    }

    @Override
    public void winGame(String username) {

    }
}
