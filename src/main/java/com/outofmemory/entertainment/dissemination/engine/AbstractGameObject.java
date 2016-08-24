package com.outofmemory.entertainment.dissemination.engine;

public abstract class AbstractGameObject implements GameObject {
    protected Animation animation;
    protected Position position;
    protected Body body;

    protected AbstractGameObject(Animation animation, Position position, Body body) {
        this.animation = animation;
        this.position = position;
        this.body = body;

    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void update(GameContext context) {

    }

    @Override
    public void onCollision(GameContext context, GameObject other) {

    }

    @Override
    public void onRemove(GameContext context) {

    }
}
