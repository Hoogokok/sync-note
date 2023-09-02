package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.syncnote.hashtag.HashTag;
import site.syncnote.hashtag.HashTagService;
import site.syncnote.member.Member;
import site.syncnote.member.MemberRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
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
        Member author = new Member("test", "test", "test");
        memberRepository.save(author);

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
        Member author = new Member("test", "test", "test");
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
        assertThat(post.getHashTags()).hasSize(3);
        assertThat(post.getHashTags()).extracting("hashTag").contains(에세이, 산문, 시);
    }
}