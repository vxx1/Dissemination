package com.outofmemory.entertainment.dissemination.game.text;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.util.Util;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GameOverText extends AbstractGameObject {
    private final Camera camera;

    public GameOverText(Camera camera, AnimationRegistry animationRegistry, long score) {
        super(buildAnimation(score, animationRegistry), null, new TextBody());
        this.camera = camera;
    }

    private static Animation buildAnimation(long score, AnimationRegistry registry) {
        Frame header = registry.get("game-over", "header").current();
        Frame footer = registry.get("game-over", "footer").current();
        Frame scoreFrame = inscribeScoreText(buildScoreText(score, registry));
        return new SingleFrameAnimation(
                Util.concatFramesVertically(new ArrayList<Frame>(Arrays.asList(header, scoreFrame, footer)))
        );
    }

    private static Frame inscribeScoreText(Frame frame) {
        ArrayList<String> rows = new ArrayList<String>();
        for (String row : frame.getRows()) {
            rows.add("/**" + StringUtils.repeat(" ", 11) + row + StringUtils.repeat(" ", 10) + "**/");
        }
        return new Frame(rows);
    }

    private static Frame buildScoreText(long score, AnimationRegistry registry) {
        char[] formattedTime = Util.formatTime(score).toCharArray();
        ArrayList<Frame> frames = new ArrayList<Frame>();

        for (char digitOrSeparator : formattedTime) {
            if (digitOrSeparator == ':') {
                final Frame separatorFrame = registry.get("game-over", "separator").getFrames().get(0);
                frames.add(separatorFrame);
            } else {
                int digit = Integer.parseInt(String.valueOf(digitOrSeparator));
                Frame digitFrame = registry.get("game-over", "digits").getFrames().get(digit);
                frames.add(digitFrame);
            }
        }
        return Util.concatFramesHorizontally(frames);
    }


    @Override
    public void update(GameContext context) {
        position = calculatePosition();
    }

    private Position calculatePosition() {
        Position pos = PositionCalculator.calculateRelativePosition(
                animation.current().getSize(), camera.getSize(), camera.getPosition(), new PositionProps() {{
                    verticalAlign = VerticalAlign.Center;
                    horizontalAlign = HorizontalAlign.Center;
                }}
        );
        pos.setZ(10);
        return pos;
    }
}
