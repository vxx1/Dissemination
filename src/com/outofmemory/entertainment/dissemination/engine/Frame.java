package com.outofmemory.entertainment.dissemination.engine;

import java.util.ArrayList;

public class Frame {
    private final ArrayList<String> rows;
    private final Size size;

    public Frame(ArrayList<String> rows) {
        this.rows = rows;
        int width = rows.isEmpty() ? 0 : rows.get(0).length();
        int height = rows.size();
        size = new Size(width, height);
    }

    public String getRow(int index) {
        return rows.get(index);
    }

    public Size getSize() {
        return size;
    }

    public ArrayList<String> getRows() {
        return rows;
    }
}
