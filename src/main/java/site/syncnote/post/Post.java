package site.syncnote.post;

import lombok.Builder;
import lombok.Getter;
import site.syncnote.hashtag.HashTag;

import java.util.List;

@Getter
public class Post {
    private String title;
    private String content;
    private List<HashTag> hashTags;
    private String author;
    private boolean deleted;

    @Builder
    public Post(String title, String content, List<HashTag> hashTags, String author) {
        this.title = title;
        this.content = content;
        verifyHashTags(hashTags);
        this.hashTags = hashTags;
        this.author = author;
        this.deleted = false;
    }

    private void verifyHashTags(List<HashTag> hashTags) {
        if (hashTags != null && hashTags.size() > 5) {
            throw new IllegalArgumentException();
        }
    }

    public void edit(String title, String content, List<HashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }

    public void delete() {
        this.deleted = true;
    }
}
