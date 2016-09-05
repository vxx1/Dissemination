package com.outofmemory.entertainment.dissemination.engine;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.outofmemory.entertainment.dissemination.GameTypedHandler;

public class GameExecutor {
    private final IntellijContext intellijContext;
    private GameLoop gameLoop;

    private volatile boolean isStopped = false;
    private volatile boolean isForceStopped = false;

    private final Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            TypedActionHandler typedActionHandler = intellijContext.typedAction.getHandler();
            intellijContext.typedAction.setupHandler(new GameTypedHandler(intellijContext.inputKey));
            try {
                while (!isStopped && !isForceStopped) {
                    long updateStarted = System.currentTimeMillis();
                    gameLoop.update();
                    long updateTime = System.currentTimeMillis() - updateStarted;
                    long timeToSleep = Math.max(0, 50 - updateTime);
                    if (timeToSleep > 0) {
                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException e) {
                            isStopped = true;
                        }
                    }
                }
                showNotification();
            } finally {
                intellijContext.typedAction.setupHandler(typedActionHandler);
                if (!isForceStopped) {
                    gameLoop.release();
                }
            }
        }
    });

    private void showNotification() {
        Notifications.Bus.notify(new Notification(
                "com.outofmemory.entertainment.dissemination",
                "<b>Dissemination</b>",
                "<b>If you like playing it, " +
                        "you may like <a href=\"https://outofmemory-entertainment.com/games" +
                        "?utm_campaign=plugin&utm_medium=idea&utm_source=popup\">other our games</a></b>",
                NotificationType.INFORMATION,
                new NotificationListener.UrlOpeningListener(true)));
    }


    public GameExecutor(IntellijContext intellijContext) {
        this.intellijContext = intellijContext;
    }

    public void startGame(Scene scene) {
        if (gameLoop != null) {
            gameLoop.release();
        }
        gameLoop = scene.buildLoop(intellijContext, this);
        gameLoop.start();

        if (!gameThread.isAlive()) {
            gameThread.start();
        }
    }

    public void stop() {
        isStopped = true;
    }

    public void forceStop() {
        gameLoop.release();
        isForceStopped = true;
    }
}
