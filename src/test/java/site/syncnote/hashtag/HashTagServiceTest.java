package site.syncnote.hashtag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HashTagServiceTest {
    @Autowired
    HashTagService hashTagService;

    @AfterEach
    void tearDown() {
        hashTagService.deleteAllInBatch();
    }

    @DisplayName("해시태그를 여러개 찾는다.")
    @Test
    void find_multi_hashTagNames() {
        // given
        String 에세이 = "에세이";
        String 시 = "시";
        String 산문 = "산문";
        List<String> hashTagNames = List.of(에세이, 시, 산문);

        // when
        List<HashTag> hashTags = hashTagService.find(hashTagNames);

        // then
        assertThat(hashTags).extracting("name").containsExactlyInAnyOrder(에세이, 시, 산문);
        assertThat(hashTags).hasSize(3);
    }

    @DisplayName("기존에 생성한 해시태그는 재사용한다.")
    @Test
    void find_multi_hashTagNames_if_exisit_reuse() {
        // given
        String 에세이 = "에세이";
        String 시 = "시";
        String 산문 = "산문";
        List<String> hashTagNames = List.of(에세이, 시, 산문);
        List<HashTag> hashTags = hashTagService.find(hashTagNames);

        // when
        List<HashTag> hashTags2 = hashTagService.find(hashTagNames);

        // then
        assertThat(hashTags).isEqualTo(hashTags2);
    }

    @DisplayName("해시태그를 삭제한다.")
    @Test
    void delete() {
        // given
        String hashTagName = "에세이";
        String hashTagName2 = "시";
        List<HashTag> hashTags = hashTagService.find(List.of(hashTagName, hashTagName2));

        // when
        hashTagService.delete(hashTags);

        // then
        assertThat(hashTags).extracting("deleted").containsExactly(true, true);
    }
}