package net.justmili.config.core.items;

public final class CommentItem implements ConfigItem {
    private final String comment;

    public CommentItem(String comment) {
        this.comment = comment;
    }

    public String comment() { return comment; }
}