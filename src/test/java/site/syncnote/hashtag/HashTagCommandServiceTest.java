package site.syncnote.hashtag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HashTagCommandServiceTest {
    @Autowired
    HashTagCommandService hashTagCommandService;

    @DisplayName("해시태그 조회 결과가 존재하면 저장한다.")
    @Test
    void save_hashTag_result_exist() {
        // given
        HashTag 에세이 = new HashTag("에세이");
        HashTag 시 = new HashTag("시");
        HashTag 산문 = new HashTag("산문");
        List<HashTag> hashTagNames = new ArrayList<>();
        hashTagNames.add(에세이);
        hashTagNames.add(시);
        hashTagNames.add(산문);
        HashTagNonExistQueryResults queryResults = new HashTagNonExistQueryResults(hashTagNames);

        // when
        List<HashTag> hashTags = hashTagCommandService.save(queryResults);

        // then
        assertThat(hashTags).containsExactly(에세이, 시, 산문);
        assertThat(hashTags).hasSize(3);
    }

    @DisplayName("해시태그 조회 결과가 없다면 저장 하지 않는다.")
    @Test
    void save_not_if_empty_hashTagQueryResult() {
        // given
        List<HashTag> hashTagNames = new ArrayList<>();
        HashTagNonExistQueryResults queryResults = new HashTagNonExistQueryResults(hashTagNames);

        // when
        List<HashTag> hashTags = hashTagCommandService.save(queryResults);

        // then
        assertThat(hashTags).isEmpty();
    }

    @DisplayName("해시태그를 삭제한다.")
    @Test
    void delete() {
        // given
        HashTag 에세이 = new HashTag("에세이");
        HashTag 시 = new HashTag("시");
        List<HashTag> hashTagNames = new ArrayList<>();
        hashTagNames.add(에세이);
        hashTagNames.add(시);
        HashTagNonExistQueryResults queryResults = new HashTagNonExistQueryResults(hashTagNames);
        List<HashTag> hashTags = hashTagCommandService.save(queryResults);

        // when
        hashTagCommandService.delete(hashTags);

        // then
        assertThat(hashTags).extracting("deleted").containsExactly(true, true);
    }
}