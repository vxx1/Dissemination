package com.outofmemory.entertainment.dissemination.engine;

public class SingleFrameAnimation implements Animation {
    private Frame frame;

    public SingleFrameAnimation(Frame frame) {
        this.frame = frame;
    }

    @Override
    public Frame current() {
        return frame;
    }

    @Override
    public Frame moveNext() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }
}
