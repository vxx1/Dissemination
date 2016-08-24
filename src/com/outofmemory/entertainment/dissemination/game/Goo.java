package com.outofmemory.entertainment.dissemination.game;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.util.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class Goo extends AbstractGameObject {
    private final static ArrayList<Character> characters = new ArrayList<Character>(Arrays.asList('1', '0', ' '));

    private final SingleFrameAnimation myAnimation;
    private final RateLimiter growRateLimiter;
    private final RateLimiter blinkRateLimiter;
    private final GooManager gooManager;

    public Goo(Position position, RateLimiter growRateLimiter, GooManager gooManager) {
        super(new SingleFrameAnimation(genFrame()), position, new Body() {{
            onBoundary = OnBoundary.WalkThrough;
            hasGravity = false;
        }});
        this.growRateLimiter = growRateLimiter;
        this.blinkRateLimiter = new RateLimiter(200);
        this.gooManager = gooManager;
        myAnimation = (SingleFrameAnimation) animation;

    }

    private static Frame genFrame() {
        return genFrame(RandomUtil.range(3, 10), RandomUtil.range(3, 10));
    }

    private static Frame genFrame(int height, int width) {
        ArrayList<String> rows = new ArrayList<String>();
        for (int i = 0; i < height; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < width; j++) {
                row.append(genGooSymbol());
            }
            rows.add(row.toString());
        }
        return new Frame(rows);
    }

    private static char genGooSymbol() {
        return RandomUtil.element(characters);
    }

    @Override
    public void onCollision(GameContext context, GameObject other) {
        if (other instanceof Bullet) {
            context.gameObjectRegistry.remove(other);
            shrink();
            if (myAnimation.current().getSize().getHeight() < 3 || myAnimation.current().getSize().getWidth() < 3) {
                gooManager.onChildDie(this);
                context.gameObjectRegistry.remove(this);
            }
        }
    }

    @Override
    public void update(GameContext context) {
        if (blinkRateLimiter.ready()) {
            blink();
        }
        if (growRateLimiter.ready()) {
            grow();
        }
    }

    private void blink() {
        Frame oldFrame = myAnimation.current();
        Frame newFrame = genFrame(oldFrame.getSize().getHeight(), oldFrame.getSize().getWidth());
        myAnimation.setFrame(newFrame);
    }

    private void grow() {
        final int growStep = 2;
        Frame oldFrame = myAnimation.current();
        Frame newFrame = genFrame(oldFrame.getSize().getHeight() + growStep, oldFrame.getSize().getWidth() + growStep);

        myAnimation.setFrame(newFrame);

        position.setY(Math.max(position.getY() - 1, 0));
        position.setX(Math.max(position.getX() - 1, 0));
    }

    private void shrink() {
        int shrinkStep = 12;
        Frame oldFrame = myAnimation.current();
        Frame newFrame = genFrame(
                Math.max(oldFrame.getSize().getHeight() - shrinkStep, 0),
                Math.max(oldFrame.getSize().getWidth() - shrinkStep, 0)
        );
        myAnimation.setFrame(newFrame);
    }


}
