package android.bignerdranch.mapboxtest;

import java.util.Objects;

public class Comment {
    private final String walkId;
    private final String text;
    private final String id;


    public Comment(String walkId,String text,String id) {
        this.walkId = walkId;
        this.text=text;
        this.id = id;
    }

    public String getWalkId() {
        return walkId;
    }


    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(walkId, comment.walkId) &&
                Objects.equals(text, comment.text) &&
                Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walkId, text, id);
    }
}
