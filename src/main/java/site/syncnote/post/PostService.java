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

    public Post write(String title, String content, List<String> hashTags, Member author) {
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
        verifyAuthor(memberId, post);
        post.delete();
        List<PostHashTag> postHashTags = post.getHashTags();
        List<HashTag> hashTags = postHashTags.stream().map(PostHashTag::getHashTag).toList();
        hashTagService.delete(hashTags, postId);

        postRepository.save(post);
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
        verifyAuthor(memberId, post);
        List<HashTag> findHashTags = hashTagService.save(hashTags);
        List<PostHashTag> postHashTags = convertPostHashTag(findHashTags, post);
        post.edit(title, content, postHashTags);

        postRepository.save(post);
    }

    private void verifyAuthor(Long memberId, Post post) {
        if (!post.isAuthor(memberId)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
