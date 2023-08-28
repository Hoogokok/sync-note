package site.syncnote.hashtag;

import lombok.Getter;

import java.util.Objects;

@Getter
public class HashTag {
    private final String name;
    private boolean deleted;

    public HashTag(String name) {
        this.name = name;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTag hashTag)) return false;
        return deleted == hashTag.deleted && Objects.equals(name, hashTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, deleted);
    }
}