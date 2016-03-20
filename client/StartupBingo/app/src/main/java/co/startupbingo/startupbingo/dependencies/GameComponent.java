package co.startupbingo.startupbingo.dependencies;

import javax.inject.Singleton;

import co.startupbingo.startupbingo.GameActivity;
import dagger.Subcomponent;

@Singleton
@Subcomponent(modules = {
        GameModule.class
})
public interface GameComponent {
    GameModule plus();
}
