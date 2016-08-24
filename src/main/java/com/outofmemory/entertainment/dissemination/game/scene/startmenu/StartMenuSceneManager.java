package com.outofmemory.entertainment.dissemination.game.scene.startmenu;

import com.outofmemory.entertainment.dissemination.engine.AbstractGameManager;
import com.outofmemory.entertainment.dissemination.engine.GameContext;
import com.outofmemory.entertainment.dissemination.engine.GameExecutor;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainScene;

public class StartMenuSceneManager extends AbstractGameManager {
    private final GameExecutor gameExecutor;
    private final MainScene mainScene;

    public StartMenuSceneManager(GameExecutor gameExecutor, MainScene mainScene) {
        this.gameExecutor = gameExecutor;
        this.mainScene = mainScene;
    }

    @Override
    public void update(GameContext context) {
        Character key = context.inputKey;
        if (key == null) {
            return;
        }
        switch (key) {
            case ' ':
                startGame();
        }
    }

    private void startGame() {
        gameExecutor.startGame(mainScene);
    }
}
