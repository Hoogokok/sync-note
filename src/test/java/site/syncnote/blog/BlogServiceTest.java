package site.syncnote.blog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.member.Member;
import site.syncnote.member.MemberService;
import site.syncnote.member.blog.BlogService;
import site.syncnote.post.Post;
import site.syncnote.post.PostService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BlogServiceTest {
    @Autowired
    BlogService blogService;
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;

    @DisplayName("회원이 작성한 글을 모두 가져온다.")
    @Test
    void getPosts() {
        // given
        String title = "title";
        String content = "content";
        Member author = memberService.join("test", "test", "1234");
        Post post = postService.write(title, content, List.of(), author);
        Post post1 = postService.write(title, content, List.of(), author);
        Post post2 = postService.write(title, content, List.of(), author);

        // when
        List<Post> posts = blogService.getPosts(author);

        // then
        assertThat(posts).hasSize(3);
        assertThat(posts).extracting("title").contains(title);
        assertThat(posts).extracting("content").contains(content);
        assertThat(posts).extracting("author").contains(author);
    }
}