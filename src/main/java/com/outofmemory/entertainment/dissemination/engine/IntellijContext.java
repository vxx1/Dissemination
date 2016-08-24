package com.outofmemory.entertainment.dissemination.engine;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.project.Project;

public class IntellijContext {
    public final InputKey inputKey;
    public final Editor editor;
    public final Project project;
    public final TypedAction typedAction;

    public IntellijContext(InputKey inputKey, Editor editor, Project project, TypedAction typedAction) {
        this.inputKey = inputKey;
        this.editor = editor;
        this.project = project;
        this.typedAction = typedAction;
    }
}
