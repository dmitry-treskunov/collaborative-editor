package com.treskunov.editor;

/**
 * Represent snapshot of the document at some point.
 * This snapshot allows to obtain document state atomically.
 */
public class DocumentSnapshot {

    private final String title;
    private final String text;
    private final int version;

    public DocumentSnapshot(String title, String text, int version) {
        this.title = title;
        this.text = text;
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public int getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }
}
