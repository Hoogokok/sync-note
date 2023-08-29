package site.syncnote.hashtag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashTagServiceTest {

    @DisplayName("해시태그를 찾는다.")
    @Test
    void find() {
        // given
        String hashTagName = "에세이";
        // when
        HashTagService hashTagService = new HashTagService();
        HashTag hashTag = hashTagService.find(hashTagName);

        // then
        assertThat(hashTag.getName()).isEqualTo(hashTagName);
    }

    @DisplayName("기존에 생성한 해시태그는 재사용한다.")
    @Test
    void 해시태그가_이미_존재하면_재사용한다() {
        // given
        String hashTagName = "에세이";

        // when
        HashTagService hashTagService = new HashTagService();
        HashTag hashTag = hashTagService.find(hashTagName);
        HashTag hashTag2 = hashTagService.find(hashTagName);

        // then
        assertThat(hashTag).isEqualTo(hashTag2);
    }
}