package co.startupbingo.startupbingo.model;

/**
 * Created by jubb on 20/03/16.
 */
public class GameEvent {

    public enum GAME_STATE_EVENT{
        JOIN,
        WORDS,
        FINISHED
    }

    public GAME_STATE_EVENT gameStateEvent;
    public Object[] returnedObjects;

    public GameEvent(GAME_STATE_EVENT event, Object... returnedObject) {
        gameStateEvent = event;
        if (returnedObject!=null) {
            returnedObjects = returnedObject.clone();
        }
    }

}
