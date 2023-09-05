package site.syncnote.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PostQueryService {
    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post findPost(final Long id) {
        return postRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
