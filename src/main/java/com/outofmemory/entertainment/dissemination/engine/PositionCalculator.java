package com.outofmemory.entertainment.dissemination.engine;

public class PositionCalculator {
    public static Position calculateRelativePosition(Size mySize, Size parentSize, Position parentPosition, PositionProps props) {
        Position anchor = calculateAnchor(parentSize, parentPosition, props);
        return applyProps(mySize, anchor, props);
    }

    private static Position applyProps(Size size, Position anchor, PositionProps props) {
        int x = anchor.getX() + props.marginLeft - props.marginRight;
        int y = anchor.getY() + props.marginTop - props.marginBottom;
        Position position = new Position(x, y);

        switch (props.horizontalAlign) {
            case Left:
                break;
            case Right:
                position.setX(position.getX() - size.getWidth());
                break;
            case Center:
                position.setX(position.getX() - size.getWidth() / 2);
                break;
        }
        switch (props.verticalAlign) {
            case Top:
                break;
            case Bottom:
                position.setY(position.getY() - size.getHeight());
                break;
            case Center:
                position.setY(position.getY() - size.getHeight() / 2);
                break;
        }
        return position;
    }

    private static Position calculateAnchor(Size size, Position position, PositionProps props) {
        int x = 0;
        int y = 0;

        switch (props.horizontalAlign) {
            case Left:
                x = position.getX();
                break;
            case Right:
                x = position.getX() + size.getWidth();
                break;
            case Center:
                x = position.getX() + size.getWidth() / 2;
                break;
        }
        switch (props.verticalAlign) {
            case Top:
                y = position.getY();
                break;
            case Bottom:
                y = position.getY() + size.getHeight();
                break;
            case Center:
                y = position.getY() + size.getHeight() / 2;
                break;
        }
        return new Position(x, y);
    }
}
