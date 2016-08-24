package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.*;

public class Bullet extends AbstractGameObject {
    private final RateLimiter moveRateLimiter = new RateLimiter(50);
    private final Direction direction;

    protected Bullet(Animation animation, Position position, Direction direction) {
        super(animation, position, new Body() {{
            hasGravity = false;
            onBoundary = OnBoundary.WalkThrough;
            isCollisionSubject = true;
        }});
        this.direction = direction;
    }

    @Override
    public void update(GameContext context) {
        if (moveRateLimiter.ready()) {
            int step = 4;
            getPosition().setX(getPosition().getX() + step * (direction.equals(Direction.Left) ? -1 : 1));
        }
    }
}
