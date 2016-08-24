package com.outofmemory.entertainment.dissemination.game.scene.main;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.game.Avatar;
import com.outofmemory.entertainment.dissemination.game.BulletManager;
import com.outofmemory.entertainment.dissemination.game.CameraManager;
import com.outofmemory.entertainment.dissemination.game.GooManager;
import com.outofmemory.entertainment.dissemination.game.text.ScoreText;

public class MainScene implements Scene {
    public GameLoop buildLoop(IntellijContext intellijContext, GameExecutor gameExecutor) {
        AnimationRegistry animationRegistry = new AnimationRegistry();

        animationRegistry.parse("animation/avatar.xml");
        animationRegistry.parse("animation/text.xml");
        animationRegistry.parse("animation/bullet.xml");

        Canvas canvas = new Canvas(intellijContext.editor, intellijContext.project);
        Camera camera = new Camera(intellijContext.editor.getScrollingModel(), canvas);
        GameObjectRegistry gameObjectRegistry = new GameObjectRegistry();

        MainSceneManager mainSceneManager = new MainSceneManager(animationRegistry, gameExecutor, this, camera);
        ScoreText scoreText = new ScoreText(camera, mainSceneManager);
        BulletManager bulletManager = new BulletManager(animationRegistry);
        Avatar avatar = new Avatar(
                mainSceneManager,
                bulletManager,
                findAvatarPosition(camera),
                animationRegistry
        );
        CameraManager cameraManager = new CameraManager(camera, avatar, mainSceneManager);
        GooManager gooManager = new GooManager(avatar);

        gameObjectRegistry.add(mainSceneManager);
        gameObjectRegistry.add(scoreText);
        gameObjectRegistry.add(bulletManager);
        gameObjectRegistry.add(avatar);
        gameObjectRegistry.add(gooManager);
        gameObjectRegistry.add(cameraManager);


        return new GameLoop(intellijContext.inputKey, canvas, camera, gameObjectRegistry);
    }

    private Position findAvatarPosition(Camera camera) {
        final Position cameraPosition = camera.getPosition();
        int x = 50;
        int y = cameraPosition.getY() + camera.getSize().getHeight() / 2;
        return new Position(x, y);
    }
}
