package co.startupbingo.startupbingo.dependencies;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.GameActivityFragment;
import co.startupbingo.startupbingo.game.IGameThread;
import dagger.Component;

@Singleton
@Component(modules={
        GameModule.class
})
public interface GameComponent {
    IGameThread gameThread();
}
