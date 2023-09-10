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
    private final HashTagService hashTagService;
    private final PostHashTagRepository postHashTagRepository;


    public PostService(PostRepository postRepository, HashTagService hashTagService, PostHashTagRepository postHashTagRepository) {
        this.postRepository = postRepository;
        this.hashTagService = hashTagService;
        this.postHashTagRepository = postHashTagRepository;
    }

    public Post write(String title, String content, List<String> hashTagNames, Member author) {
        List<HashTag> hashTags = hashTagService.write(hashTagNames);
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .postHashTags(hashTags)
            .build();
        return postRepository.save(post);
    }

    public void delete(Long postId, Member member) {
        Post post = findPost(postId);
        List<HashTag> notUsedHashTags = findNotUsedHashTags(post);
        post.delete(member.getId());
        hashTagService.delete(notUsedHashTags);
    }

    public void edit(Long postId, Member member, String title, String content, List<String> hashTagNames) {
        Post post = findPost(postId);
        List<HashTag> hashTags = hashTagService.write(hashTagNames);
        post.edit(title, content, hashTags, member.getId());
    }

    private List<HashTag> findNotUsedHashTags(final Post post) {
        List<HashTag> hashTags = post.getPostHashTags().stream()
            .map(PostHashTag::getHashTag).toList();
        List<HashTag> shouldNotDeletedHashTags = postHashTagRepository.findAllByHashTagIn(hashTags).stream()
            .filter(postHashTag -> !postHashTag.isPost(post))
            .map(PostHashTag::getHashTag)
            .toList();
        return hashTags.stream()
            .filter(ht -> !shouldNotDeletedHashTags.contains(ht))
            .toList();
    }

    private Post findPost(final Long id) {
        return postRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }

}
