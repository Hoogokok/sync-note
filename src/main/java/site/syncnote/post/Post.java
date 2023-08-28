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
        this.hashTags = hashTags;
        this.author = author;
        this.deleted = false;
    }

    public void edit(String title, String content) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }
}
