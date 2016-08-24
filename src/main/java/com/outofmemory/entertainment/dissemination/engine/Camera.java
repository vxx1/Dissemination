package com.outofmemory.entertainment.dissemination.engine;

import com.intellij.openapi.editor.ScrollingModel;
import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;

import javax.swing.*;
import java.awt.*;

public class Camera implements VisibleAreaListener {
    private ScrollingModel scrollingModel;
    private final Canvas canvas;

    private Size size;
    private Position position;

    public Camera(ScrollingModel scrollingModel, Canvas canvas) {
        this.scrollingModel = scrollingModel;
        this.canvas = canvas;
        initialRead();
        scrollingModel.addVisibleAreaListener(this);
    }

    public Size getSize() {
        return size;
    }

    private void mapScrollingToCamera() {
        Point scrollPoint = new Point(scrollingModel.getHorizontalScrollOffset(), scrollingModel.getVerticalScrollOffset());
        int scrollHeight = scrollingModel.getVisibleArea().height;
        int scrollWidth = scrollingModel.getVisibleArea().width;
        Point scrollPointDown = new Point(scrollPoint.x, scrollPoint.y + scrollHeight);
        Point scrollPointRight = new Point(scrollPoint.x + scrollWidth, scrollPoint.y);

        position = canvas.xyToPosition(scrollPoint);
        int cameraWidth = canvas.xyToPosition(scrollPointRight).getX() - position.getX();
        int cameraHeight = canvas.xyToPosition(scrollPointDown).getY() - position.getY();
        size = new Size(cameraWidth, cameraHeight);

    }

    private void initialRead() {
        if (SwingUtilities.isEventDispatchThread()) {
            mapScrollingToCamera();
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        mapScrollingToCamera();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("failed start camera");
            }
        }
    }

    public Position getPosition() {
        return position;
    }

    public void move(final Position newPosition) {
        if (position.equals(newPosition)) {
            return;
        }
        if (!canvas.isInsideCanvas(position)) {
            return;
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    final Point point = canvas.position2xy(newPosition);
                    if (newPosition.getX() != position.getX()) {
                        scrollingModel.scrollHorizontally(point.x);
                    }
                    if (newPosition.getY() != position.getY()) {
                        scrollingModel.scrollVertically(point.y);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed move camera");
        }
    }

    @Override
    public void visibleAreaChanged(VisibleAreaEvent e) {
        mapScrollingToCamera();
    }
}
