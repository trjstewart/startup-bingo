package co.startupbingo.startupbingo;

import android.app.Application;

import co.startupbingo.startupbingo.dependencies.AppModule;
import co.startupbingo.startupbingo.dependencies.DaggerGameComponent;
import co.startupbingo.startupbingo.dependencies.DaggerNetComponent;
import co.startupbingo.startupbingo.dependencies.GameComponent;
import co.startupbingo.startupbingo.dependencies.NetComponent;
import co.startupbingo.startupbingo.dependencies.NetModule;
import co.startupbingo.startupbingo.game.IGameThread;

public class StartupBingo extends Application {

    private NetComponent mNetComponent;
    private GameComponent mGameComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("swagme.me"))
                .build();
        mGameComponent = DaggerGameComponent.builder().build();
        //baedit IP: 153.92.44.24
        //prod server: startupbingo.co
    }

    public NetComponent getNetComponent(){
        return mNetComponent;
    }

    public GameComponent getGameComponent() {
        return mGameComponent;
    }
}
