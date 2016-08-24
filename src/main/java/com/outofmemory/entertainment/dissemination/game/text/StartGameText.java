package com.outofmemory.entertainment.dissemination.game.text;

import com.outofmemory.entertainment.dissemination.engine.*;

public class StartGameText extends AbstractGameObject {
    private final Camera camera;

    public StartGameText(Camera camera, Animation animation) {
        super(animation, null, new TextBody());
        this.camera = camera;
    }

    @Override
    public void update(GameContext context) {
        position = calculatePosition();
    }

    private Position calculatePosition() {
        Position cameraPosition = camera.getPosition();
        Position position = PositionCalculator.calculateRelativePosition(
                animation.current().getSize(), camera.getSize(), cameraPosition, new PositionProps() {{
                    verticalAlign = VerticalAlign.Center;
                    horizontalAlign = HorizontalAlign.Center;
                }}
        );
        position.setZ(10);
        return position;
    }

}
