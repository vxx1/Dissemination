package com.outofmemory.entertainment.dissemination.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CollisionDetector {

    private final GameObjectRegistry gameObjectRegistry;
    private final Canvas canvas;

    public CollisionDetector(GameObjectRegistry gameObjectRegistry, Canvas canvas) {

        this.gameObjectRegistry = gameObjectRegistry;
        this.canvas = canvas;
    }


    public Collection<Collision> detectCollisions() {

        ArrayList<Collision> collisions = new ArrayList<Collision>();
        List<GameObject> gameObjects = gameObjectRegistry.getObjects();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject left = gameObjects.get(i);
            if (!left.getBody().isCollisionObject || !left.getBody().isCollisionSubject) {
                continue;
            }
            Collider leftCollider = buildCollider(left);

            for (GameObject right : gameObjects) {
                if (left == right || !right.getBody().isCollisionObject) {
                    continue;
                }
                Collider rightCollider = buildCollider(right);
                if (leftCollider.intersects(rightCollider)) {
                    collisions.add(new Collision(left, right));
                }
            }
        }
        return collisions;
    }


    public Collider buildCollider(GameObject gameObject) {
        HashSet<Integer> offsets = new HashSet<Integer>();
        Position position = gameObject.getPosition();
        Frame frame = gameObject.getAnimation().current();

        for (int i = 0; i < frame.getSize().getHeight(); i++) {
            String frameRow = frame.getRow(i);
            for (int j = 0; j < frameRow.length(); j++) {
                int x = position.getX() + j;
                int y = position.getY() + i;
                if (frameRow.charAt(j) != ' ' && x < canvas.getSize().getWidth()) {
                    int offset = (y * canvas.getSize().getWidth()) + x;
                    offsets.add(offset);
                }
            }
        }
        return new Collider(offsets);
    }
}
