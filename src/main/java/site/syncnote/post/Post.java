package site.syncnote.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.syncnote.hashtag.HashTag;
import site.syncnote.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashTag> hashTags = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;
    private boolean deleted;

    @Builder
    public Post(String title, String content, Member author, List<HashTag> hashTags) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.hashTags = Objects.isNull(hashTags) ? new ArrayList<>() : addHashTag(hashTags);
        this.deleted = false;
    }

    private List<PostHashTag> addHashTag(List<HashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        verifyHashTags(hashTags);
        return convertPostHashTag(hashTags);
    }

    private List<PostHashTag> convertPostHashTag(List<HashTag> hashTags) {
        return hashTags.stream()
            .map(hashTag -> new PostHashTag(this, hashTag))
            .toList();
    }

    public void edit(String title, String content, List<HashTag> hashTags, Long memberId) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        verifyAuthor(memberId, this);
        verifyHashTags(hashTags);
        this.title = title;
        this.content = content;
        this.hashTags = convertPostHashTag(hashTags);
    }

    public void delete(long memberId) {
        verifyAuthor(memberId, this);
        this.deleted = true;
        if (Objects.nonNull(hashTags)) {
            hashTags.forEach(PostHashTag::delete);
        }
    }

    private void verifyAuthor(Long memberId, Post post) {
        if (!post.isAuthor(memberId)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    private boolean isAuthor(Long memberId) {
        return author.haveYou(memberId);
    }

    private void verifyHashTags(List<HashTag> hashTags) {
        int oldHashTagSize = this.hashTags.size();
        int newHashTagSize = Objects.isNull(hashTags) ? 0 : hashTags.size();
        if (newHashTagSize > 5 || newHashTagSize + oldHashTagSize > 5) {
            throw new IllegalArgumentException();
        }
    }
}
