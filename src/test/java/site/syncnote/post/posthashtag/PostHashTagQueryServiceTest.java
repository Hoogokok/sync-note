package site.syncnote.post.posthashtag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.hashtag.HashTag;
import site.syncnote.hashtag.HashTagRepository;
import site.syncnote.post.Post;
import site.syncnote.post.PostRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostHashTagQueryServiceTest {
    @Autowired
    PostHashTagQueryService postHashTagQueryService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashTagRepository hashTagRepository;

    @DisplayName("사용되지 않은 해시태그를 찾는다.")
    @Test
    void findNotUsedHashTags() {
        // given
        HashTag hashTag1 = hashTagRepository.save(new HashTag("test1"));
        HashTag hashTag2 = hashTagRepository.save(new HashTag("test2"));
        Post post = Post.builder()
            .title("title")
            .content("content")
            .hashTags(List.of(hashTag1, hashTag2))
            .build();
        Post post2 = Post.builder()
            .title("title")
            .content("content")
            .hashTags(List.of(hashTag1))
            .build();
        postRepository.save(post);
        postRepository.save(post2);

        // when
        List<HashTag> notUsedHashTags = postHashTagQueryService.findNotUsedHashTags(post);

        // then
        assertThat(notUsedHashTags).containsExactly(hashTag2);
    }
}