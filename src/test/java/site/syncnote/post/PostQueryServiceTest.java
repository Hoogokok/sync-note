package site.syncnote.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.member.Member;
import site.syncnote.member.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostQueryServiceTest {
    @Autowired
    PostQueryService postQueryService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("글을 찾는다.")
    @Test
    void findPost() {
        // given
        Member author = memberRepository.save(Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build());
        Post post = postRepository.save(Post.builder()
            .title("title")
            .content("content")
            .author(author)
            .build());

        // when
        Post findPost = postQueryService.findPost(post.getId());

        // then
        assertThat(findPost).isEqualTo(post);
    }

    @DisplayName("없는 글 번호를 찾으면 실패한다.")
    @Test
    void findPost_fail_if_not_exist() {
        // given
        Long notExistId = 1L;
        // when & then
        assertThatThrownBy(() -> postQueryService.findPost(notExistId))
            .isInstanceOf(IllegalArgumentException.class);
    }

}