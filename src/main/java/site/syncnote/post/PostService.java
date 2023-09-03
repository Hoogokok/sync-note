package site.syncnote.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.hashtag.HashTag;
import site.syncnote.hashtag.HashTagService;
import site.syncnote.member.Member;

import java.util.List;

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

    public void delete(Long id) {
        Post post = findPost(id);
        post.delete();
        postRepository.save(post);
    }

    public void edit(Long id, String title, String content, List<String> hashTags) {
        Post post = findPost(id);
        List<HashTag> findHashTags = hashTagService.save(hashTags);
        List<PostHashTag> postHashTags = findHashTags.stream().map(hashTag -> new PostHashTag(post, hashTag)).toList();
        post.edit(title, content, postHashTags);
        postRepository.save(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    private void addHashTag(Post post, List<HashTag> hashTags) {
        List<PostHashTag> postHashTags = hashTags.stream().map(hashTag -> new PostHashTag(post, hashTag))
            .toList();
        post.addHashTag(postHashTags);
    }
}
