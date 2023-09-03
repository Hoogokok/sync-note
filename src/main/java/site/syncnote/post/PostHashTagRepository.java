package site.syncnote.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

     boolean existsByHashTagIdAndPostIdNot(Long hashTagId, Long postId);

}
