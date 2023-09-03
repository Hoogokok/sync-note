package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import site.syncnote.hashtag.HashTag;
import site.syncnote.member.Member;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

    @DisplayName("회원이 제목, 본문, 해시태그로 글을 작성한다.")
    @Test
    void creat_post() {
        // given
        String title = "나의 이야기";
        String content = "나의 이야기 본문";
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");

        // when
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(member)
            .build();

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getAuthor()).isEqualTo(member);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.isDeleted()).isFalse();
    }

    @DisplayName("해시태그는 5개이상 추가할 수 없다.")
    @Test
    void createPost_fail_if_hashtag_more_then_five() {
        // given
        String title = "나의 이야기";
        String content = "나의 이야기 본문";
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        HashTag hashTag = new HashTag("에세이");
        HashTag hashTag2 = new HashTag("산수");
        HashTag hashTag3 = new HashTag("산문");
        HashTag hashTag4 = new HashTag("수필");
        HashTag hashTag5 = new HashTag("소설");
        HashTag hashTag6 = new HashTag("시");
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(member)
            .build();
        List<PostHashTag> hashTags = List.of(
            new PostHashTag(post, hashTag),
            new PostHashTag(post, hashTag2),
            new PostHashTag(post, hashTag3),
            new PostHashTag(post, hashTag4),
            new PostHashTag(post, hashTag5),
            new PostHashTag(post, hashTag6)
        );

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.addHashTag(hashTags));
    }

    @DisplayName("회원이 글을 수정한다.")
    @Test
    void edit() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        HashTag hashTag = new HashTag("에세이");
        HashTag newHashTag = new HashTag("산문");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        PostHashTag postHashTag = new PostHashTag(post, hashTag);
        PostHashTag postHashTag1 = new PostHashTag(post, newHashTag);

        // when
        post.edit("너의 이야기", "너의 이야기 본문", List.of(postHashTag, postHashTag1));

        // then
        assertThat(post.getTitle()).isEqualTo("너의 이야기");
        assertThat(post.getContent()).isEqualTo("너의 이야기 본문");
        assertThat(post.getHashTags()).containsExactly(postHashTag, postHashTag1);
    }

    @DisplayName("해시태그가 5개이상인 경우 글을 수정할 수 없다.")
    @Test
    void edit_fail_if_hashTag_more_then_five() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        HashTag hashTag = new HashTag("에세이");
        post.addHashTag(List.of(new PostHashTag(post, hashTag)));
        HashTag newHashTag1 = new HashTag("에세이");
        HashTag newHashTag2 = new HashTag("시");
        HashTag newHashTag3 = new HashTag("소설");
        HashTag newHashTag4 = new HashTag("수필");
        HashTag newHashTag5 = new HashTag("산수");
        List<PostHashTag> hashTags = List.of(
            new PostHashTag(post, newHashTag1),
            new PostHashTag(post, newHashTag2),
            new PostHashTag(post, newHashTag3),
            new PostHashTag(post, newHashTag4),
            new PostHashTag(post, newHashTag5)
        );

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit("너의 이야기", "너의 이야기 본문", hashTags));
    }

    @DisplayName("글이 삭제된 경우 수정할 수 없다.")
    @Test
    void edit_fail_if_post_deleted() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        ReflectionTestUtils.setField(post, "deleted", true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit( "너의 이야기", "본문", List.of()));
    }

    @DisplayName("글을 삭제한다.")
    @Test
    void delete() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();

        // when
        ReflectionTestUtils.setField(post, "deleted", true);

        // then
        assertThat(post.isDeleted()).isTrue();
    }
}