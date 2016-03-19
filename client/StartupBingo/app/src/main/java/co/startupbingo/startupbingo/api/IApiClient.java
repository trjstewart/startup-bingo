package co.startupbingo.startupbingo.api;

import java.net.Socket;

import co.startupbingo.startupbingo.model.Word;
import rx.Observable;
import rx.Subscriber;

public interface IApiClient {
    Observable<Word> getRandomizedWordList();
}
