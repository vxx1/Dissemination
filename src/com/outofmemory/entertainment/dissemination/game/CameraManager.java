package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.AbstractGameManager;
import com.outofmemory.entertainment.dissemination.engine.Camera;
import com.outofmemory.entertainment.dissemination.engine.GameContext;
import com.outofmemory.entertainment.dissemination.engine.Position;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainSceneManager;

public class CameraManager extends AbstractGameManager {

    public class Moving {
        private final Position targetPosition;
        private final int step;

        private Position currentPosition;
        private long startTime;

        public Moving(Position target, Position current, int step, long delay) {
            this.targetPosition = target;
            this.currentPosition = current;
            this.step = step;
            this.startTime = System.currentTimeMillis() + delay;
        }

        private int calculateDirection(int from, int to) {
            return (int) Math.signum(to - from);
        }

        private int calculateNextCoordinate(int current, int target) {
            int direction = calculateDirection(current, target);
            int next = current + (step * direction);
            int nextDirection = calculateDirection(next, target);

            // already here
            if (direction != nextDirection) {
                return Math.max(0, target);
            }
            return Math.max(0, next);
        }

        public Position nextMove() {
            boolean isWaiting = startTime >= System.currentTimeMillis();
            if (isWaiting) {
                return currentPosition;
            }
            int nextX = calculateNextCoordinate(currentPosition.getX(), targetPosition.getX());
            int nextY = calculateNextCoordinate(currentPosition.getY(), targetPosition.getY());
            Position next = new Position(nextX, nextY);
            currentPosition = next;
            return next;
        }
    }

    private final Camera camera;
    private final Avatar avatar;
    private final MainSceneManager mainSceneManager;
    private Moving moving;

    public CameraManager(Camera camera, Avatar avatar, MainSceneManager mainSceneManager) {
        this.camera = camera;
        this.avatar = avatar;
        this.mainSceneManager = mainSceneManager;
    }

    @Override
    public void update(GameContext context) {
        if (mainSceneManager.isEnded()) {
            return;
        }
        final Position position = calculateNewCameraPosition();

        if (!camera.getPosition().equals(position)) {
            if (moving == null || !moving.targetPosition.equals(position)) {
                moving = new Moving(position, camera.getPosition(), 2, 100);
            }
            Position nextMove = moving.nextMove();
            camera.move(nextMove);
            if (moving.targetPosition.equals(nextMove)) {
                moving = null;
            }
        }
    }

    private Position calculateNewCameraPosition() {
        int newX = calculateNewX();
        int newY = calculateNewY();
        return new Position(newX, newY);
    }

    private int calculateNewX() {
        return camera.getPosition().getX();
    }

    private int calculateNewY() {
        int delay = 8;
        boolean isNeedMoveUp = avatar.getPosition().getY() - camera.getPosition().getY() < delay;
        if (isNeedMoveUp) {
            return avatar.getPosition().getY() - delay;
        }
        int avatarHeight = avatar.getAnimation().current().getSize().getHeight();
        boolean isNeedMoveDown = (camera.getPosition().getY() + camera.getSize().getHeight())
                - (avatar.getPosition().getY() + avatarHeight) < delay;

        if (isNeedMoveDown) {
            return avatar.getPosition().getY() + avatarHeight + delay;
        }
        return camera.getPosition().getY();
    }

}
