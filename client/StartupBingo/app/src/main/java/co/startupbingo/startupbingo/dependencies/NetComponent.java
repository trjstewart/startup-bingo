package co.startupbingo.startupbingo.dependencies;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.GameActivity;
import co.startupbingo.startupbingo.GameActivityFragment;
import co.startupbingo.startupbingo.GameActivityOverFragment;
import co.startupbingo.startupbingo.LaunchActivity;
import co.startupbingo.startupbingo.MainActivity;
import dagger.Component;

@Singleton
@Component(modules={
        AppModule.class,
        NetModule.class
})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(LaunchActivity activity);
    void inject(GameActivity activity);
    void inject(GameActivityFragment gameActivityFragment);
    void inject(GameActivityOverFragment gameActivityOverFragment);
}
