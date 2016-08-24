package com.outofmemory.entertainment.dissemination.engine;

import java.util.HashSet;

public class Collider {
    private final HashSet<Integer> offsets;


    public Collider(HashSet<Integer> offsets) {
        this.offsets = offsets;
    }

    public boolean intersects(Collider other) {
        for (Integer leftOffset : offsets) {
            if (other.offsets.contains(leftOffset)) {
                return true;
            }
        }
        return false;
    }
}
