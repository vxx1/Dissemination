package com.outofmemory.entertainment.dissemination.engine;

public interface Scene {
    GameLoop buildLoop(IntellijContext intellijContext, GameExecutor gameExecutor);
}
