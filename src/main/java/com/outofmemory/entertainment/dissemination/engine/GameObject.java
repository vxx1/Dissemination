package com.outofmemory.entertainment.dissemination.engine;

public interface GameObject extends GameState {
    Animation getAnimation();

    Position getPosition();

    Body getBody();

    void onCollision(GameContext context, GameObject other);

    void onRemove(GameContext context);
}
