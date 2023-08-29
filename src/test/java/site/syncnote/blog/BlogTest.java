package site.syncnote.blog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import site.syncnote.member.Member;
import site.syncnote.post.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BlogTest {
    @DisplayName("블로그를 생성한다.")
    @Test
    void create_blog() {
        // given
        Member owner = Member.builder()
            .email("test@gmail.com")
            .name("tester")
            .password("1234")
            .build();
        Post post = Post.builder()
            .title("title")
            .content("content")
            .author(owner)
            .build();
        List<Post> posts = List.of(post);

        // when
        Blog blog = Blog.builder()
            .owner(owner)
            .posts(posts)
            .build();

        // then
        assertThat(blog.getOwner()).isEqualTo(owner);
        assertThat(blog.getPosts()).containsExactly(post);
    }
}