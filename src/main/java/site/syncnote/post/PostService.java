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
    public PostRepository postRepository;
    public HashTagService hashTagService;

    public PostService(PostRepository postRepository, HashTagService hashTagService) {
        this.postRepository = postRepository;
        this.hashTagService = hashTagService;
    }

    public Post write(String title, String content, List<String> hashTags, Member author) {
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();

        if (hashTags == null || hashTags.isEmpty()) {
            return postRepository.save(post);
        }
        List<HashTag> findHashTags = hashTagService.save(hashTags);
        addHashTag(post, findHashTags);

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
        return postRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    /** toList로 반환하면 immutable list가 반환되어 수정시 clear()가 불가능하다
     *  만약에 toList를 쓰고 싶다면 post edit시 new ArrayList<>()안에 다시 넣어줘야한다.
     *  그래서 stream().collect(Collectors.toList())를 쓴다.
     */
    private void addHashTag(Post post, List<HashTag> hashTags) {
        List<PostHashTag> postHashTags = convertPostHashTag(hashTags, post);
        post.addHashTag(postHashTags);
    }

    private List<PostHashTag> convertPostHashTag(List<HashTag> findHashTags, Post post) {
        return findHashTags.stream().map(hashTag -> new PostHashTag(post, hashTag)).collect(Collectors.toList());
    }

}
