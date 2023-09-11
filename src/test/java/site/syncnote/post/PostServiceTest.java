package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.SyncNoteIntegrationTest;
import site.syncnote.member.Member;
import site.syncnote.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.*;

@Transactional
class PostServiceTest extends SyncNoteIntegrationTest {
    @Autowired
    PostService postService;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("해시태그 없이 게시글을 작성한다.")
    @Test
    void write_if_not_hashTag() {
        // given
        String title = "title";
        String content = "content";
        Member author = memberRepository.save(Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build());

        // when
        Post post = postService.write(title, content, List.of(), author);

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.findPostHashTags()).isEmpty();
    }

    @DisplayName("글을 쓸 때 해시태그를 추가한다.")
    @Test
    void write_add_hashTag() {
        // given
        Member author = Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build();
        memberRepository.save(author);
        String 에세이 = "에세이";
        String 산문 = "산문";
        String 시 = "시";
        List<String> hashTags = List.of(에세이, 산문, 시);

        // when
        Post post = postService.write("title", "content", hashTags, author);

        // then
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.findPostHashTags()).hasSize(3);
        assertThat(post.findPostHashTags()).extracting("hashTag").extracting("name")
            .containsExactlyInAnyOrder(에세이, 산문, 시);
    }

    @DisplayName("글을 삭제한다.")
    @Test
    void delete() {
        //given
        String title = "title";
        String content = "content";
        Member author = Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build();
        memberRepository.save(author);
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        Post post = postService.write(title, content, hashtagNames, author);

        //when
        postService.delete(post.getId(), author);

        //then
        assertThat(post.isDeleted()).isTrue();
        assertThat(post.getPostHashTags()).extracting("deleted").containsExactly(true);
    }

    @DisplayName("글을 수정한다.")
    @Test
    void edit() {
        // given
        String title = "title";
        String content = "content";
        Member author = Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build();
        memberRepository.save(author);
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        hashtagNames.add("시");
        Post post = postService.write(title, content, hashtagNames, author);
        hashtagNames.clear();

        // when
        postService.edit(post.getId(), author, "제목", "내용", hashtagNames);

        // then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.findPostHashTags()).isEmpty();
    }

    @DisplayName("작성자가 아닌 사람이 글을 수정하려고 하면 예외가 발생한다.")
    @Test
    void edit_fail_if_not_author() {
        //given
        String title = "title";
        String content = "content";
        Member author = Member.builder()
            .name("test")
            .email("test")
            .password("1234")
            .build();
        Member notAuthor = Member.builder()
            .name("test2")
            .email("test2")
            .password("12342")
            .build();
        memberRepository.save(author);
        memberRepository.save(notAuthor);
        List<String> hashtagNames = new ArrayList<>();
        hashtagNames.add("에세이");
        hashtagNames.add("시");
        Post post = postService.write(title, content, hashtagNames, author);
        hashtagNames.clear();

        //when & then
        assertThatThrownBy(() -> postService.edit(post.getId(), notAuthor, "제목", "내용", List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}