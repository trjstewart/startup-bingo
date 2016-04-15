package co.startupbingo.startupbingo.api.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import co.startupbingo.startupbingo.model.Score;
import co.startupbingo.startupbingo.model.User;
import co.startupbingo.startupbingo.model.Word;

/**
 * Created by jubb on 20/03/16.
 */
public class ScoreListDeserializer implements JsonDeserializer<Score[]> {

    private String roomName;

    public ScoreListDeserializer(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public Score[] deserialize(final JsonElement json, final Type typeOfT
            ,final JsonDeserializationContext context) throws JsonParseException {

        final JsonArray jsonObject = json.getAsJsonArray();
        final Score[] scoreArray = new Score[jsonObject.size()];
        for (int i=0; i< scoreArray.length; i++){
            final JsonObject jsonScore = jsonObject.get(i).getAsJsonObject();
            final String jsonUserName = jsonScore.get("username").getAsString();
            final Integer jsonScoreInteger = jsonScore.get("score").getAsInt();
            scoreArray[i] = new Score(new User(jsonUserName,roomName),jsonScoreInteger);
        }
        return scoreArray;
    }
}
