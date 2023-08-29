package site.syncnote.blog;

import lombok.Builder;
import lombok.Getter;
import site.syncnote.member.Member;
import site.syncnote.post.Post;

import java.util.List;

@Getter
public class Blog {
    private Member owner;
    private List<Post> posts;

    @Builder
    public Blog(Member owner, List<Post> posts) {
        this.owner = owner;
        this.posts = posts;
    }
}
