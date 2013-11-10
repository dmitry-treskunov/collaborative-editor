package com.treskunov.editor.operation;

import com.treskunov.editor.Action;

/**
 * Data structure for pair of actions.
 * <p/>
 * It is needed for {@link ActionTransformer#transform(Action, Action)}
 * that should return pair of actions together.
 */
final class ActionPair {
    private Action first;
    private Action second;

    public ActionPair(Action first, Action second) {
        this.first = first;
        this.second = second;
    }

    public Action getSecond() {
        return second;
    }

    public Action getFirst() {
        return first;
    }

    public ActionPair swap() {
        return new ActionPair(second, first);
    }
}
