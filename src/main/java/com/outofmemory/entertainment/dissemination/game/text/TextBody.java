package com.outofmemory.entertainment.dissemination.game.text;

import com.outofmemory.entertainment.dissemination.engine.Body;
import com.outofmemory.entertainment.dissemination.engine.OnBoundary;

public class TextBody extends Body {
    OnBoundary onBoundary = OnBoundary.WalkThrough;
    public boolean hasGravity = false;
    public boolean isCollisionObject = false;
}
