package site.syncnote.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.post.PostHashTagRepository;

import java.util.Optional;

@Transactional
@Service
public class HashTagService {
    private HashTagRepository hashTagRepository;
    private PostHashTagRepository postHashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
    }

    public HashTag find(String name) {
        Optional<HashTag> findByName = hashTagRepository.findByName(name);
        if (findByName.isPresent()) {
            return findByName.get();
        }
        HashTag newHashTag = new HashTag(name);
        hashTagRepository.save(newHashTag);
        return newHashTag;
    }

    public void delete(HashTag hashTag) {
        if (!postHashTagRepository.existsByHashTagId(hashTag.getId())) {
            hashTag.delete();
            hashTagRepository.save(hashTag);
        }
    }
}
