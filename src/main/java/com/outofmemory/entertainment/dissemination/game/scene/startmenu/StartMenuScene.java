package com.outofmemory.entertainment.dissemination.game.scene.startmenu;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainScene;
import com.outofmemory.entertainment.dissemination.game.text.StartGameText;

public class StartMenuScene implements Scene {
    @Override
    public GameLoop buildLoop(IntellijContext intellijContext, GameExecutor gameExecutor) {
        AnimationRegistry animationRegistry = new AnimationRegistry();
        animationRegistry.parse("animation/text.xml");

        Canvas canvas = new Canvas(intellijContext.editor, intellijContext.project);
        Camera camera = new Camera(intellijContext.editor.getScrollingModel(), canvas);
        GameObjectRegistry gameObjectRegistry = new GameObjectRegistry();

        StartMenuSceneManager startMenuSceneManager = new StartMenuSceneManager(gameExecutor, new MainScene());
        StartGameText startGameText = new StartGameText(camera, animationRegistry.get("game-start", "idle"));
        gameObjectRegistry.add(startMenuSceneManager);
        gameObjectRegistry.add(startGameText);

        return new GameLoop(intellijContext.inputKey, canvas, camera, gameObjectRegistry);
    }
}
