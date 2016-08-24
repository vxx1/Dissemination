package com.outofmemory.entertainment.dissemination.game;

public class RateLimiter {
    private final long period;
    private long lastTime;

    public RateLimiter(long period) {
        this.period = period;
        reset();
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
    }

    public boolean ready() {
        long now = System.currentTimeMillis();
        if (now - period > lastTime) {
            lastTime = now;
            return true;
        }
        return false;
    }
}
