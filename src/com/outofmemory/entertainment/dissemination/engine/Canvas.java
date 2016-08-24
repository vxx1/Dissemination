package com.outofmemory.entertainment.dissemination.engine;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.outofmemory.entertainment.dissemination.util.ArrayUtil;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Canvas {

    private class SortByZAxis implements Comparator<GameObject> {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            return new Integer(o1.getPosition().getZ()).compareTo(o2.getPosition().getZ());
        }
    }

    private final Document document;
    private final Editor editor;
    private final CharSequence initialText;
    private final Project project;
    private final Size size;
    private final int codeWidth;
    private final char[][] canvas;

    public Canvas(Editor editor, Project project) {
        this.editor = editor;
        this.project = project;
        this.document = editor.getDocument();
        this.initialText = this.document.getText();
        int minWidth = 120;
        this.codeWidth = calculateCodeWidth(minWidth);
        this.canvas = prepareCanvas();
        this.size = new Size(minWidth, this.canvas.length);

    }

    public Size getSize() {
        return size;
    }

    public void release() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.setText(initialText);
            }
        };
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }

    private int calculateCodeWidth(int minWidth) {
        int codeWidth = minWidth;

        for (int i = 0; i < document.getLineCount(); i++) {
            int lineStartOffset = document.getLineStartOffset(i);
            int lineEndOffset = document.getLineEndOffset(i);
            int lineWidth = lineEndOffset - lineStartOffset;
            if (lineWidth > codeWidth) {
                codeWidth = lineWidth;
            }
        }
        return codeWidth;
    }


    private char[][] prepareCanvas() {
        char[][] canvas = new char[document.getLineCount()][codeWidth];
        for (int i = 0; i < document.getLineCount(); i++) {
            int lineStartOffset = document.getLineStartOffset(i);
            int lineEndOffset = document.getLineEndOffset(i);
            String line = document.getText(new TextRange(lineStartOffset, lineEndOffset));
            canvas[i] = StringUtils.rightPad(line, codeWidth, " ").toCharArray();
        }
        return canvas;
    }

    public void render(List<GameObject> gameObjects) {
        final CharSequence text = buildText(gameObjects);
        if (!document.getCharsSequence().equals(text)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    document.setText(text);
                }
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }

    private CharSequence buildText(List<GameObject> gameObjects) {
        char[][] newCanvas = ArrayUtil.cloneArray(canvas);
        Collections.sort(gameObjects, new SortByZAxis());
        for (GameObject gameObject : gameObjects) {
            Position position = gameObject.getPosition();
            Frame frame = gameObject.getAnimation().current();

            for (int i = 0; i < frame.getSize().getHeight(); i++) {
                String frameRow = frame.getRow(i);
                for (int j = 0; j < frameRow.length(); j++) {
                    int x = position.getX() + j;
                    int y = position.getY() + i;
                    if (isInsideCanvas(x, y)) {
                        newCanvas[y][x] = frameRow.charAt(j);
                    }

                }
            }
        }
        StringBuilder textBuilder = new StringBuilder();
        for (char[] row : newCanvas) {
            textBuilder.append(row);
            textBuilder.append('\n');
        }
        return textBuilder.toString();
    }

    private boolean isInsideCanvas(int x, int y) {
        return x >= 0 && x <= size.getWidth() - 1 && y >= 0 && y < size.getHeight() - 1;
    }

    public boolean isInsideCanvas(Position position) {
        return isInsideCanvas(position.getX(), position.getY());
    }

    public boolean isOutsideCanvas(GameObject gameObject) {
        Frame frame = gameObject.getAnimation().current();
        Position pos = gameObject.getPosition();
        boolean isLeftUpInside = isInsideCanvas(pos.getX(), pos.getY());
        boolean isRightUpInside = isInsideCanvas(pos.getX() + frame.getSize().getWidth(), pos.getY());
        boolean isLeftDownInside = isInsideCanvas(pos.getX(), pos.getY() - frame.getSize().getHeight());
        boolean isRightDownInside = isInsideCanvas(
                pos.getX() + frame.getSize().getWidth(), pos.getY() - frame.getSize().getHeight());

        return !(isLeftUpInside || isRightUpInside || isLeftDownInside || isRightDownInside);
    }

    private int position2offset(Position position) {
        return document.getLineStartOffset(position.getY()) + position.getX();
    }

    public Position offset2position(int offset) {
        int y = document.getLineNumber(offset);
        int x = offset - document.getLineStartOffset(y);
        return new Position(x, y);
    }

    public Position xyToPosition(Point point) {
        return offset2position(editor.logicalPositionToOffset(editor.xyToLogicalPosition(point)));
    }

    public Point position2xy(Position position) {
        return editor.logicalPositionToXY(editor.offsetToLogicalPosition(position2offset(position)));
    }

    public void checkBoundary(GameObject gameObject) {
        if (gameObject.getBody().onBoundary.equals(OnBoundary.Stop)) {
            Frame collider = gameObject.getAnimation().current();

            if (gameObject.getPosition().getX() < 0) {
                gameObject.getPosition().setX(0);
            }
            if (gameObject.getPosition().getX() + collider.getSize().getWidth() > size.getWidth() - 1) {
                gameObject.getPosition().setX(size.getWidth() - collider.getSize().getWidth() - 1);
            }
        }
    }

    public void checkFall(GameObject gameObject) {
        if (gameObject.getBody().hasGravity && !hasFloor(gameObject)) {
            Position position = gameObject.getPosition();
            position.setY(position.getY() + 1);
        }
    }

    private boolean hasFloor(GameObject gameObject) {
        Frame collider = gameObject.getAnimation().current();
        int underY = gameObject.getPosition().getY() + collider.getSize().getHeight();
        if (underY >= size.getHeight()) {
            return false;
        }
        int rightX = gameObject.getPosition().getX() + collider.getSize().getWidth();
        for (int i = gameObject.getPosition().getX(); i < rightX; i++) {
            char character = getCharacter(new Position(i, underY));
            if (character != ' ') {
                return true;
            }
        }
        return false;
    }

    private char getCharacter(Position position) {
        return canvas[position.getY()][position.getX()];
    }
}
