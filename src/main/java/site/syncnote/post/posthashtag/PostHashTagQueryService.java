package site.syncnote.post.posthashtag;

import org.springframework.stereotype.Service;
import site.syncnote.hashtag.HashTag;
import site.syncnote.post.Post;

import java.util.List;

@Service
public class PostHashTagQueryService {
    private final PostHashTagRepository postHashTagRepository;

    public PostHashTagQueryService(PostHashTagRepository postHashTagRepository) {
        this.postHashTagRepository = postHashTagRepository;
    }

    public List<HashTag> findNotUsedHashTags(final Post post) {
        List<HashTag> hashTags = post.getHashTags().stream()
            .map(PostHashTag::getHashTag).toList();
        List<HashTag> shouldNotDeletedHashTags = postHashTagRepository.findAllByHashTagIn(hashTags).stream()
            .filter(postHashTag -> !postHashTag.isPost(post))
            .map(PostHashTag::getHashTag)
            .toList();
        return hashTags.stream()
            .filter(ht -> !shouldNotDeletedHashTags.contains(ht))
            .toList();
    }
}
