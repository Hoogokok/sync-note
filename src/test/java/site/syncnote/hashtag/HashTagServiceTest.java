package site.syncnote.hashtag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class HashTagServiceTest {

    @Autowired
    HashTagService hashTagService;
    @Autowired
    HashTagRepository hashTagRepository;

    @DisplayName("있는 해시태그를 사용한다.")
    @Test
    void write() {
        // given
        HashTag hashTag1 = hashTagRepository.save(new HashTag("test1"));
        HashTag hashTag2 = hashTagRepository.save(new HashTag("test2"));
        HashTag hashTag3 = hashTagRepository.save(new HashTag("test3"));

        // when
        List<HashTag> hashTags = hashTagService.write(List.of("test1", "test2", "test3"));

        // then
        assertThat(hashTags).containsExactlyInAnyOrder(hashTag1, hashTag2, hashTag3);
    }

    @DisplayName("없는 해시태그를 새로 쓴다.")
    @Test
    void write_if_not_exist() {
        // given
        List<String> hashTagNames = List.of("test1", "test2", "test3");

        // when
        List<HashTag> newHashTags = hashTagService.write(hashTagNames);

        // then
        assertThat(newHashTags).extracting("name").containsExactlyInAnyOrder("test1", "test2", "test3");
    }

    @DisplayName("해시태그를 삭제한다.")
    @Test
    void delete() {
        // given
        HashTag 에세이 = new HashTag("에세이");
        HashTag 시 = new HashTag("시");
        hashTagRepository.save(에세이);
        hashTagRepository.save(시);
        List<HashTag> hashTags = new ArrayList<>();
        hashTags.add(에세이);
        hashTags.add(시);
        // when
        hashTagService.delete(hashTags);

        // then
        assertThat(hashTags).extracting("deleted").containsExactly(true, true);
    }

}