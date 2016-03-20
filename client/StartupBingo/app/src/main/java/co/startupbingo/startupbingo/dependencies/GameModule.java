package co.startupbingo.startupbingo.dependencies;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.game.IGameThread;
import co.startupbingo.startupbingo.game.SocketGameThread;
import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    IGameThread mGameThread;

    public GameModule(){
        mGameThread = new SocketGameThread();
    }

    @Provides
    @Singleton
    IGameThread provideGameThread(){
        return mGameThread;
    }

}
