package com.outofmemory.entertainment.dissemination;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.outofmemory.entertainment.dissemination.engine.InputKey;
import org.jetbrains.annotations.NotNull;

public class GameTypedHandler implements TypedActionHandler {
    private final InputKey inputKey;

    public GameTypedHandler(InputKey inputKey) {
        this.inputKey = inputKey;
    }

    @Override
    public void execute(@NotNull Editor editor, char c, @NotNull DataContext dataContext) {
        inputKey.put(c);
    }
}

