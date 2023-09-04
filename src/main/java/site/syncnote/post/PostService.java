package site.syncnote.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.hashtag.HashTag;
import site.syncnote.hashtag.HashTagService;
import site.syncnote.member.Member;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagService hashTagService;

    public PostService(PostRepository postRepository,
                       PostHashTagRepository postHashTagRepository,
                       HashTagService hashTagService) {
        this.postRepository = postRepository;
        this.postHashTagRepository = postHashTagRepository;
        this.hashTagService = hashTagService;
    }

    // TODO: 2023/09/03 CQRS 적용하기
    public Post write(String title, String content, List<String> hashTagNames, Member author) {
        List<HashTag> hashTags = hashTagService.saveOrFind(hashTagNames);
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .hashTags(hashTags)
            .build();
        return postRepository.save(post);
    }

    public void delete(Long postId, Long memberId) {
        Post post = findPost(postId);
        post.delete(memberId);
        List<HashTag> notUsedHashTags = findNotUsedHashTags(post);
        hashTagService.delete(notUsedHashTags);
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

    public void edit(Long postId, Long memberId, String title, String content, List<String> hashTags) {
        Post post = findPost(postId);
        List<HashTag> findHashTags = hashTagService.saveOrFind(hashTags);
        post.edit(title, content, findHashTags, memberId);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
