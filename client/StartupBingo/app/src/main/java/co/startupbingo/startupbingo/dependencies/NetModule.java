package co.startupbingo.startupbingo.dependencies;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.api.IApiClient;
import co.startupbingo.startupbingo.api.RestApiClient;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class NetModule {
    String mBaseUrl;

    public NetModule(String baseUrl){
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application){
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(),cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache){
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    IApiClient provideApiClient(OkHttpClient httpClient){
        return new RestApiClient.Builder()
                .baseUrl(mBaseUrl)
                .okHttpClient(httpClient)
                .build();
    }
}
