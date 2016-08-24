package com.outofmemory.entertainment.dissemination.game.scene.main;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.game.text.GameOverText;


public class MainSceneManager extends AbstractGameManager {

    public class GoingState extends AbstractGameState {
    }

    public class EndedState extends AbstractGameState {
        private MainSceneManager mainSceneManager;

        private EndedState(MainSceneManager mainSceneManager) {
            this.mainSceneManager = mainSceneManager;
        }

        @Override
        public void update(GameContext context) {
            Character key = context.inputKey;
            if (key == null) {
                return;
            }
            switch (key) {
                case ' ':
                    mainSceneManager.restartGame();
                    break;
                case 'q':
                    mainSceneManager.exitGame();
                    break;

            }
        }
    }

    private final long gameStartTime = System.currentTimeMillis();
    private final AnimationRegistry animationRegistry;
    private final GameExecutor gameExecutor;
    private final MainScene mainScene;
    private final Camera camera;

    private GameState gameState;

    public MainSceneManager(AnimationRegistry animationRegistry, GameExecutor gameExecutor, MainScene mainScene, Camera camera) {
        this.animationRegistry = animationRegistry;
        this.gameExecutor = gameExecutor;
        this.mainScene = mainScene;
        this.camera = camera;
        this.gameState = new GoingState();
    }

    public boolean isEnded() {
        return gameState instanceof MainSceneManager.EndedState;
    }

    public void restartGame() {
        gameExecutor.startGame(mainScene);
    }

    public void exitGame() {
        gameExecutor.stop();
    }

    public void gameOver(GameContext context) {
        GameOverText gameOverText = new GameOverText(
                camera,
                animationRegistry,
                getElapsedTime());

        gameOverText.update(context);

        context.gameObjectRegistry.add(gameOverText);

        gameState = new EndedState(this);
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - gameStartTime;
    }

    @Override
    public void update(GameContext context) {
        gameState.update(context);
    }

}
