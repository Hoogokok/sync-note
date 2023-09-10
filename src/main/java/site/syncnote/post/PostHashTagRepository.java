package site.syncnote.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.syncnote.hashtag.HashTag;

import java.util.List;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

     List<PostHashTag> findAllByHashTagIn(List<HashTag> hashTags);
}
