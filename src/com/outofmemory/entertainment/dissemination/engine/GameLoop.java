package com.outofmemory.entertainment.dissemination.engine;

import java.util.Collection;

public class GameLoop {
    private final InputKey inputKey;
    private final Canvas canvas;
    private final Camera camera;
    private final GameObjectRegistry gameObjectRegistry;
    private final CollisionDetector collisionDetector;

    public GameLoop(InputKey inputKey, Canvas canvas, Camera camera, GameObjectRegistry gameObjectRegistry) {
        this.inputKey = inputKey;
        this.canvas = canvas;
        this.camera = camera;
        this.gameObjectRegistry = gameObjectRegistry;
        this.collisionDetector = new CollisionDetector(gameObjectRegistry, canvas);
    }

    private GameContext buildContext() {
        Character key = inputKey.take();
        return new GameContext(key, gameObjectRegistry, canvas, collisionDetector, camera);
    }

    public void start() {
        final GameContext gameContext = buildContext();
        for (GameManager gameManager : gameObjectRegistry.getManagers()) {
            gameManager.start(gameContext);
        }
    }

    public void release() {
        canvas.release();
    }

    public void update() {
        GameContext gameContext = buildContext();

        for (GameManager gameManager : gameObjectRegistry.getManagers()) {
            gameManager.update(gameContext);
        }
        for (GameObject gameObject : gameObjectRegistry.getObjects()) {
            gameObject.getAnimation().moveNext();
            gameObject.update(gameContext);
            canvas.checkBoundary(gameObject);
            canvas.checkFall(gameObject);
            if (canvas.isOutsideCanvas(gameObject)) {
                gameObject.onRemove(gameContext);
                gameObjectRegistry.remove(gameObject);
            }
        }
        Collection<Collision> collisions = collisionDetector.detectCollisions();

        for (Collision collision : collisions) {
            collision.getLeft().onCollision(gameContext, collision.getRight());
            collision.getRight().onCollision(gameContext, collision.getLeft());
        }

        canvas.render(gameObjectRegistry.getObjects());
    }
}
