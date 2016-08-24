package com.outofmemory.entertainment.dissemination.engine;

public class InputKey {
    private Character key;

    public synchronized void put(char key) {
        this.key = key;
    }

    public synchronized Character take() {
        final Character key = this.key;
        this.key = null;
        return key;
    }
}
