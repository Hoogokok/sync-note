package site.syncnote.hashtag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashTagTest {

    @DisplayName("해시태그를 생성할 수 있다.")
    @Test
    void create_hashtag() {
        // given && when
        HashTag hashTag = new HashTag("에세이");

        // then
        assertThat(hashTag.getName()).isEqualTo("에세이");
        assertThat(hashTag.isDeleted()).isFalse();
    }

    @DisplayName("해시태그를 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        HashTag hashTag = new HashTag("에세이");

        // when
        hashTag.delete();

        // then
        assertThat(hashTag.isDeleted()).isTrue();
    }
}