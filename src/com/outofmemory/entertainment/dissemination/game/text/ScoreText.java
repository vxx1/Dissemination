package com.outofmemory.entertainment.dissemination.game.text;

import com.outofmemory.entertainment.dissemination.engine.*;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainSceneManager;
import com.outofmemory.entertainment.dissemination.util.Util;

import java.util.ArrayList;

public class ScoreText extends AbstractGameObject {
    private final Camera camera;
    private final MainSceneManager mainSceneManager;

    public ScoreText(Camera camera, MainSceneManager mainSceneManager) {
        super(buildAnimation(0), null, new TextBody());
        this.camera = camera;
        this.mainSceneManager = mainSceneManager;
    }

    private static Animation buildAnimation(long time) {
        ArrayList<String> rows = new ArrayList<String>();
        rows.add("/** " + Util.formatTime(time) + "**/");
        return new SingleFrameAnimation(new Frame(rows));
    }

    @Override
    public void update(GameContext context) {
        position = calculatePosition();

        if (mainSceneManager.isEnded()) {
            return;
        }
        animation = buildAnimation(mainSceneManager.getElapsedTime());
    }


    private Position calculatePosition() {
        Position pos = PositionCalculator.calculateRelativePosition(
                animation.current().getSize(), camera.getSize(), camera.getPosition(), new PositionProps() {{
                    verticalAlign = VerticalAlign.Top;
                    horizontalAlign = HorizontalAlign.Right;
                    marginTop = 1;
                    marginRight = 8;
                }}
        );
        pos.setZ(10);
        return pos;
    }


}
