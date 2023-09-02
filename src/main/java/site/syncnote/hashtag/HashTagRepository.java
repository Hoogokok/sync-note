package site.syncnote.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    public Optional<HashTag> findByName(String name);
}
