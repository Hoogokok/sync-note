package site.syncnote.hashtag;

import lombok.Getter;
@Getter
public class HashTag {
    private final String name;
    private boolean deleted;

    public HashTag(String name) {
        this.name = name;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;    }
}