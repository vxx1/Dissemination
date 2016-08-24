package com.outofmemory.entertainment.dissemination.engine;

public class Collision {
    private final GameObject left;
    private final GameObject right;

    public Collision(GameObject left, GameObject right) {
        this.left = left;
        this.right = right;
    }

    public GameObject getLeft() {
        return left;
    }

    public GameObject getRight() {
        return right;
    }
}
