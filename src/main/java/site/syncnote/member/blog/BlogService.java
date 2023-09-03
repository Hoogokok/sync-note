package site.syncnote.member.blog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.member.Member;
import site.syncnote.post.Post;
import site.syncnote.post.PostRepository;

import java.util.List;

@Transactional
@Service
public class BlogService {
    private final PostRepository postRepository;

    public BlogService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts(Member member) {
        return postRepository.findAllByAuthor(member);
    }
}
