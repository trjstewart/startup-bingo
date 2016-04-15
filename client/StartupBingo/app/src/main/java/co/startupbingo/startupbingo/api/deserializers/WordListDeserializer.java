package co.startupbingo.startupbingo.api.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import co.startupbingo.startupbingo.model.Word;

/**
 * Created by jubb on 20/03/16.
 */
public class WordListDeserializer implements JsonDeserializer<Word[]> {
    @Override
    public Word[] deserialize(final JsonElement json, final Type typeOfT
                            ,final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonElement statusElement = jsonObject.get(Word.STATUS);
        final int statusInt = statusElement.getAsInt();
        if (statusInt!=200){
            throw new JsonParseException("Status Code not valid: Returned "+statusInt);
        }
        final JsonArray jsonWordArray = jsonObject.getAsJsonArray(Word.WORD_LIST_DATA);
        final Word[] wordArray = new Word[jsonWordArray.size()];
        for (int i=0; i< wordArray.length; i++) {
            final JsonElement jsonWord = jsonWordArray.get(i);
            wordArray[i] = new Word(jsonWord.getAsString());
        }
        return wordArray;
    }
}
