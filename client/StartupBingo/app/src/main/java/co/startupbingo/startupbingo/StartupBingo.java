package co.startupbingo.startupbingo;

import android.app.Application;

import co.startupbingo.startupbingo.dependencies.AppModule;
import co.startupbingo.startupbingo.dependencies.DaggerNetComponent;
import co.startupbingo.startupbingo.dependencies.NetComponent;
import co.startupbingo.startupbingo.dependencies.NetModule;

public class StartupBingo extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("153.92.44.24"))
                .build();
        //baedit IP: 153.92.44.24
        //prod server: startupbingo.co
    }

    public NetComponent getNetComponent(){
        return mNetComponent;
    }

}
