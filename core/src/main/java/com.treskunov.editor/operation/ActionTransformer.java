package com.treskunov.editor.operation;

import com.treskunov.editor.Action;

/**
 * Represent Operational Transformation.
 * <p/>
 * In Operational Transformation ties are possible, for example if both actions are
 * insertion to the same position.
 * This transformer break ties in favor of first action.
 *
 * @see http://en.wikipedia.org/wiki/Operational_transformation
 */
public interface ActionTransformer {

    /**
     * Generate pair of transformed actions for incoming two actions:
     * <pre>
     *     transform(a,b) = (a',b') where a(b'(document)) == b(a'(document))
     * </pre>
     */
    ActionPair transform(Action first, Action second);
}
