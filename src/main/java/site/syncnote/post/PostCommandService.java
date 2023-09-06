package site.syncnote.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.hashtag.*;
import site.syncnote.member.Member;

import java.util.List;

@Transactional
@Service
public class PostCommandService {
    private final PostRepository postRepository;
    private final HashTagService hashTagService;

    public PostCommandService(PostRepository postRepository,
                              HashTagService hashTagService) {
        this.postRepository = postRepository;
        this.hashTagService = hashTagService;
    }

    public Post write(String title, String content, List<String> hashTagNames, Member author) {
        List<HashTag> hashTags = hashTagService.write(hashTagNames);
        Post post = Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .hashTags(hashTags)
            .build();
        return postRepository.save(post);
    }

    public void delete(Post post, Member member, List<HashTag> notUsedHashTags) {
        post.delete(member.getId());
        hashTagService.delete(notUsedHashTags);
    }

    public void edit(Post post, Member member, String title, String content, List<String> hashTagNames) {
        List<HashTag> hashTags = hashTagService.write(hashTagNames);
        post.edit(title, content, hashTags, member.getId());
    }
}
