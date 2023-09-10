package site.syncnote.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import site.syncnote.hashtag.HashTag;
import site.syncnote.member.Member;

import java.util.ArrayList;
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


        // when & then
        assertThrows(IllegalArgumentException.class, () -> Post.builder().title(title).content(content).author(member).postHashTags(List.of(hashTag, hashTag2, hashTag3, hashTag4, hashTag5, hashTag6)).build());
    }

    @DisplayName("회원이 글을 수정한다.")
    @Test
    void edit() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        HashTag oldHashTag = new HashTag("에세이");
        HashTag oldHashTag2 = new HashTag("구문");
        HashTag newHashTag = new HashTag("산문");
        HashTag newHashTag2 = new HashTag("시");
        List<HashTag> oldHashTags = new ArrayList<>();
        oldHashTags.add(oldHashTag);
        oldHashTags.add(oldHashTag2);
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .postHashTags(oldHashTags)
            .build();
        PostHashTag tag = post.getPostHashTags().stream().filter(postHashTag -> postHashTag.getHashTag().equals(oldHashTag)).findAny().get();
        ReflectionTestUtils.setField(member, "id", 1L);
        List<HashTag> newHashTags = new ArrayList<>();
        newHashTags.add(newHashTag);
        newHashTags.add(oldHashTag);
        newHashTags.add(newHashTag2);

        // when
        post.edit("너의 이야기", "너의 이야기 본문", newHashTags, member.getId());

        // then
        assertThat(post.getTitle()).isEqualTo("너의 이야기");
        assertThat(post.getContent()).isEqualTo("너의 이야기 본문");
        assertThat(post.getPostHashTags()).extracting("hashTag").contains(newHashTag, oldHashTag, newHashTag2);
        assertThat(post.getPostHashTags()).contains(tag);
    }

    @DisplayName("해시태그가 5개이상인 경우 글을 수정할 수 없다.")
    @Test
    void edit_fail_if_hashTag_more_then_five() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        ReflectionTestUtils.setField(member, "id", 1L);
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        HashTag hashTag = new HashTag("에세이");
        HashTag newHashTag1 = new HashTag("에세이");
        HashTag newHashTag2 = new HashTag("시");
        HashTag newHashTag3 = new HashTag("소설");
        HashTag newHashTag4 = new HashTag("수필");
        HashTag newHashTag5 = new HashTag("산수");
        List<HashTag> hashTags = List.of(hashTag, newHashTag1, newHashTag2, newHashTag3, newHashTag4, newHashTag5);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit("너의 이야기", "너의 이야기 본문", hashTags, member.getId()));
    }

    @DisplayName("글이 삭제된 경우 수정할 수 없다.")
    @Test
    void edit_fail_if_post_deleted() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        ReflectionTestUtils.setField(member, "id", 1L);
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        ReflectionTestUtils.setField(post, "deleted", true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit("너의 이야기", "본문", List.of(), member.getId()));
    }

    @DisplayName("본인 글이 아닌 경우 수정할 수 없다.")
    @Test
    void edit_fail_if_not_author() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.edit("너의 이야기", "본문", List.of(), 2L));
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

    @DisplayName("본인 글이 아닌 경우 삭제할 수 없다.")
    @Test
    void delete_fail_if_not_author() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.delete(2L));
    }

    @DisplayName("본인 글이 아닌 경우 삭제할 수 없다.")
    @Test
    void delete_fail_if_() {
        // given
        String name = "나숙희";
        Member member = new Member("test@gmail.com", name, "1234");
        Post post = Post.builder()
            .title("나의 이야기")
            .content("나의 이야기 본문")
            .author(member)
            .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> post.delete(2L));
    }
}