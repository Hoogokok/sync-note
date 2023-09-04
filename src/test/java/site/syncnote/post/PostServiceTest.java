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


import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;
    // 독립적인 테스트를 위해 서비스가 아닌 레포지토리를 직접 호출하는 방식으로 바꾸어보자

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
        assertThat(post.getHashTags()).isEmpty();
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
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        Post post = postService.write(title, content, hashtagNames, author);

        //when
        postService.delete(post.getId(), author.getId());

        //then
        assertThat(post.isDeleted()).isTrue();
        assertThat(post.getHashTags()).extracting("deleted").containsExactly(true);
    }

    @DisplayName("없는 글을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void delete_fail_if_not_exist() {
        //given
        Long id = 1L;

        //when & then
        assertThatThrownBy(() -> postService.delete(id, 0L))
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
        postService.edit(author.getId(), post.getId(), "제목", "내용", hashtagNames);

        // then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getHashTags()).isEmpty();
    }

    @DisplayName("없는 글을 수정하려고 하면 예외가 발생한다.")
    @Test
    void edit_fail_if_not_exist() {
        //given
        Long id = 1L;

        //when & then
        assertThatThrownBy(() -> postService.edit(0L, id, "제목", "내용", List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("작성자가 아닌 사람이 글을 수정하려고 하면 예외가 발생한다.")
    @Test
    void edit_fail_if_not_author() {
        //given
        String title = "title";
        String content = "content";
        Member author = memberService.join("test", "test", "1234");
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        hashtagNames.add("시");
        Post post = postService.write(title, content, hashtagNames, author);
        hashtagNames.clear();

        //when & then
        assertThatThrownBy(() -> postService.edit(0L, post.getId(), "제목", "내용", List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}