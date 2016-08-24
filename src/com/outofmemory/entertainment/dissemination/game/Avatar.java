package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainSceneManager;

public class Avatar extends AbstractGameObject {
    private class WalkingState implements GameState {
        private final RateLimiter idleTimer = new RateLimiter(200);
        private boolean isIdle = true;

        @Override
        public void update(GameContext context) {
            boolean hasMoved = processInput(context);
            if (hasMoved) {
                isIdle = false;
                idleTimer.reset();
            }

            if (!isIdle && idleTimer.ready()) {
                isIdle = true;
                changeAnimation(getIdleAnimation());
            }
        }

        public String getIdleAnimation() {
            if (direction == Direction.Left) {
                return "idle-left";
            } else {
                return "idle-right";
            }
        }
    }

    private class DyingState implements GameState {
        private final RateLimiter dieTimer = new RateLimiter(2000);

        @Override
        public void update(GameContext context) {
            if (dieTimer.ready()) {
                finallyDie(context);
            }
        }
    }

    private class JumpingState implements GameState {
        private final RateLimiter hopTimer = new RateLimiter(50);
        private int hopsCount = 4;

        public JumpingState() {
            getBody().hasGravity = false;
            hop();
        }

        private void hop() {
            position.setY(position.getY() - 2);
            hopsCount--;
        }

        @Override
        public void update(GameContext context) {
            processInput(context);
            if (hopTimer.ready()) {
                hop();
                if (hopsCount <= 0) {
                    getBody().hasGravity = true;
                    state = new WalkingState();
                }
            }
        }
    }


    private final MainSceneManager mainSceneManager;
    private final BulletManager bulletManager;
    private final RateLimiter shootingRateLimiter = new RateLimiter(500);
    private final AnimationRegistry animationRegistry;
    private final int moveStep = 3;

    private Direction direction;
    private GameState state;

    public Avatar(MainSceneManager mainSceneManager, BulletManager bulletManager, Position position, AnimationRegistry animationRegistry) {
        super(animationRegistry.get("avatar", "idle"), position, new Body() {{
            onBoundary = OnBoundary.Stop;
            hasGravity = true;
            isCollisionSubject = true;
        }});
        this.mainSceneManager = mainSceneManager;
        this.bulletManager = bulletManager;
        this.animationRegistry = animationRegistry;
        state = new WalkingState();
    }

    private void moveDown() {
        position.setY(position.getY() + 1);
    }

    private void moveUp() {
        position.setY(position.getY() - 1);
    }

    private void moveRight() {
        position.setX(position.getX() + moveStep);
        direction = Direction.Right;
        changeAnimation("walk-right");
    }

    private void moveLeft() {
        position.setX(position.getX() - moveStep);
        direction = Direction.Left;
        changeAnimation("walk-left");
    }

    @Override
    public void update(GameContext context) {
        state.update(context);
    }

    private void changeAnimation(String animationId) {
        animation = animationRegistry.get("avatar", animationId);
    }

    private boolean processInput(GameContext context) {
        final Character key = context.inputKey;
        if (key == null) {
            return false;
        }
        switch (key) {
            case 'a':
                moveLeft();
                return true;
            case 'd':
                moveRight();
                return true;
            case 'w':
                moveUp();
                return true;
            case 's':
                moveDown();
                return true;
            case '.':
                shoot(context);
                return false;
            case ' ':
                jump();
                return true;
        }
        return false;
    }

    private void jump() {
        state = new JumpingState();
    }

    private void shoot(GameContext context) {
        if (shootingRateLimiter.ready()) {
            if (direction == null) {
                direction = Direction.Right;
                changeAnimation("idle-right");
            }
            bulletManager.createBullet(this, direction, context);
        }
    }

    @Override
    public void onCollision(GameContext context, GameObject other) {
        if (other instanceof Goo) {
            die();
        }
    }

    @Override
    public void onRemove(GameContext context) {
        finallyDie(context);
    }

    private void die() {
        if (state instanceof DyingState) {
            return;
        }
        position.setZ(10);
        state = new DyingState();
        changeAnimation("die");
    }

    private void finallyDie(GameContext context) {
        context.gameObjectRegistry.remove(this);
        mainSceneManager.gameOver(context);
    }
}
