package site.syncnote.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.syncnote.hashtag.HashTag;
import site.syncnote.member.Member;
import site.syncnote.post.posthashtag.PostHashTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }
        verifyAuthor(memberId, this);
        verifyHashTags(hashTags);
        this.title = title;
        this.content = content;
        this.hashTags = mergeOldAndNewTags(hashTags);
    }

    private List<PostHashTag> mergeOldAndNewTags(List<HashTag> hashTags) {
        Stream<PostHashTag> notDeletedTags = findNotDeletedTags(hashTags);
        Stream<PostHashTag> newTags = convertNewTags(hashTags);
        return Stream.of(notDeletedTags, newTags)
            .flatMap(s -> s)
            .toList();
    }

    private Stream<PostHashTag> convertNewTags(List<HashTag> hashTags) {
        return hashTags.stream()
            .filter(hashTag -> this.hashTags.stream().map(PostHashTag::getHashTag).noneMatch(ht -> ht.equals(hashTag)))
            .map(hashTag -> new PostHashTag(this, hashTag));
    }

    private Stream<PostHashTag> findNotDeletedTags(List<HashTag> hashTags) {
        Stream<PostHashTag> notDeltedTags = this.hashTags.stream()
            .filter(postHashTag -> hashTags.contains(postHashTag.getHashTag()));
        return notDeltedTags;
    }

    public void delete(long memberId) {
        verifyAuthor(memberId, this);
        this.deleted = true;
        if (!hashTags.isEmpty()) {
            hashTags.forEach(PostHashTag::delete);
        }
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
        int oldHashTagSize = this.hashTags.size();
        int newHashTagSize = Objects.isNull(hashTags) ? 0 : hashTags.size();
        if (newHashTagSize > 5 || newHashTagSize + oldHashTagSize > 5) {
            throw new IllegalArgumentException("해시태그는 5개까지만 등록할 수 있습니다.");
        }
    }
}
