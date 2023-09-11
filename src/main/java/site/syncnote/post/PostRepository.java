package site.syncnote.post;

import org.springframework.data.jpa.repository.JpaRepository;
import site.syncnote.member.Member;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthor(Member member);
}
