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
    private final HashTagCommandService hashTagCommandService;
    private final HashTagQueryService hashTagQueryService;

    public PostCommandService(PostRepository postRepository,
                              HashTagCommandService hashTagCommandService,
                              HashTagQueryService hashTagQueryService) {
        this.postRepository = postRepository;
        this.hashTagCommandService = hashTagCommandService;
        this.hashTagQueryService = hashTagQueryService;
    }

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

    public void delete(Post post, Member member, List<HashTag> notUsedHashTags) {
        post.delete(member.getId());
        hashTagCommandService.delete(notUsedHashTags);
    }

    public void edit(Post post, Member member, String title, String content, List<String> hashTagNames) {
        HashTagNonExistQueryResults nonExist = hashTagQueryService.findNonExist(hashTagNames);
        hashTagCommandService.save(nonExist);
        List<HashTag> existHashTags = hashTagQueryService.findExistHashTag(hashTagNames);
        post.edit(title, content, existHashTags, member.getId());
    }
}
