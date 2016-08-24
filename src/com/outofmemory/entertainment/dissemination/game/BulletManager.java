package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.*;

public class BulletManager extends AbstractGameManager {
    private final AnimationRegistry animationRegistry;

    public BulletManager(AnimationRegistry animationRegistry) {
        this.animationRegistry = animationRegistry;
    }

    public void createBullet(Avatar avatar, Direction direction, GameContext context) {
        Position bulletPosition = findBulletPosition(avatar, direction);
        String animationId = getAnimationId(direction);
        AnimationImpl bulletAnimation = animationRegistry.get("bullet", animationId);
        Bullet bullet = new Bullet(bulletAnimation, bulletPosition, direction);
        context.gameObjectRegistry.add(bullet);
    }

    private Position findBulletPosition(Avatar avatar, Direction direction) {
        int y = avatar.getPosition().getY() + 4;
        int rightToAvatar = avatar.getPosition().getX() + avatar.getAnimation().current().getSize().getWidth();
        int x = direction.equals(Direction.Left) ? avatar.getPosition().getX() - 1 : rightToAvatar;
        return new Position(x, y);
    }

    private String getAnimationId(Direction direction) {
        if (direction.equals(Direction.Left)) {
            return "bullet-left";
        }
        return "bullet-right";
    }
}
