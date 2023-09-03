package site.syncnote.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private List<PostHashTag> hashTags;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;
    private boolean deleted;

    @Builder
    public Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.deleted = false;
    }

    public void edit(String title, String content, List<PostHashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        verifyHashTags(hashTags);
        this.title = title;
        this.content = content;
        this.hashTags.clear();
        this.hashTags.addAll(hashTags);
    }

    public void addHashTag(List<PostHashTag> hashTags) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        verifyHashTags(hashTags);
        this.hashTags = hashTags;
    }

    private void verifyHashTags(List<PostHashTag> hashTags) {
        if (hashTags == null) {
            throw new IllegalArgumentException();
        }

        int oldHashTagSize = Objects.isNull(this.hashTags) ? 0 : this.hashTags.size();
        int newHashTagSize = hashTags.size();
        if (newHashTagSize > 5 || newHashTagSize + oldHashTagSize > 5) {
            throw new IllegalArgumentException();
        }
    }

    public void delete() {
        this.deleted = true;
    }
}
