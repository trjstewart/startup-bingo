package co.startupbingo.startupbingo.dependencies;


import android.app.Application;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.game.IGameThread;
import co.startupbingo.startupbingo.game.SocketGameThread;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}
