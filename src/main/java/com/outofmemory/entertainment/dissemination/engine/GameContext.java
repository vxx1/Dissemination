package com.outofmemory.entertainment.dissemination.engine;

public class GameContext {
    public final Character inputKey;
    public final GameObjectRegistry gameObjectRegistry;
    public final Canvas canvas;
    public final CollisionDetector collisionDetector;
    public final Camera camera;

    public GameContext(Character inputKey, GameObjectRegistry gameObjectRegistry, Canvas canvas, CollisionDetector collisionDetector, Camera camera) {
        this.inputKey = inputKey;
        this.gameObjectRegistry = gameObjectRegistry;
        this.canvas = canvas;
        this.collisionDetector = collisionDetector;
        this.camera = camera;
    }
}
