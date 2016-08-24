package com.outofmemory.entertainment.dissemination.util;

import com.outofmemory.entertainment.dissemination.engine.Frame;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

public class Util {
    public static String formatTime(long time) {
        long totalSeconds = time / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return StringUtils.leftPad(Long.toString(minutes), 2, '0') + ":" + StringUtils.leftPad(Long.toString(seconds), 2, '0');

    }

    public static Frame concatFramesHorizontally(ArrayList<Frame> frames) {
        ArrayList<String> rows = new ArrayList<String>();
        int height = frames.get(0).getRows().size();
        for (int i = 0; i < height; i++) {
            StringBuilder builder = new StringBuilder();
            for (Frame frame : frames) {
                builder.append(frame.getRow(i));
            }
            rows.add(builder.toString());
        }
        return new Frame(rows);
    }

    public static Frame concatFramesVertically(ArrayList<Frame> frames) {
        ArrayList<String> rows = new ArrayList<String>();
        for (Frame frame : frames) {
            rows.addAll(frame.getRows());
        }
        return new Frame(rows);
    }
}
