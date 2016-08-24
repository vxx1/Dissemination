package com.outofmemory.entertainment.dissemination.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObjectRegistry {
    private final ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private final ArrayList<GameManager> managers = new ArrayList<GameManager>();

    public void add(GameObject gameObject) {
        objects.add(gameObject);
    }

    public void add(GameManager gameManager) {
        managers.add(gameManager);
    }

    public List<GameObject> getObjects() {
        return (List<GameObject>) objects.clone();
    }

    public List<GameManager> getManagers() {
        return (List<GameManager>) managers.clone();
    }

    public void remove(GameObject gameObject) {
        objects.remove(gameObject);
    }

}
