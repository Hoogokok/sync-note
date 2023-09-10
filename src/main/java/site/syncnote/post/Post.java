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
import java.util.stream.Collectors;

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
    private List<PostHashTag> postHashTags = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;
    private boolean deleted;

    @Builder
    public Post(String title, String content, Member author, List<HashTag> postHashTags) {
        this.title = title;
        this.content = content;
        this.author = author;
        addHashTag(postHashTags);
        this.deleted = false;
    }

    private void addHashTag(List<HashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }
        verifyHashTags(hashTags);
        postHashTags = convertPostHashTag(hashTags);
    }

    private List<PostHashTag> convertPostHashTag(List<HashTag> hashTags) {
        return hashTags.stream()
            .map(hashTag -> new PostHashTag(this, hashTag))
            .collect(Collectors.toList());
    }

    public void edit(String title, String content, List<HashTag> hashTags, Long memberId) {
        if (deleted) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }
        verifyAuthor(memberId, this);
        verifyHashTags(hashTags);
        this.title = title;
        this.content = content;
        deleteAndAddHashTags(hashTags);
    }

    public List<PostHashTag> getPostHashTags() {
        return postHashTags.stream()
            .filter(postHashTag -> !postHashTag.isDeleted())
            .collect(Collectors.toList());
    }

    private void deleteAndAddHashTags(List<HashTag> hashTags) {
        deleteUnUsedTags(hashTags);
        addNewHashTags(hashTags);
    }

    private void deleteUnUsedTags(List<HashTag> hashTags) {
        this.postHashTags.stream()
            .filter(postHashTag -> !hashTags.contains(postHashTag.getHashTag()))
            .forEach(PostHashTag::delete);
    }

    private void addNewHashTags(List<HashTag> hashTags) {
        hashTags.stream()
            .filter(hashTag -> this.postHashTags.stream().map(PostHashTag::getHashTag).noneMatch(ht -> ht.equals(hashTag)))
            .map(hashTag -> new PostHashTag(this, hashTag))
            .forEach(this.postHashTags::add);
    }

    public void delete(long memberId) {
        verifyAuthor(memberId, this);
        this.deleted = true;
        postHashTags.forEach(PostHashTag::delete);
    }

    private void verifyAuthor(Long memberId, Post post) {
        if (!post.isAuthor(memberId)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    private boolean isAuthor(Long memberId) {
        return author.has(memberId);
    }

    private void verifyHashTags(List<HashTag> hashTags) {
        int oldHashTagSize = this.postHashTags.size();
        int newHashTagSize = Objects.isNull(hashTags) ? 0 : hashTags.size();
        if (newHashTagSize > 5 || newHashTagSize + oldHashTagSize > 5) {
            throw new IllegalArgumentException("해시태그는 5개까지만 등록할 수 있습니다.");
        }
    }
}
