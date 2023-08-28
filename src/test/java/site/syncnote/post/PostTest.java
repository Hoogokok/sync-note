package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import site.syncnote.hashtag.HashTag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

    @DisplayName("회원이 제목, 본문, 해시태그로 글을 작성한다.")
    @Test
    void creatPost() {
        // given
        String title = "나의 이야기";
        String content = "나의 이야기 본문";
        String author = "나숙희";
        HashTag hashTag = new HashTag("에세이");
        List<HashTag> hashtags = List.of(hashTag);

        // when
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .hashTags(hashtags)
            .build();

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getHashTags()).containsExactly(hashTag);
        assertThat(post.isDeleted()).isFalse();
    }

    @DisplayName("회원이 글을 수정한다.")
    @Test
    void edit() {
        // given
        HashTag hashTag = new HashTag("에세이");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author("나숙희")
            .hashTags(List.of(hashTag))
            .build();

        // when
        HashTag newHashTag = new HashTag("산문");
        post.edit("너의 이야기", "너의 이야기 본문", List.of(newHashTag));

        // then
        assertThat(post.getTitle()).isEqualTo("너의 이야기");
        assertThat(post.getContent()).isEqualTo("너의 이야기 본문");
        assertThat(post.getHashTags()).containsExactly(newHashTag);
    }

    @DisplayName("글이 삭제된 경우 수정할 수 없다.")
    @Test
    void edit_fail_if_post_deleted() {
        // given
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author("나숙희")
            .build();
        ReflectionTestUtils.setField(post, "deleted", true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit("너의 이야기", "본문", List.of()));
    }
}