package site.syncnote.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.syncnote.hashtag.HashTag;

@NoArgsConstructor
@Getter
@Entity
public class PostHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;
    @ManyToOne
    private HashTag hashTag;
    private boolean deleted;

    public PostHashTag(Post post, HashTag hashTag) {
        this.post = post;
        this.hashTag = hashTag;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }
}
