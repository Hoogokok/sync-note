package site.syncnote.hashtag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class HashTagQueryServiceTest {
    @Autowired
    HashTagQueryService hashTagQueryService;
    @Autowired
    HashTagRepository hashTagRepository;

    @DisplayName("있는 해시태그를 찾는다.")
    @Test
    void findExistHashTag() {
        // given
        HashTag hashTag1 = hashTagRepository.save(new HashTag("test1"));
        HashTag hashTag2 = hashTagRepository.save(new HashTag("test2"));
        HashTag hashTag3 = hashTagRepository.save(new HashTag("test3"));

        // when
        List<HashTag> existHashTag = hashTagQueryService.findExistHashTag(List.of("test1", "test2", "test3"));

        // then
        assertThat(existHashTag).containsExactlyInAnyOrder(hashTag1, hashTag2, hashTag3);
    }

    @DisplayName("없는 해시태그 이름을 조회하면 새 해시태그를 만들고 그 결과를 반환한다.")
    @Test
    void findNonExist() {
        // given
        List<String> hashTagNames = List.of("test1", "test2", "test3");

        // when
        HashTagNonExistQueryResults nonExist = hashTagQueryService.findNonExist(hashTagNames);

        // then
        assertThat(nonExist.getNonExistHashTags()).extracting(HashTag::getName).containsExactlyInAnyOrder("test1", "test2", "test3");
        assertThat(nonExist.isAnyNonExist()).isTrue();
    }

    @DisplayName("해시태그가 이미 존재한다면 결과는 빈 값이다.")
    @Test
    void findNonExist_result_false() {
        // given
        HashTag hashTag1 = hashTagRepository.save(new HashTag("test1"));
        HashTag hashTag2 = hashTagRepository.save(new HashTag("test2"));
        HashTag hashTag3 = hashTagRepository.save(new HashTag("test3"));
        List<String> hashTagNames = List.of("test1", "test2", "test3");

        // when
        HashTagNonExistQueryResults nonExist = hashTagQueryService.findNonExist(hashTagNames);

        // then
        assertThat(nonExist.getNonExistHashTags()).isEmpty();
        assertThat(nonExist.isAnyNonExist()).isFalse();
    }

}