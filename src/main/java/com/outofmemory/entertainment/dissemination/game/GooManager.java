package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class GooManager extends AbstractGameManager {
    private final RateLimiter hardLevelLimiter = new RateLimiter(4 * 1000);
    private final RateLimiter spawnRateLimiter = new RateLimiter((long) (1 * 1000));
    private final Avatar avatar;
    private final List<Goo> spawnedChildren = new ArrayList<Goo>();

    private int maxChildrenCount = 1;

    public GooManager(Avatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public void update(GameContext context) {
        if (spawnRateLimiter.ready() && spawnedChildren.size() <= maxChildrenCount) {
            Collider avatarCollider = context.collisionDetector.buildCollider(avatar);
            while (true) {
                Position position = genInitialGooPosition(context);
                long growPeriod = 500;
                Goo goo = new Goo(position, new RateLimiter(growPeriod), this);
                Collider gooCollider = context.collisionDetector.buildCollider(goo);
                if (!avatarCollider.intersects(gooCollider)) {
                    context.gameObjectRegistry.add(goo);
                    spawnedChildren.add(goo);
                    break;
                }
            }
        }

        if (hardLevelLimiter.ready()) {
            maxChildrenCount = Math.min(6, maxChildrenCount + 1);
        }
    }

    private Position genInitialGooPosition(GameContext context) {
        Camera camera = context.camera;
        Position cameraPosition = context.camera.getPosition();
        int x = RandomUtil.range(cameraPosition.getX(), cameraPosition.getX() + camera.getSize().getWidth());
        int y = RandomUtil.range(cameraPosition.getY(), cameraPosition.getY() + camera.getSize().getHeight());
        return new Position(x, y);
    }

    public void onChildDie(Goo goo) {
        spawnedChildren.remove(goo);
    }
}
