package com.outofmemory.entertainment.dissemination.engine;

import java.util.ArrayList;

public class AnimationImpl implements Animation {
    private final ArrayList<Frame> frames;
    private int idx = -1;

    public AnimationImpl(ArrayList<Frame> frames) {
        this.frames = frames;
    }

    public Frame current() {
        if (idx == -1) {
            return frames.get(0);
        }
        return frames.get(idx);
    }

    public Frame moveNext() {
        if (idx == frames.size() - 1) {
            idx = -1;
        }
        idx++;
        return frames.get(idx);
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }
}
