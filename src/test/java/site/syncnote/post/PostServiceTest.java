package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.member.Member;
import site.syncnote.member.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @DisplayName("해시태그 없이 게시글을 작성한다.")
    @Test
    void write_if_not_hashTag() {
        // given
        String title = "title";
        String content = "content";
        Member author = memberService.join("test", "test", "1234");

        // when
        Post post = postService.write(title, content, List.of(), author);

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(Objects.isNull(post.getHashTags())).isTrue();
    }

    @DisplayName("글을 쓸 때 해시태그를 추가한다.")
    @Test
    void write_add_hashTag() {
        // given
        Member author = memberService.join("test", "test", "1234");
        String 에세이 = "에세이";
        String 산문 = "산문";
        String 시 = "시";
        List<String> hashTags = List.of(에세이, 산문, 시);

        // when
        Post post = postService.write("title", "content", hashTags, author);

        // then
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getHashTags()).hasSize(3);
        assertThat(post.getHashTags()).extracting("hashTag").extracting("name")
            .containsExactlyInAnyOrder(에세이, 산문, 시);
    }

    @DisplayName("글을 삭제한다.")
    @Test
    void delete() {
        //given
        String title = "title";
        String content = "content";
        Member author = memberService.join("test", "test", "1234");
        Post post = postService.write(title, content, List.of(), author);

        //when
        postService.delete(post.getId());

        //then
        assertThat(post.isDeleted()).isTrue();
    }

    @DisplayName("없는 글을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void delete_fail_if_not_exist() {
        //given
        Long id = 1L;

        //when & then
        assertThatThrownBy(() -> postService.delete(id))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("글을 수정한다.")
    @Test
    void edit() {
        // given
        String title = "title";
        String content = "content";
        Member author = memberService.join("test", "test", "1234");
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        hashtagNames.add("시");
        Post post = postService.write(title, content, hashtagNames, author);
        hashtagNames.clear();

        // when
        postService.edit(post.getId(), "제목", "내용", hashtagNames);

        // then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getHashTags()).isEmpty();
    }
}