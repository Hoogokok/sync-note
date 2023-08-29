package site.syncnote.post;

import lombok.Builder;
import lombok.Getter;
import site.syncnote.hashtag.HashTag;
import site.syncnote.member.Member;

import java.util.List;

@Getter
public class Post {
    private String title;
    private String content;
    private List<HashTag> hashTags;
    private final Member author;
    private boolean deleted;

    @Builder
    public Post(String title, String content, List<HashTag> hashTags, Member author) {
        this.title = title;
        this.content = content;
        verifyHashTags(hashTags);
        this.hashTags = hashTags;
        this.author = author;
        this.deleted = false;
    }

    public void edit(String title, String content, List<HashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        verifyHashTags(hashTags);
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }

    private void verifyHashTags(List<HashTag> hashTags) {
        if (hashTags != null && hashTags.size() > 5) {
            throw new IllegalArgumentException();
        }
    }

    public void delete() {
        this.deleted = true;
    }
}
