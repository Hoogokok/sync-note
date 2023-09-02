package site.syncnote.hashtag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HashTagServiceTest {
    @Autowired
    HashTagService hashTagService;

    @DisplayName("해시태그를 찾는다.")
    @Test
    void find() {
        // given
        String hashTagName = "에세이";

        // when
        HashTag hashTag = hashTagService.find(hashTagName);

        // then
        assertThat(hashTag.getName()).isEqualTo(hashTagName);
    }

    @DisplayName("기존에 생성한 해시태그는 재사용한다.")
    @Test
    void If_hashTag_already_exists_reuse() {
        // given
        String hashTagName = "산문";

        // when
        HashTag hashTag = hashTagService.find(hashTagName);
        HashTag hashTag2 = hashTagService.find(hashTagName);

        // then
        assertThat(hashTag).isEqualTo(hashTag2);
    }

    @DisplayName("해시태그를 삭제한다.")
    @Test
    void delete() {
        // given
        String hashTagName = "에세이";
        HashTag hashTag = hashTagService.find(hashTagName);

        // when
        hashTagService.delete(hashTag);

        // then
        assertThat(hashTag.isDeleted()).isTrue();
    }
}