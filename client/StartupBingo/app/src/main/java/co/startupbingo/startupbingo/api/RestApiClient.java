package co.startupbingo.startupbingo.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import co.startupbingo.startupbingo.api.deserializers.ApiParseException;
import co.startupbingo.startupbingo.api.deserializers.WordListDeserializer;
import co.startupbingo.startupbingo.model.Word;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

public class RestApiClient implements IApiClient {

    public static class Builder{
        private String nestedUrl;
        private OkHttpClient nestedClient;

        public Builder(){

        }

        public Builder okHttpClient(OkHttpClient httpClient){
            this.nestedClient = httpClient;
            return this;
        }

        public Builder baseUrl(String baseUrl){
            this.nestedUrl = baseUrl;
            return this;
        }

        public RestApiClient build(){
            if (nestedUrl==null||nestedClient==null){
                throw new RuntimeException("Missing Parameters");
            }
            return new RestApiClient(nestedUrl,nestedClient);
        }
    }

    private String baseUrl;
    private OkHttpClient httpClient;

    public RestApiClient(String baseUrl, OkHttpClient httpClient){
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public Observable<Word> getRandomizedWordList() {
        return Observable.create(sub->{
            try{
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("http")
                        .host(baseUrl)
                        .addPathSegment("api")
                        .addPathSegment("words")
                        .build();
                Request httpRequest = new Request.Builder()
                        .url(httpUrl)
                        .get()
                        .build();
                Response response =httpClient.newCall(httpRequest).execute();
                if (response.isSuccessful()){
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Word[].class, new WordListDeserializer());
                    Gson gson = gsonBuilder.create();
                    Word[] words = gson.fromJson(response.body().string(),Word[].class);
                    for(Word word : words){
                        sub.onNext(word);
                    }
                    sub.onCompleted();
                } else {
                    sub.onError(new ApiParseException("Response Failed"));
                }
            } catch (Exception e){
                sub.onError(e);
            }
        });
    }
}
