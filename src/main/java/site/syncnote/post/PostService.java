package site.syncnote.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.hashtag.*;
import site.syncnote.member.Member;

import java.util.List;

@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagCommandService hashTagCommandService;
    private final HashTagQueryService hashTagQueryService;

    public PostService(PostRepository postRepository,
                       PostHashTagRepository postHashTagRepository,
                       HashTagCommandService hashTagCommandService,
                       HashTagQueryService hashTagQueryService) {
        this.postRepository = postRepository;
        this.postHashTagRepository = postHashTagRepository;
        this.hashTagCommandService = hashTagCommandService;
        this.hashTagQueryService = hashTagQueryService;
    }

    // TODO: 2023/09/03 CQRS 적용하기
    public Post write(String title, String content, List<String> hashTagNames, Member author) {
        HashTagNonExistQueryResults nonExist = hashTagQueryService.findNonExist(hashTagNames);
        hashTagCommandService.save(nonExist);
        List<HashTag> existHashTag = hashTagQueryService.findExistHashTag(hashTagNames);
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .hashTags(existHashTag)
            .build();
        return postRepository.save(post);
    }

    public void delete(Long postId, Long memberId) {
        Post post = findPost(postId);
        post.delete(memberId);
        List<HashTag> notUsedHashTags = findNotUsedHashTags(post);
        hashTagCommandService.delete(notUsedHashTags);
    }

    private List<HashTag> findNotUsedHashTags(Post post) {
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

    public void edit(Long postId, Long memberId, String title, String content, List<String> hashTagNames) {
        HashTagNonExistQueryResults nonExist = hashTagQueryService.findNonExist(hashTagNames);
        hashTagCommandService.save(nonExist);
        List<HashTag> existHashTags = hashTagQueryService.findExistHashTag(hashTagNames);
        Post post = findPost(postId);
        post.edit(title, content, existHashTags, memberId);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
