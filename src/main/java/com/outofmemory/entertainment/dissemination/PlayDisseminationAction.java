package com.outofmemory.entertainment.dissemination;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.outofmemory.entertainment.dissemination.engine.GameExecutor;
import com.outofmemory.entertainment.dissemination.engine.InputKey;
import com.outofmemory.entertainment.dissemination.engine.IntellijContext;
import com.outofmemory.entertainment.dissemination.game.scene.main.MainScene;
import com.outofmemory.entertainment.dissemination.game.scene.startmenu.StartMenuScene;

public class PlayDisseminationAction extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        EditorImpl editorImpl = (EditorImpl) editor;

        EditorActionManager actionManager = EditorActionManager.getInstance();
        TypedAction typedAction = actionManager.getTypedAction();
        InputKey inputKey = new InputKey();
        IntellijContext intellijContext = new IntellijContext(inputKey, editorImpl, project, typedAction);
        final GameExecutor gameExecutor = new GameExecutor(intellijContext);

        gameExecutor.startGame(new StartMenuScene());

        EditorFactory.getInstance().addEditorFactoryListener(new EditorFactoryListener() {
            @Override
            public void editorCreated(EditorFactoryEvent event) {

            }

            @Override
            public void editorReleased(EditorFactoryEvent event) {
                if (event.getEditor() == editor) {
                    gameExecutor.forceStop();
                }
            }
        }, project);
    }

    @Override
    public void update(final AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setEnabled((project != null && editor != null));
    }
}